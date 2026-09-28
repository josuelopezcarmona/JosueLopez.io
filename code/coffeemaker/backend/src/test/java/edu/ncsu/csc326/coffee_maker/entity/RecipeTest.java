package edu.ncsu.csc326.coffee_maker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests Recipe's identity-based equals/hashCode/toString, which are keyed off name.
 *
 * @author Carlos Martinez Cabrera
 */
class RecipeTest {

    @Test
    void testEqualsSameName() {
        Recipe recipe1 = new Recipe("Coffee", 50);
        Recipe recipe2 = new Recipe("Coffee", 75);

        assertEquals(recipe1, recipe2);
        assertEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testEqualsDifferentName() {
        Recipe recipe1 = new Recipe("Coffee", 50);
        Recipe recipe2 = new Recipe("Latte", 50);

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEqualsSelf() {
        Recipe recipe = new Recipe("Coffee", 50);
        assertEquals(recipe, recipe);
    }

    @Test
    void testEqualsNullAndOtherType() {
        Recipe recipe = new Recipe("Coffee", 50);

        assertFalse(recipe.equals(null));
        assertFalse(recipe.equals("Coffee"));
    }

    @Test
    void testEqualsNullName() {
        Recipe recipe1 = new Recipe("Coffee", 50);
        recipe1.setName(null);
        Recipe recipe2 = new Recipe("Coffee", 50);
        recipe2.setName(null);

        assertEquals(recipe1, recipe2);

        Recipe recipe3 = new Recipe("Coffee", 50);
        assertNotEquals(recipe1, recipe3);
    }

    @Test
    void testToString() {
        Recipe recipe = new Recipe("Coffee", 50);
        assertEquals("Coffee", recipe.toString());
    }

}
