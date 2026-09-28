package edu.ncsu.csc326.coffee_maker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Ingredient for the coffee maker
 *
 * @author Carlos Martinez Cabrera
 */
@Entity
public class Ingredient {

    /**  id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**  name of ingredient */
    private String ingredient;

    /** quantity of ingredient */
    private Integer quantity;

    /**
     * default ingredient
     */
    public Ingredient() {
    }

    /**
     * Creates an ingredient with the specified name and quantity.
     *
     * @param ingredient name of the ingredient
     * @param quantity quantity of the ingredient
     */
    public Ingredient(String ingredient, Integer quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    /**
     * id of the Ingredient
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * set the id
     *
     * @param id the ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * return name of ingredient
     *
     * @return the name of ingredient
     */
    public String getIngredient() {
        return ingredient;
    }

    /**
     * set the ingredient
     *
     * @param ingredient the ingredient to set
     */
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    /**
     * return the num of the ingredient
     *
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets ingredient quantity
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
