package edu.ncsu.csc326.coffee_maker.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Recipe for the coffee maker. Recipe is a Data Access Object (DAO) is tied to the database using
 * Hibernate libraries. RecipeRepository provides the methods for database CRUD operations.
 *
 * @author CSC 326 Course Staff
 */
@Entity
@Table(name = "recipes")
public class Recipe {

    /** Recipe id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;

    /** Recipe name */
    private String name;

    /** Recipe price */
    private Integer price;

    /** Ingredients that make up this recipe */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<>();
    }

    /**
     * Creates a recipe from all the fields
     *
     * @param id recipe id
     * @param name recipe name
     * @param price recipe price
     */
    public Recipe(Long id, String name, Integer price) {
    	this.id = id;
    	this.name = name;
    	this.price = price;
    	this.ingredients = new ArrayList<>();
    }

    /**
     * Creates a recipe from all the fields
     * @param name recipe name
     * @param price recipe price
     */
    public Recipe(String name, Integer price) {
    	this.name = name;
    	this.price = price;
    	this.ingredients = new ArrayList<>();
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }


    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Returns the ingredients that make up this recipe.
     *
     * @return the ingredients
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Adds an ingredient to this recipe.
     *
     * @param ingredient
     *            the ingredient to add
     */
    public void addIngredient ( final Ingredient ingredient ) {
        this.ingredients.add( ingredient );
    }

    /**
     * Replaces this recipe's ingredients with the given list.
     *
     * @param ingredients
     *            the ingredients to set
     */
    public void setIngredients ( final List<Ingredient> ingredients ) {
        this.ingredients = ingredients;
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        return name;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
