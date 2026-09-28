package edu.ncsu.csc326.coffee_maker.mapper;

import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;

/**
 * Converts between RecipeDto and Recipe entity
 *
 * @author CSC 326 Course Staff
 */
public class RecipeMapper {

	/**
	 * Converts a Recipe entity to RecipeDto
	 * @param recipe Recipe to convert
	 * @return RecipeDto object
	 */
	public static RecipeDto mapToRecipeDto(Recipe recipe) {
		RecipeDto r = new RecipeDto (
				recipe.getId(),
				recipe.getName(),
				recipe.getPrice()
		);
		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			r.addIngredient(recipe.getIngredients().get(i));
		}
		return r;
	}

	/**
	 * Converts a RecipeDto object to a Recipe entity.
	 * @param recipeDto RecipeDto to convert
	 * @return Recipe entity
	 */
	public static Recipe mapToRecipe(RecipeDto recipeDto) {
		Recipe rec = new Recipe (
				recipeDto.getId(),
				recipeDto.getName(),
				recipeDto.getPrice()
		);
		for (int i = 0; i < recipeDto.getIngredients().size(); i++) {
			rec.addIngredient(recipeDto.getIngredients().get(i));
		}
		return rec;
	}

}
