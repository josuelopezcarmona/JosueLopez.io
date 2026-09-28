package edu.ncsu.csc326.coffee_maker.mapper;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * Converts between Ingredient entities and DTOs.
 */
public class IngredientMapper {

    private IngredientMapper() {
    }

    /**
     * Maps an ingredient entity to its DTO.
     *
     * @param ingredient ingredient entity
     * @return ingredient DTO
     */
    public static IngredientDto mapToIngredientDto(Ingredient ingredient) {
        return new IngredientDto(ingredient.getId(), ingredient.getIngredient(),
                ingredient.getQuantity());
    }

    /**
     * Maps an ingredient DTO to its entity.
     *
     * @param ingredientDto ingredient DTO
     * @return ingredient entity
     */
    public static Ingredient mapToIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient(ingredientDto.getIngredient(),
                ingredientDto.getQuantity());
        ingredient.setId(ingredientDto.getId());
        return ingredient;
    }
}
