package edu.ncsu.csc326.coffee_maker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RecipeControllerTest {
    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc mvc;

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
     * make a RecipeDto with the name and price and a ngredient.
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
    public void testGetRecipes() throws Exception {
        String recipe = mvc.perform(get("/api/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertFalse(recipe.contains("Mocha"));

        assertEquals("[]", recipe);
    }

    @Test
    public void testCreateRecipe() throws Exception {
        RecipeDto recipeDto = recipeWithOneIngredient("Mocha", 200);

        mvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(recipeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mocha"))
                .andExpect(jsonPath("$.price").value(200))
                .andExpect(jsonPath("$.ingredients.length()").value(1))
                .andExpect(jsonPath("$.ingredients[0].ingredient").value("Coffee"))
                .andExpect(jsonPath("$.ingredients[0].quantity").value(2));

        String recipe = mvc.perform(get("/api/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertTrue(recipe.contains("Mocha"));

        mvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(recipeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateRecipeTooMany() throws Exception {
        mvc.perform(post("/api/recipes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeWithOneIngredient("Coffee", 50)))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mvc.perform(post("/api/recipes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeWithOneIngredient("Latte", 75)))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mvc.perform(post("/api/recipes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeWithOneIngredient("Mocha", 100)))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        mvc.perform(post("/api/recipes").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(recipeWithOneIngredient("Espresso", 60)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInsufficientStorage());
    }

    @Test
    public void testCreateRecipeInvalidPrice() throws Exception {
        mvc.perform(post("/api/recipes").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(recipeWithOneIngredient("Coffee", -5)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateRecipeInvalidUnit() throws Exception {
        RecipeDto recipeDto = new RecipeDto("Coffee", 50);
        recipeDto.addIngredient(new Ingredient("Coffee", -1));

        mvc.perform(post("/api/recipes").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(recipeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateRecipeNoIngredients() throws Exception {
        RecipeDto recipeDto = new RecipeDto("Coffee", 50);

        mvc.perform(post("/api/recipes").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(recipeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateRecipe() throws Exception {
        RecipeDto recipeDto = recipeWithOneIngredient("Mocha", 200);

        String response = mvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(recipeDto))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        RecipeDto created = new ObjectMapper().readValue(response, RecipeDto.class);

        RecipeDto update = new RecipeDto("Espresso", 250);
        update.addIngredient(new Ingredient("Coffee", 3));

        mvc.perform(put("/api/recipes/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(update))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("Espresso"))
                        .andExpect(jsonPath("$.price").value(250))
                        .andExpect(jsonPath("$.ingredients.length()").value(1))
                        .andExpect(jsonPath("$.ingredients[0].ingredient").value("Coffee"))
                        .andExpect(jsonPath("$.ingredients[0].quantity").value(3));
    }

    @Test
    public void testUpdateRecipeNotFound() throws Exception {
        RecipeDto update = recipeWithOneIngredient("Espresso", 250);

        mvc.perform(put("/api/recipes/{id}", 12345L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(update))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateRecipeInvalidPrice() throws Exception {
        RecipeDto recipeDto = recipeWithOneIngredient("Mocha", 200);

        String response = mvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(recipeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        RecipeDto created = new ObjectMapper().readValue(response, RecipeDto.class);

        RecipeDto update = recipeWithOneIngredient("Mocha", -5);

        mvc.perform(put("/api/recipes/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(update))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateRecipeInvalidUnit() throws Exception {
        RecipeDto recipeDto = recipeWithOneIngredient("Mocha", 200);

        String response = mvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(recipeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        RecipeDto created = new ObjectMapper().readValue(response, RecipeDto.class);

        RecipeDto update = new RecipeDto("Mocha", 200);
        update.addIngredient(new Ingredient("Coffee", -1));

        mvc.perform(put("/api/recipes/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(update))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateRecipeNoIngredients() throws Exception {
        RecipeDto recipeDto = recipeWithOneIngredient("Mocha", 200);

        String response = mvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(recipeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        RecipeDto created = new ObjectMapper().readValue(response, RecipeDto.class);

        RecipeDto update = new RecipeDto("Mocha", 200);

        mvc.perform(put("/api/recipes/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(update))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetRecipe() throws Exception {
        mvc.perform(get("/api/recipes/[{name}", "dneRecipe"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        RecipeDto recipeDto = recipeWithOneIngredient("Mocha", 200);

        String response = mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RecipeDto recipe = new ObjectMapper().readValue(response, RecipeDto.class);

        mvc.perform(delete("/api/recipes/{id}", recipe.getId()));

        String recipes = mvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertFalse(recipes.contains("Mocha"));
    }
}
