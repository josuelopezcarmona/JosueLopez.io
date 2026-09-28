package edu.ncsu.csc326.coffee_maker.services;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;

/**
 * Interface defining the inventory behaviors.
 *
 * @author CSC 326 Course Staff
 */
public interface InventoryService {
	
	/**
	 * Creates the inventory.
	 * @param inventoryDto inventory to create
	 * @return updated inventory after creation
	 */
	InventoryDto createInventory(InventoryDto inventoryDto);
	
	/**
	 * Returns the single inventory.
	 * @return the single inventory
	 */
	InventoryDto getInventory();
	
	/**
	 * Updates the contents of the inventory.
	 * @param inventoryDto values to update
	 * @return updated inventory
	 */
	InventoryDto updateInventory(InventoryDto inventoryDto);

}
