package edu.ncsu.csc326.coffee_maker.dto;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * Used to transfer Recipe data between the client and server.
 * This class will serve as the response in the REST API.
 *
 * @author CSC 326 Course Staff
 */
public class RecipeDto {

	/** Recipe Id */
    private Long    id;

    /** Recipe name */
    private String  name;

    /** Recipe price */
    private Integer price;

    /** Ingredients that make up this recipe */
    private List<Ingredient> ingredients;

    /**
     * Default constructor for Recipe.
     */
    public RecipeDto() {
    	this.ingredients = new ArrayList<>();
    }

    /**
     * Creates recipe from field values.
     * @param id recipe's id
     * @param name recipe's name
     * @param price recipe's price
     */
	public RecipeDto(Long id, String name, Integer price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.ingredients = new ArrayList<>();
	}

	/**
     * Creates recipe from field values.
     * @param name recipe's name
     * @param price recipe's price
     */
	public RecipeDto(String name, Integer price) {
		super();
		this.name = name;
		this.price = price;
		this.ingredients = new ArrayList<>();
	}

	/**
	 * Gets the recipe id.
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Recipe id to set.
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets recipe's name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Recipe name to set.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the recipe's price
	 * @return the price
	 */
	public Integer getPrice() {
		return price;
	}

	/**
	 * Prices value to set.
	 * @param price the price to set
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}

	/**
	 * get the recipe
	 * @return the ingredients
	 */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * set the recipes
	 * @param ingredients the ingredients to set
	 */
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	/**
	 * Adds ingredient
	 * @param ingredient the ingredient to add
	 */
	public void addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
	}

}
