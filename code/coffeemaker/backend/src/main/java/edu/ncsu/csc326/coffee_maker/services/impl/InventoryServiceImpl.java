
package edu.ncsu.csc326.coffee_maker.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.mapper.InventoryMapper;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;

/**
 * Implementation of the InventoryService interface.
 *
 * @author CSC 326 Course Staff
 */
@Service
public class InventoryServiceImpl implements InventoryService {
	
	/** Connection to the repository to work with the DAO + database */
	@Autowired
	private InventoryRepository inventoryRepository;

	/**
	 * Creates the inventory.
	 * @param inventoryDto inventory to create
	 * @return updated inventory after creation
	 */
	@Override
	public InventoryDto createInventory(InventoryDto inventoryDto) {
		Inventory inventory = InventoryMapper.mapToInventory(inventoryDto);
		Inventory savedInventory = inventoryRepository.save(inventory);
		return InventoryMapper.mapToInventoryDto(savedInventory);
	}

	/**
	 * Returns the single Inventory row, if one has been created.  Inventory is
	 * treated as a singleton, so it is located by reading the only row rather than
	 * by a hard-coded id.  Assuming a particular id would tie this method's
	 * correctness to the database's AUTO_INCREMENT counter, which keeps climbing
	 * after deletes and is not reset when a transaction rolls back.
	 * @return the single Inventory, or an empty Optional if none exists yet
	 */
	private Optional<Inventory> findSingleInventory() {
		return inventoryRepository.findAll().stream().findFirst();
	}

	/**
	 * Returns the single inventory.
	 * @return the single inventory
	 */
	@Override
	public InventoryDto getInventory() {
		Optional<Inventory> inventory = findSingleInventory();
		if (inventory.isEmpty()) {
			InventoryDto savedInventoryDto = createInventory(new InventoryDto());
			return savedInventoryDto;
		}
		return InventoryMapper.mapToInventoryDto(inventory.get());
	}

	/**
	 * Updates the contents of the inventory.
	 * @param inventoryDto values to update
	 * @return updated inventory
	 * @throws ResourceNotFoundException if no inventory has been created yet
	 */
	@Override
	public InventoryDto updateInventory(InventoryDto inventoryDto) {
		Inventory inventory = findSingleInventory().orElseThrow(
				() -> new ResourceNotFoundException("Inventory does not exist.")
		);

		inventory.setIngredients(inventoryDto.getIngredients());
		
		Inventory savedInventory = inventoryRepository.save(inventory);
		
		return InventoryMapper.mapToInventoryDto(savedInventory);
	}


}
