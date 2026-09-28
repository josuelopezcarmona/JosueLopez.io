package edu.ncsu.csc326.coffee_maker.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.mapper.IngredientMapper;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;

/**
 * Implementation of the ingredient service.
 */
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final InventoryRepository inventoryRepository;

    /**
     * Creates the ingredient service.
     *
     * @param ingredientRepository ingredient repository
     */
    public IngredientServiceImpl(IngredientRepository ingredientRepository,
            InventoryRepository inventoryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Creates an ingredient after validating its name and starting quantity.
     *
     * @param ingredientDto ingredient to create
     * @return saved ingredient
     */
    @Override
    @Transactional
    public IngredientDto createIngredient(IngredientDto ingredientDto) {
        validate(ingredientDto);
        if (existsByName(ingredientDto.getIngredient())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ingredient '" + ingredientDto.getIngredient() + "' already exists.");
        }
        Ingredient saved = ingredientRepository.save(
                IngredientMapper.mapToIngredient(ingredientDto));
        Inventory inventory = inventoryRepository.findAll().stream().findFirst()
                .orElseGet(Inventory::new);
        inventory.addIngredient(saved);
        inventoryRepository.save(inventory);
        return IngredientMapper.mapToIngredientDto(saved);
    }

    /**
     * Finds an ingredient by name, ignoring case.
     *
     * @param ingredientDto DTO containing the requested name
     * @return matching ingredient
     */
    @Override
    public IngredientDto getIngredient(IngredientDto ingredientDto) {
        return findByName(ingredientDto.getIngredient());
    }

    /**
     * Finds an ingredient for the quantity endpoint.
     *
     * @param ingredientDto DTO containing the requested name
     * @return matching ingredient
     */
    @Override
    public IngredientDto getQuantity(IngredientDto ingredientDto) {
        return findByName(ingredientDto.getIngredient());
    }

    private boolean existsByName(String name) {
        return ingredientRepository.findAll().stream()
                .anyMatch(ingredient -> ingredient.getIngredient() != null
                        && ingredient.getIngredient().equalsIgnoreCase(name));
    }

    private IngredientDto findByName(String name) {
        return ingredientRepository.findAll().stream()
                .filter(ingredient -> ingredient.getIngredient() != null
                        && ingredient.getIngredient().equalsIgnoreCase(name))
                .findFirst()
                .map(IngredientMapper::mapToIngredientDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ingredient does not exist with name " + name));
    }

    private void validate(IngredientDto ingredientDto) {
        if (ingredientDto == null || ingredientDto.getIngredient() == null
                || ingredientDto.getIngredient().isBlank()
                || ingredientDto.getQuantity() == null
                || ingredientDto.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ingredient name and a positive quantity are required.");
        }
    }
}
