package com.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ValidationResult} class.
 * Tests the behavior of the ValidationResult class to ensure it correctly handles different constructors and getter methods.
 */
public class ValidationResultTest {

    /**
     * Tests the constructor and getter methods of ValidationResult.
     * Verifies that the constructor initializes fields correctly and the getters return expected values.
     */
    @Test
    public void testConstructorAndGetters() {
        ValidationResult result = new ValidationResult(true, "No errors", 10, 100, "Clockwise");

        assertTrue(result.isValid());

        assertEquals("No errors", result.getErrorMessage());

        assertEquals(10, result.getLength());

        assertEquals(100, result.getSpeed());

        assertEquals("Clockwise", result.getDirection());
    }

    /**
     * Tests the constructor when a null error message is provided.
     * Verifies that the error message is set to an empty string when null is provided.
     */
    @Test
    public void testConstructorWithNullErrorMessage() {
        ValidationResult result = new ValidationResult(true, null, 10, 100, "Clockwise");

        assertEquals("", result.getErrorMessage());
    }

    /**
     * Tests the behavior of ValidationResult with invalid input values.
     * Verifies that the constructor correctly sets fields to represent an invalid result.
     */
    @Test
    public void testInvalidResult() {
        ValidationResult result = new ValidationResult(false, "Error occurred", 0, 0, "");

        assertFalse(result.isValid());

        assertEquals("Error occurred", result.getErrorMessage());

        assertEquals(0, result.getLength());

        assertEquals(0, result.getSpeed());

        assertEquals("", result.getDirection());
    }
}
