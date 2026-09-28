package edu.ncsu.csc326.coffee_maker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;

/**
 * Controller for CoffeeMaker's inventory.  The inventory is a singleton; there's
 * only one row in the database that contains the current inventory for the system.
 *
 * @author CSC 326 Course Staff
 * @author Josue Lopez-Carmona
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	/**
	 * Connection to inventory service for manipulating the Inventory model.
	 */
	@Autowired
	private InventoryService inventoryService;
	
	 /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Inventory. 
     *
     * @return response to the request
     */
	@GetMapping
	public ResponseEntity<InventoryDto> getInventory() {
		InventoryDto inventoryDto = inventoryService.getInventory();
		return ResponseEntity.ok(inventoryDto);
	}
	
	/**
	 * REST API endpoint to provide update access to the CoffeeMaker's singleton
	 * Inventory.
     * @param inventoryDto
     *            amounts to add to inventory
     * @return response to the request
	 */
	@PutMapping
	public ResponseEntity<InventoryDto> updateInventory(@RequestBody InventoryDto inventoryDto) {
		for (Ingredient ingredient : inventoryDto.getIngredients()) {
			if (ingredient.getQuantity() == null || ingredient.getQuantity() <= 0) {
				return new ResponseEntity<>(inventoryDto, HttpStatus.BAD_REQUEST);
			}
		}

		InventoryDto savedInventoryDto = inventoryService.updateInventory(inventoryDto);
		return ResponseEntity.ok(savedInventoryDto);
	}

}
