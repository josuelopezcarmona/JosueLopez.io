package edu.ncsu.csc326.coffee_maker.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.mapper.RecipeMapper;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;
import edu.ncsu.csc326.coffee_maker.services.RecipeService;

/**
 * Implementation of the RecipeService interface.
 *
 * @author CSC 326 Course Staff
 */
@Service
public class RecipeServiceImpl implements RecipeService {

	/** Connection to the repository to work with the DAO + database */
	@Autowired
	private RecipeRepository recipeRepository;

	/**
	 * Creates a recipe with the given information.
	 * @param recipeDto recipe to create
	 * @return created recipe
	 */
	@Override
	public RecipeDto createRecipe(RecipeDto recipeDto) {
		Recipe recipe = RecipeMapper.mapToRecipe(recipeDto);
		Recipe savedRecipe = recipeRepository.save(recipe);
		return RecipeMapper.mapToRecipeDto(savedRecipe);
	}

	/**
	 * Returns the recipe with the given id.
	 * @param recipeId recipe's id
	 * @return the recipe with the given id
	 * @throws ResourceNotFoundException if the recipe doesn't exist
	 */
	@Override
	public RecipeDto getRecipeById(Long recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
				() -> new ResourceNotFoundException("Recipe does not exist with id " + recipeId)
		);
		return RecipeMapper.mapToRecipeDto(recipe);
	}

	/**
	 * Returns the recipe with the given name
	 * @param recipeName recipe's name
	 * @return the recipe with the given name.
	 * @throws ResourceNotFoundException if the recipe doesn't exist
	 */
	@Override
	public RecipeDto getRecipeByName(String recipeName) {
		Recipe recipe = recipeRepository.findByName(recipeName).orElseThrow(
				() -> new ResourceNotFoundException("Recipe does not exist with name " + recipeName)
		);
		return RecipeMapper.mapToRecipeDto(recipe);
	}

	/**
	 * Returns true if the recipe already exists in the database.
	 * @param recipeName recipe's name to check
	 * @return true if already in the database
	 */
	@Override
	public boolean isDuplicateName(String recipeName) {
		try {
			getRecipeByName(recipeName);
			return true;
		} catch (ResourceNotFoundException e) {
			return false;
		}
	}

	/**
	 * Returns a list of all the recipes
	 * @return all the recipes
	 */
	@Override
	public List<RecipeDto> getAllRecipes() {
		List<Recipe> recipes = recipeRepository.findAll();
		return recipes.stream().map((recipe) -> RecipeMapper.mapToRecipeDto(recipe)).collect(Collectors.toList());
	}

	/**
	 * Updates the recipe with the given id with the recipe information
	 * @param recipeId id of recipe to update
	 * @param recipeDto values to update
	 * @return updated recipe
	 * @throws ResourceNotFoundException if the recipe doesn't exist
	 */
	@Override
	public RecipeDto updateRecipe(Long recipeId, RecipeDto recipeDto) {
		Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
				() -> new ResourceNotFoundException("Recipe does not exist with id " + recipeId)
		);

		recipe.setName(recipeDto.getName());
		recipe.setPrice(recipeDto.getPrice());
		recipe.setIngredients(recipeDto.getIngredients());

		Recipe savedRecipe = recipeRepository.save(recipe);

		return RecipeMapper.mapToRecipeDto(savedRecipe);
	}

	/**
	 * Deletes the recipe with the given id
	 * @param recipeId recipe's id
	 * @throws ResourceNotFoundException if the recipe doesn't exist
	 */
	@Override
	public void deleteRecipe(Long recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
				() -> new ResourceNotFoundException("Recipe does not exist with id " + recipeId)
		);

		recipeRepository.delete(recipe);
	}

}
