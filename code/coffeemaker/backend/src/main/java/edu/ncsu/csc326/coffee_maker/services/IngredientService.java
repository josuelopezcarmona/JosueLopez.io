package edu.ncsu.csc326.coffee_maker.services;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;

/**
 * Interface defining the ingredient behaviors.
 *
 * @author CSC 326 Course Staff
 */
public interface IngredientService {

    /**
     * Creates the ingredient.
     * @param ingredientDto ingredient to create
     * @return updated ingredient after creation
     */
    IngredientDto createIngredient(IngredientDto ingredientDto);

    /**
     * Returns the single ingredient.
     * @return the single ingredient
     */
    IngredientDto getIngredient(IngredientDto ingredientDto);

    /**
     * Updates the contents of the ingredient.
     * @param ingredientDto values to update
     * @return updated ingredient
     */
    IngredientDto getQuantity(IngredientDto ingredientDto);

}
