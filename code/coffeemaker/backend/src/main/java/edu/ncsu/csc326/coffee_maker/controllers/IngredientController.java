package edu.ncsu.csc326.coffee_maker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;

/**
 * REST endpoints for ingredients.
 */
@CrossOrigin("*")
@RestController
public class IngredientController {

    private final IngredientService ingredientService;

    /**
     * Creates the ingredient controller.
     *
     * @param ingredientService ingredient service
     */
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * Gets an ingredient by name.
     *
     * @param name ingredient name
     * @return ingredient
     */
    @GetMapping("/ingredient/{name}")
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable String name) {
        IngredientDto ingredient = ingredientService.getIngredient(
                new IngredientDto(null, name, (Integer) null));
        return ResponseEntity.ok(ingredient);
    }

    /**
     * Gets an ingredient's on-hand quantity.
     *
     * @param name ingredient name
     * @return quantity
     */
    @GetMapping("/ingredient/{name}/quantity")
    public ResponseEntity<Integer> getQuantity(@PathVariable String name) {
        IngredientDto ingredient = ingredientService.getQuantity(
                new IngredientDto(null, name, (Integer) null));
        return ResponseEntity.ok(ingredient.getQuantity());
    }

    /**
     * Adds an ingredient to the system.
     *
     * @param ingredientDto ingredient and starting quantity
     * @return created ingredient
     */
    @PostMapping("/api/inventory/ingredients")
    public ResponseEntity<IngredientDto> createIngredient(
            @RequestBody IngredientDto ingredientDto) {
        IngredientDto saved = ingredientService.createIngredient(ingredientDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
