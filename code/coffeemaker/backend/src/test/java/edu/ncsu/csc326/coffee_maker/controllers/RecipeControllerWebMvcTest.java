package edu.ncsu.csc326.coffee_maker.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.services.RecipeService;

/**
 * Tests RecipeController
 *
 * The following is according to the instructions from the website:
 *
 * For the 507 INSUFFICIENT_STORAGE case, write a RecipeControllerWebMvcTest
 * using the isolation style above. Mocking RecipeService lets you produce that
 * condition in one line instead of creating recipes until the system fills up.
 *
 * @author Carlos Martinez Cabrera
 */
@WebMvcTest(RecipeController.class)
public class RecipeControllerWebMvcTest {

	/** this will be the mock mvc that we will use for testing */
	@Autowired
	private MockMvc mvc;

	/** fake RecipeService we control instead of using the real one */
	@MockitoBean
	private RecipeService recipeService;

	/**
	 * make a RecipeDto with the name and or price and a single ingredient
	 * @param name recipe name
	 * @param price recipe price
	 * @return the built RecipeDto
	 */
	private RecipeDto recipeWithOneIngredient(String name, Integer price) {
		RecipeDto recipeDto = new RecipeDto(name, price);
		recipeDto.addIngredient(new Ingredient("Coffee", 4));
		return recipeDto;
	}

	/**
	 * Tests POST /api/recipes and 507 INSUFFICIENT_STORAGE
	 * This is when it already has the max amount of recipes
	 *
	 * @throws Exception if any sort of problems during running
	 */
	@Test
	public void testMakeAnotherRecipeButFull() throws Exception {
		RecipeDto daRecipe = recipeWithOneIngredient("Cappuccino", 150);

		when(recipeService.isDuplicateName(anyString())).thenReturn(false);
		when(recipeService.getAllRecipes()).thenReturn(List.of(
				recipeWithOneIngredient("Coffee", 67),
				recipeWithOneIngredient("Vanilla Iced Latte", 150),
				recipeWithOneIngredient("Hersheys", 300)));

		mvc.perform(post("/api/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(daRecipe)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isInsufficientStorage());
		verify(recipeService).isDuplicateName("Cappuccino");
		verify(recipeService).getAllRecipes();
	}
}
