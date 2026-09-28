package edu.ncsu.csc326.coffee_maker.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;
import edu.ncsu.csc326.coffee_maker.mapper.InventoryMapper;
import edu.ncsu.csc326.coffee_maker.mapper.RecipeMapper;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.services.MakeRecipeService;

/**
 * Implementation of the MakeRecipeService interface.
 *
 * Inventory ingredient lookup is name-based, so recipes can use any ingredient
 * represented in the inventory.
 *
 * @author CSC 326 Course Staff
 */
@Service
public class MakeRecipeServiceImpl implements MakeRecipeService {

	/** Connection to the repository to work with the DAO + database */
	@Autowired
	private InventoryRepository inventoryRepository;

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
	@Override
	public boolean makeRecipe(InventoryDto inventoryDto, RecipeDto recipeDto) {
		Inventory inventory = InventoryMapper.mapToInventory(inventoryDto);
		Recipe recipe = RecipeMapper.mapToRecipe(recipeDto);

		if ( enoughIngredients( inventory, recipe ) ) {
			for ( Ingredient ingredient : recipe.getIngredients() ) {
				decrementInventory( inventory, ingredient );
			}

            inventoryRepository.save(inventory);
            return true;
		}

        return false;
	}

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param inventory
     * 			  coffee maker inventory
     * @param recipe
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    private boolean enoughIngredients (Inventory inventory, Recipe recipe) {
        for ( Ingredient ingredient : recipe.getIngredients() ) {
            Integer available = inventoryAmountFor( inventory, ingredient );
            if ( available == null || available < ingredient.getQuantity() ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes one ingredient's amount from the inventory field it corresponds to.
     *
     * @param inventory
     *            coffee maker inventory to decrement
     * @param ingredient
     *            ingredient (and amount) to remove from the inventory
     */
    private void decrementInventory ( Inventory inventory, Ingredient ingredient ) {
        inventory.setIngredientQuantity(ingredient.getIngredient(),
                inventoryAmountFor(inventory, ingredient) - ingredient.getQuantity());
    }

    /**
     * Returns how much of the given ingredient's type is currently in the inventory,
     * or null if Inventory does not track that type yet.
     *
     * @param inventory
     *            coffee maker inventory
     * @param ingredient
     *            ingredient whose type to look up
     * @return the amount in inventory, or null if the type isn't tracked
     */
    private Integer inventoryAmountFor ( Inventory inventory, Ingredient ingredient ) {
        return inventory.getIngredient(ingredient.getIngredient());
    }

}
