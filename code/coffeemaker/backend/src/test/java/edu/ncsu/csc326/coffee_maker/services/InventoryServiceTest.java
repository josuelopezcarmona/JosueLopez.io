package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests InventoryServiceImpl.
 *
 * @author CSC 326 Course Staff
 */
@SpringBootTest
@Transactional
public class InventoryServiceTest {
	
	/** Reference to InventoryService (and InventoryServiceImpl). */
	@Autowired
	private InventoryService inventoryService;
	
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
	 * Tests InventoryService.createInventory().
	 */
	@Test
	public void testCreateInventory() {
		InventoryDto inventoryDto = new InventoryDto();
		inventoryDto.addIngredient(new Ingredient("Coffee", 5));
		inventoryDto.addIngredient(new Ingredient("Milk", 9));
		inventoryDto.addIngredient(new Ingredient("Sugar", 14));
		inventoryDto.addIngredient(new Ingredient("Chocolate", 23));
		
		inventoryService.createInventory(inventoryDto);
		
		//Read the inventory back through the service rather than asserting on the
		//object createInventory() returned, so the assertions check what was saved.
		entityManager.flush();
		entityManager.clear();
		
		InventoryDto createdInventoryDto = inventoryService.getInventory();
		assertAll("InventoryDto contents",
				() -> assertEquals(5, createdInventoryDto.getIngredient("Coffee")),
				() -> assertEquals(9, createdInventoryDto.getIngredient("Milk")),
				() -> assertEquals(14, createdInventoryDto.getIngredient("Sugar")),
				() -> assertEquals(23, createdInventoryDto.getIngredient("Chocolate")));
	}
	
	/**
	 * Tests InventoryService.updateInventory()
	 */
	@Test
	public void testUpdateInventory() {
		InventoryDto inventoryDto = inventoryService.getInventory();

		inventoryDto.setIngredients(new ArrayList<>(List.of(
				new Ingredient("Coffee", 35),
				new Ingredient("Milk", 17),
				new Ingredient("Sugar", 12),
				new Ingredient("Chocolate", 14)
		)));
		
		inventoryService.updateInventory(inventoryDto);
		
		//Asserting on the object that updateInventory() returns would only re-check
		//the fields we just set.  Flush the change, empty the persistence context,
		//and read the inventory back so the assertions test what was persisted.
		entityManager.flush();
		entityManager.clear();
		
		InventoryDto updatedInventoryDto = inventoryService.getInventory();
		assertAll("InventoryDto contents",
				() -> assertEquals(35, updatedInventoryDto.getIngredient("Coffee")),
				() -> assertEquals(17, updatedInventoryDto.getIngredient("Milk")),
				() -> assertEquals(12, updatedInventoryDto.getIngredient("Sugar")),
				() -> assertEquals(14, updatedInventoryDto.getIngredient("Chocolate")));
	}
}
