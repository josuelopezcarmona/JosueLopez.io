package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * Tests InventoryRepository.  Uses the real database - not an embedded one.
 *
 * @author CSC 326 Course Staff
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class InventoryRepositoryTest {
	
	/** Reference to inventory repository */
	@Autowired
	private InventoryRepository inventoryRepository;
	
	/** Reference to EntityManager */
	@Autowired
	private TestEntityManager testEntityManager;
	
	/** Reference to inventory */
	private Inventory inventory;

	/**
	 * Sets up the test case.  Inventory is treated as a singleton, so the table is
	 * emptied before each test.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		inventoryRepository.deleteAll();
		
		inventory = new Inventory();
		inventory.addIngredient(new Ingredient("Coffee", 20));
		inventory.addIngredient(new Ingredient("Milk", 14));
		inventory.addIngredient(new Ingredient("Sugar", 32));
		inventory.addIngredient(new Ingredient("Chocolate", 10));
		inventoryRepository.save(inventory);
		
		//Push the insert to the database and empty the persistence context.  Without
		//this, the tests below could be handed the same in-memory object that save()
		//just returned and would pass even if nothing reached the database.
		testEntityManager.flush();
		testEntityManager.clear();
	}


	/**
	 * Test saving the inventory and retrieving from the repository.
	 */
	@Test
	public void testSaveAndGetInventory() {
		Inventory fetchedInventory = inventoryRepository.findById(inventory.getId()).orElseThrow();
		assertAll("Inventory contents",
				() -> assertEquals(20, fetchedInventory.getIngredient("Coffee")),
				() -> assertEquals(14, fetchedInventory.getIngredient("Milk")),
				() -> assertEquals(32, fetchedInventory.getIngredient("Sugar")),
				() -> assertEquals(10, fetchedInventory.getIngredient("Chocolate")));
	}
	
	/**
	 * Tests updating the inventory
	 */
	@Test
	public void testUpdateInventory() {
		Inventory fetchedInventory = inventoryRepository.findById(inventory.getId()).orElseThrow();
		fetchedInventory.setIngredientQuantity("Coffee", 13);
		fetchedInventory.setIngredientQuantity("Milk", 14);
		fetchedInventory.setIngredientQuantity("Sugar", 27);
		fetchedInventory.setIngredientQuantity("Chocolate", 23);
		
		inventoryRepository.save(fetchedInventory);
		
		//Asserting on the object that save() returns would only re-check the fields
		//we just set on an in-memory object.  Flush the change, empty the persistence
		//context, and read the row back so the assertions test what was persisted.
		testEntityManager.flush();
		testEntityManager.clear();
		
		Inventory updatedInventory = inventoryRepository.findById(inventory.getId()).orElseThrow();
		assertAll("Inventory contents",
				() -> assertEquals(13, updatedInventory.getIngredient("Coffee")),
				() -> assertEquals(14, updatedInventory.getIngredient("Milk")),
				() -> assertEquals(27, updatedInventory.getIngredient("Sugar")),
				() -> assertEquals(23, updatedInventory.getIngredient("Chocolate")));
	}
}
