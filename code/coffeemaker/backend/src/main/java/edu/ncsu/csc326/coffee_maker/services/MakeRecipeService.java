package edu.ncsu.csc326.coffee_maker.services;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;

/**
 * Interface defining the make recipe behaviors.
 *
 * @author CSC 326 Course Staff
 */
public interface MakeRecipeService {
	
	/**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param inventoryDto
     * 			  current inventory
     * @param recipeDto
     *            recipe to make
     * @return updated inventory 
     */
	boolean makeRecipe(InventoryDto inventoryDto, RecipeDto recipeDto);

}
