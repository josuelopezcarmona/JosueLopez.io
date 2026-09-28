package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;
/**
 * Tests InventoryController on its own, without the rest of the application.
 *
 * This class and InventoryControllerTest test the same two endpoints on purpose.
 * InventoryControllerTest starts the whole application with @SpringBootTest and
 * talks to a real MySQL database, so it proves that the controller, the service,
 * the repository, and the database all work together.  That takes several seconds
 * and needs MySQL running.
 *
 * This class starts only Spring's web layer with @WebMvcTest and hands the
 * controller a Mockito mock in place of the real InventoryService, using an
 * annotation called @MockitoBean.  Nothing here touches a database.  It tests
 * less of the system, and in exchange it can do three things the other test
 * cannot:
 *
 *   1. decide exactly what the service returns, so the controller can be checked
 *      against values that would be awkward to arrange in a real database;
 *   2. inspect what the controller handed down to the service, which is the only
 *      way to prove the JSON request body was bound to an InventoryDto correctly;
 *   3. make the service fail on command, so error handling can be tested at all.
 *
 * Neither style replaces the other.  Reach for this one while working on a
 * controller: it starts its Spring context roughly ten times faster than
 * InventoryControllerTest does, and it passes with no MySQL running at all, so it
 * still works before the database is set up.  Keep an integration test around too,
 * to catch the wiring mistakes that a mock will happily hide.
 *
 * Note @MockitoBean and not @MockBean.  They do the same job, but @MockBean was
 * deprecated in Spring Boot 3.4, so it is what older tutorials will show you.
 *
 * @author CSC 326 Course Staff
 */
@WebMvcTest(InventoryController.class)
public class InventoryControllerWebMvcTest {

	/** Mock MVC for testing the controller without a running server. */
	@Autowired
	private MockMvc mvc;

	/**
	 * Stands in for the real InventoryService.  @WebMvcTest does not create the
	 * application's @Service beans, so without this the controller would have
	 * nothing to call and the test context would fail to start.
	 */
	@MockitoBean
	private InventoryService inventoryService;

	/**
	 * Tests that GET /api/inventory returns whatever the service reports, as JSON.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	public void testGetInventory() throws Exception {
		InventoryDto inventory = new InventoryDto(1L, java.util.List.of(
				new Ingredient("Coffee", 5),
				new Ingredient("Milk", 10),
				new Ingredient("Sugar", 15),
				new Ingredient("Chocolate", 20)));
		when(inventoryService.getInventory()).thenReturn(inventory);

		mvc.perform(get("/api/inventory"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients[0].quantity").value(5))
				.andExpect(jsonPath("$.ingredients[1].quantity").value(10))
				.andExpect(jsonPath("$.ingredients[2].quantity").value(15))
				.andExpect(jsonPath("$.ingredients[3].quantity").value(20));

		//The stub above would still be in place if the controller never called the
		//service, so check that it actually did.
		verify(inventoryService).getInventory();
	}

	/**
	 * Tests that PUT /api/inventory turns the JSON request body into an InventoryDto
	 * and passes it to the service.  Capturing the argument is the only way to see
	 * what the controller sent onward; the response alone cannot tell us, because
	 * the mock decides what comes back regardless of what it was given.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	public void testUpdateInventoryPassesRequestBodyToService() throws Exception {
		when(inventoryService.updateInventory(any(InventoryDto.class)))
				.thenReturn(new InventoryDto(1L, java.util.List.of(
						new Ingredient("Coffee", 5),
						new Ingredient("Milk", 10),
						new Ingredient("Sugar", 15),
						new Ingredient("Chocolate", 20))));

		mvc.perform(put("/api/inventory")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(new InventoryDto(null, java.util.List.of(
						new Ingredient("Coffee", 5),
						new Ingredient("Milk", 10),
						new Ingredient("Sugar", 15),
						new Ingredient("Chocolate", 20)))))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients[0].quantity").value(5));

		ArgumentCaptor<InventoryDto> sentToService = ArgumentCaptor.forClass(InventoryDto.class);
		verify(inventoryService).updateInventory(sentToService.capture());

		InventoryDto captured = sentToService.getValue();
		assertAll("InventoryDto the controller built from the request body",
				() -> assertEquals(5, captured.getIngredient("Coffee")),
				() -> assertEquals(10, captured.getIngredient("Milk")),
				() -> assertEquals(15, captured.getIngredient("Sugar")),
				() -> assertEquals(20, captured.getIngredient("Chocolate")));
	}

	/**
	 * Tests that PUT /api/inventory answers 404 when the service reports that there
	 * is no inventory to update.  Arranging that against a real database would mean
	 * emptying the table first; here the mock simply throws.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	public void testUpdateInventoryWhenInventoryIsMissing() throws Exception {
		when(inventoryService.updateInventory(any(InventoryDto.class)))
				.thenThrow(new ResourceNotFoundException("Inventory does not exist."));

		//404 rather than 500 because ResourceNotFoundException carries
		//@ResponseStatus(HttpStatus.NOT_FOUND).
		mvc.perform(put("/api/inventory")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(new InventoryDto(null, java.util.List.of(
						new Ingredient("Coffee", 5),
						new Ingredient("Milk", 10),
						new Ingredient("Sugar", 15),
						new Ingredient("Chocolate", 20)))))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
