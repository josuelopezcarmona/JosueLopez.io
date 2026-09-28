package edu.ncsu.csc326.coffee_maker.services;

import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests RecipeServiceImpl.
 *
 * @author Cole Dail
 * @author Carlos Martinez Cabrera
 */
@SpringBootTest
@Transactional
class RecipeServiceTest {

    /** Reference to RecipeService (and RecipeServiceImpl). */
    @Autowired
    private RecipeService recipeService;

    /** Reference to EntityManager */
    @Autowired
    private EntityManager entityManager;

    /**
     * Sets up the test case.
     * @throws java.lang.Exception if error
     */
    @BeforeEach
    public void setUp() throws Exception {
        entityManager.createQuery("DELETE FROM Recipe").executeUpdate();
    }

    /**
     * make  a RecipeDto with the name and price and a single  ingredient.
     * @param name recipe name
     * @param price recipe price
     * @return the built RecipeDto
     */
    private RecipeDto recipeWithOneIngredient(String name, Integer price) {
        RecipeDto recipeDto = new RecipeDto(name, price);
        recipeDto.addIngredient(new Ingredient("Coffee", 2));
        return recipeDto;
    }

    @Test
    public void testCreateRecipe() {
        RecipeDto recipeDto = new RecipeDto("Coffee", 50);
        recipeDto.addIngredient(new Ingredient("Coffee", 2));
        recipeDto.addIngredient(new Ingredient("Milk", 1));

        recipeService.createRecipe(recipeDto);

        //Read the recipe back through the service rather than asserting on the object
        //createRecipe() returned, so the assertions check what was saved.
        entityManager.flush();
        entityManager.clear();

        RecipeDto createdRecipe = recipeService.getRecipeByName("Coffee");
        assertAll("Recipe contents",
                () -> assertEquals("Coffee", createdRecipe.getName()),
                () -> assertEquals(50, createdRecipe.getPrice()),
                () -> assertEquals(2, createdRecipe.getIngredients().size()));
    }

    @Test
    void testGetAllRecipes() {
        RecipeDto recipeDto1 = recipeWithOneIngredient("Coffee", 50);
        RecipeDto recipeDto2 = recipeWithOneIngredient("Latte", 75);

        recipeService.createRecipe(recipeDto1);
        recipeService.createRecipe(recipeDto2);

        entityManager.flush();
        entityManager.clear();

        List<RecipeDto> recipes = recipeService.getAllRecipes();

        assertEquals(2, recipes.size());
        assertTrue(recipes.stream().anyMatch(r -> "Coffee".equals(r.getName())));
        assertTrue(recipes.stream().anyMatch(r -> "Latte".equals(r.getName())));
    }

    @Test
    void testIsDuplicateName() {
        recipeService.createRecipe(recipeWithOneIngredient("Coffee", 50));

        entityManager.flush();
        entityManager.clear();

        assertTrue(recipeService.isDuplicateName("Coffee"));
        assertFalse(recipeService.isDuplicateName("Latte"));
    }

    @Test
    void testUpdateRecipe() {
        RecipeDto recipeDto = recipeWithOneIngredient("Coffee", 50);
        recipeService.createRecipe(recipeDto);

        entityManager.flush();
        entityManager.clear();

        long recipeId = recipeService.getRecipeByName("Coffee").getId();

        RecipeDto update = new RecipeDto("Espresso", 40);
        update.addIngredient(new Ingredient("Coffee", 3));
        recipeService.updateRecipe(recipeId, update);

        entityManager.flush();
        entityManager.clear();

        RecipeDto newRecipe = recipeService.getRecipeById(recipeId);

        assertAll("Recipe contents",
                () -> assertEquals("Espresso", newRecipe.getName()),
                () -> assertEquals(40, newRecipe.getPrice()),
                () -> assertEquals(1, newRecipe.getIngredients().size()));
    }

    @Test
    void testDeleteRecipe() {
        RecipeDto recipeDto = recipeWithOneIngredient("Coffee", 50);
        recipeService.createRecipe(recipeDto);

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, recipeService.getAllRecipes().size());
        assertEquals("Coffee", recipeService.getAllRecipes().getFirst().getName());

        recipeService.deleteRecipe(recipeService.getRecipeByName("Coffee").getId());

        entityManager.flush();
        entityManager.clear();

        assertEquals(0, recipeService.getAllRecipes().size());
    }
}