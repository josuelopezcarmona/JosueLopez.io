package edu.ncsu.csc326.coffee_maker.dto;

/**
 * Used to transfer Ingredient data between the client and server.
 * This class will serve as the response in the REST API.
 *
 * @author CSC 326 Course Staff
 */
public class IngredientDto {

    /** id for ingredient entry */
    private Long    id;

    /** name of ingredient */
    private String ingredient;

    /** quantity of ingredient */
    private Integer quantity;

    /**
     * Default IngredientDto constructor.
     */
    public IngredientDto() {
        this.ingredient = null;
        this.quantity = null;
    }

    /**
     * Constructs an ingredient DTO with arbitrary ingredients.
     *
     * @param id ingredient id
     * @param ingredient ingredient name
     * @param quantity ingredient quantity
     */
    public IngredientDto(Long id, String ingredient, Integer quantity) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    /**
     * Compatibility constructor for callers that still provide quantity text.
     *
     * @param id ingredient id
     * @param ingredient ingredient name
     * @param quantity ingredient quantity text
     */
    public IngredientDto(Long id, String ingredient, String quantity) {
        this(id, ingredient, quantity == null ? null : Integer.valueOf(quantity));
    }

    /** @return the ingredient */
    public String getIngredient() {
        return ingredient;
    }

    /** @param ingredient the ingredient to set */
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    /** @return the quantity */
    public Integer getQuantity() {
        return quantity;
    }

    /** @param quantity the quantity to set */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the ingredient id.
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Ingredient id to set.
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

}
