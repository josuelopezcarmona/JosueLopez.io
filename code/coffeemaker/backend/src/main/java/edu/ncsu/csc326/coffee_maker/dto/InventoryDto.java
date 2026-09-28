package edu.ncsu.csc326.coffee_maker.dto;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * Used to transfer Inventory data between the client and server.  
 * This class will serve as the response in the REST API.
 *
 * @author CSC 326 Course Staff
 */
public class InventoryDto {
	
	/** id for inventory entry */
    private Long    id;
    /** arbitrary ingredients in inventory */
    private List<Ingredient> ingredients;
    
    /** 
     * Default InventoryDto constructor.
     */
    public InventoryDto() {
        this.ingredients = new ArrayList<>();
    }
    
    /**
     * Constructs an inventory DTO with arbitrary ingredients.
     *
     * @param id inventory id
     * @param ingredients inventory ingredients
     */
    public InventoryDto(Long id, List<Ingredient> ingredients) {
        this.id = id;
        this.ingredients = ingredients == null ? new ArrayList<>() : ingredients;
    }

    /** @return arbitrary inventory ingredients */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /** @param ingredients arbitrary inventory ingredients */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients == null ? new ArrayList<>() : ingredients;
    }

    /** @param ingredient ingredient to add */
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    /**
     * Returns the quantity stored for an ingredient name.
     *
     * @param name ingredient name
     * @return quantity, or null when not present
     */
    public Integer getIngredient(String name) {
        for (Ingredient ingredient : ingredients) {
            if (name != null && name.equals(ingredient.getIngredient())) {
                return ingredient.getQuantity();
            }
        }
        return null;
    }

	/**
	 * Gets the inventory id.
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Inventory id to set.
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
