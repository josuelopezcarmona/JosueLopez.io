package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;

/**
 * Tests Recipe repository.  Uses the real database - not an embedded one.
 *
 * @author CSC 326 Course Staff
 * @author Josue Lopez-Carmona
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RecipeRepositoryTest {

	/** Reference to recipe repository */
	@Autowired
	private RecipeRepository recipeRepository;

	/** Reference to EntityManager */
	@Autowired
	private TestEntityManager testEntityManager;

	/** Coffee recipe */
	private Recipe recipe1;
	/** Latte recipe */
	private Recipe recipe2;

	/**
	 * Sets up the test case.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		recipeRepository.deleteAll();

		recipe1 = new Recipe("Coffee", 50);
		recipe1.addIngredient(new Ingredient("Coffee", 3));

		recipe2 = new Recipe("Latte", 100);
		recipe2.addIngredient(new Ingredient("Coffee", 3));
		recipe2.addIngredient(new Ingredient("Milk", 2));

		recipeRepository.save(recipe1);
		recipeRepository.save(recipe2);

		//Push the inserts to the database and empty the persistence context.  Without
		//this, a test below could be handed the same in-memory object that save()
		//just returned and would pass even if nothing reached the database.
		testEntityManager.flush();
		testEntityManager.clear();
	}

	/**
	 * Tests RecipeRepository.findByName().  Use this test as the model for the
	 * repository tests you write for the remaining operations.
	 */
	@Test
	public void testFindRecipeByName() {
		Recipe fetchedRecipe = recipeRepository.findByName("Latte").orElseThrow();

		assertAll("Recipe contents",
				() -> assertEquals("Latte", fetchedRecipe.getName()),
				() -> assertEquals(100, fetchedRecipe.getPrice()),
				() -> assertEquals(2, fetchedRecipe.getIngredients().size()));
	}

	/**
	 * Tests retrieving every recipe from the repository.
	 */
	@Test
	public void testGetAllRecipes() {
		List<Recipe> recipes = recipeRepository.findAll();

		assertEquals(2, recipes.size());
		assertTrue(recipes.stream().anyMatch(r -> "Coffee".equals(r.getName())));
		assertTrue(recipes.stream().anyMatch(r -> "Latte".equals(r.getName())));
	}

	/**
	 * Tests updating a recipe already in the repository.
	 */
	@Test
	public void testUpdateRecipe() {
		Recipe fetchedRecipe = recipeRepository.findByName("Coffee").orElseThrow();
		fetchedRecipe.setPrice(75);
		recipeRepository.save(fetchedRecipe);

		testEntityManager.flush();
		testEntityManager.clear();

		Recipe updatedRecipe = recipeRepository.findByName("Coffee").orElseThrow();
		assertEquals(75, updatedRecipe.getPrice());
	}

	/**
	 * Tests deleting a recipe from the repository.
	 */
	@Test
	public void testDeleteRecipe() {
		Recipe fetchedRecipe = recipeRepository.findByName("Latte").orElseThrow();
		recipeRepository.delete(fetchedRecipe);

		testEntityManager.flush();
		testEntityManager.clear();

		assertTrue(recipeRepository.findByName("Latte").isEmpty());
	}

	/**
	 * Tests tthat a recipe is saved
	 */
	@Test
	public void testAddIngredients() {
		Recipe daRecipe = new Recipe("Mocha", 500);
		daRecipe.addIngredient(new Ingredient("Coffee", 3));
		daRecipe.addIngredient(new Ingredient("Pumpkin Spice", 2));
		daRecipe.addIngredient(new Ingredient("Milk", 1));

		Recipe savedRecipe = recipeRepository.save(daRecipe);
		testEntityManager.flush();
		testEntityManager.clear();

		Recipe retrievedRecipe = recipeRepository.findById(savedRecipe.getId()).orElseThrow();
		assertAll("Recipe contents",
				() -> assertEquals("Mocha", retrievedRecipe.getName()),
				() -> assertEquals(500, retrievedRecipe.getPrice()),
				() -> assertEquals(3, retrievedRecipe.getIngredients().size()));

		Ingredient i1 = retrievedRecipe.getIngredients().get(0);
		Ingredient i2 = retrievedRecipe.getIngredients().get(1);
		Ingredient i3 = retrievedRecipe.getIngredients().get(2);

		assertAll("Ingredient contents",
				() -> assertEquals("Coffee", i1.getIngredient()),
				() -> assertEquals(3, i1.getQuantity()));

		assertAll("Ingredient contents",
				() -> assertEquals("Pumpkin Spice", i2.getIngredient()),
				() -> assertEquals(2, i2.getQuantity()));

		assertAll("Ingredient contents",
				() -> assertEquals("Milk", i3.getIngredient()),
				() -> assertEquals(1, i3.getQuantity()));
	}

}
