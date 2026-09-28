package edu.ncsu.csc326.coffee_maker.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import jakarta.persistence.EntityManager;

/**
 * Tests the InventoryController REST API through MockMvc.
 *
 * @author CSC 326 Course Staff
 * @author Josue Lopez-Carmona
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InventoryControllerTest {
		
	/** Mock MVC for testing controller */
	@Autowired
	private MockMvc mvc;
	
	/** Reference to EntityManager */
	@Autowired
	private EntityManager entityManager;

	/**
	 * Sets up the test case.  Inventory is treated as a singleton, so the table is
	 * emptied before each test.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		entityManager.createQuery("DELETE FROM Inventory").executeUpdate();
	}

	/**
	 * Tests the GET /api/inventory endpoint.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	public void testGetInventory() throws Exception {
		//Assert on the individual fields. The id is assigned by the database, so a
		//test that pins it down would be asserting on the AUTO_INCREMENT counter
		//instead of on the behavior of the endpoint.
		mvc.perform(get("/api/inventory"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients").isEmpty());
	}

	/**
	 * Tests the PUT /api/inventory endpoint.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	public void testUpdateInventory() throws Exception {
		//The GET creates the single inventory row if it does not exist yet, which the
		//PUT below then updates.
		mvc.perform(get("/api/inventory"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients").isEmpty());
		
		InventoryDto updatedInventory = new InventoryDto(null, java.util.List.of(
				new Ingredient("Coffee", 5),
				new Ingredient("Milk", 10),
				new Ingredient("Sugar", 15),
				new Ingredient("Chocolate", 20)));
		
		mvc.perform(put("/api/inventory")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(updatedInventory))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients[0].quantity").value(5))
				.andExpect(jsonPath("$.ingredients[1].quantity").value(10))
				.andExpect(jsonPath("$.ingredients[2].quantity").value(15))
				.andExpect(jsonPath("$.ingredients[3].quantity").value(20));

	}

	/**
	 * Tests the PUT /api/inventory endpoint with an invalid ingredient quantity.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	public void testUpdateInventoryInvalidUnit() throws Exception {
		mvc.perform(get("/api/inventory"))
				.andExpect(status().isOk());

		InventoryDto updatedInventory = new InventoryDto(null, java.util.List.of(
				new Ingredient("Coffee", -5)));

		mvc.perform(put("/api/inventory")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(updatedInventory))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

}
