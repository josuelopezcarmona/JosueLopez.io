package edu.ncsu.csc326.coffee_maker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory for the coffee maker. Inventory is a Data Access Object (DAO) is tied to the database using
 * Hibernate libraries. InventoryRepository provides the methods for database CRUD operations.
 *
 * @author CSC 326 Course Staff
 */
@Entity
public class Inventory {
	
	/** id for inventory entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    /** list of ingredients */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ingredient> ingredients;
    
    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        this.ingredients = new ArrayList<>();
    }
    
    /**
     * Creates an Inventory with all fields
     * @param id inventory's id
     * @param ingredients inventory's list of ingredients
     */
    public Inventory(Long id, List<Ingredient> ingredients) {
		this.id = id;
		this.ingredients = ingredients == null ? new ArrayList<>() : ingredients;
	}
    
    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the quantity of the given ingredient in the inventory.
     *
     * @return int
     */
    public Integer getIngredientQuantity (Ingredient ingredient) {
        return ingredient == null ? null : getIngredient(ingredient.getIngredient());
    }

    /**
     * Sets the number of sugar units in the inventory to the specified amount.
     *
     * @param ingredient
     *            type of Ingredient to set
     * @param quantity
     *           quantity of Ingredient to set
     */
    public void setIngredientQuantity ( String ingredient, final Integer quantity ) {
        if (ingredient == null || quantity == null || quantity < 0) {
            return;
        }
        Ingredient storedIngredient = findIngredient(ingredient);
        if (storedIngredient == null) {
            ingredients.add(new Ingredient(ingredient, quantity));
        } else {
            storedIngredient.setQuantity(quantity);
        }
    }

    /**
     * Returns the quantity stored for an ingredient name.
     *
     * @param ingredient name of the ingredient
     * @return quantity, or null when the ingredient is not in the inventory
     */
    public Integer getIngredient (String ingredient) {
        Ingredient storedIngredient = findIngredient(ingredient);
        return storedIngredient == null ? null : storedIngredient.getQuantity();
    }

    /** @return all ingredients in the inventory */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /** @param ingredients ingredients to store */
    public void setIngredients (List<Ingredient> ingredients) {
        this.ingredients = ingredients == null ? new ArrayList<>() : ingredients;
    }

    /** @param ingredient ingredient to add */
    public void addIngredient (Ingredient ingredient) {
        if (ingredient != null) {
            ingredients.add(ingredient);
        }
    }

    private Ingredient findIngredient (String ingredient) {
        if (ingredient == null) {
            return null;
        }
        for (Ingredient storedIngredient : ingredients) {
            if (ingredient.equals(storedIngredient.getIngredient())) {
                return storedIngredient;
            }
        }
        return null;
    }

}
