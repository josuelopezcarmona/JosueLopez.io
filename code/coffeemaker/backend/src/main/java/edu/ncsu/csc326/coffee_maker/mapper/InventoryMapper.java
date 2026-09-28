package edu.ncsu.csc326.coffee_maker.mapper;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;

/**
 * Converts between InventoryDto and Inventory entity.
 *
 * @author CSC 326 Course Staff
 */
public class InventoryMapper {

	/**
	 * Converts an Inventory entity to InventoryDto
	 * @param inventory Inventory to convert
	 * @return InventoryDto object
	 */
	public static InventoryDto mapToInventoryDto(Inventory inventory) {
		return new InventoryDto(inventory.getId(), inventory.getIngredients());
				
	}
	
	/**
	 * Converts an InventoryDto to an Inventory entity
	 * @param inventoryDto InventoryDto to convert
	 * @return Inventory entity
	 */
	public static Inventory mapToInventory(InventoryDto inventoryDto) {
		return new Inventory (
				inventoryDto.getId(),
				inventoryDto.getIngredients()
		);
				
	}
}
