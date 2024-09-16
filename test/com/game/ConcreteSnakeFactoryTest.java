package com.game;

import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ConcreteSnakeFactory} class.
 * These tests ensure that the factory creates snakes correctly
 * and handles different movement strategies and invalid input.
 */
public class ConcreteSnakeFactoryTest {

    private final ConcreteSnakeFactory factory = new ConcreteSnakeFactory();

    /**
     * Tests the creation of a snake with a clockwise movement strategy.
     * Verifies that the snake moves correctly in a clockwise direction.
     */
    @Test
    public void testCreateSnakeClockwise() {
        Snake snake = factory.createSnake(5, "Clockwise", 10, 10, 1L);

        testSnakeMovement(snake, 10, 10, 15, 10);
    }

    /**
     * Tests the creation of a snake with an anticlockwise movement strategy.
     * Verifies that the snake moves correctly in an anticlockwise direction.
     */
    @Test
    public void testCreateSnakeAnticlockwise() {
        Snake snake = factory.createSnake(5, "Anticlockwise", 10, 10, 1L);

        testSnakeMovement(snake, 10, 10, 10, 15);
    }

    /**
     * Tests the creation of a snake with an invalid direction.
     * Verifies that an {@link IllegalArgumentException} is thrown.
     */
    @Test
    public void testCreateSnakeInvalidDirection() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            factory.createSnake(5, "InvalidDirection", 10, 10, 1L);
        });

        assertTrue(thrown.getMessage().contains("Invalid direction"));
    }

    /**
     * Helper method to test the movement of a snake.
     * Moves the snake twice and verifies its position after movement.
     *
     * @param snake        The snake to test.
     * @param startX       The starting x-coordinate of the snake.
     * @param startY       The starting y-coordinate of the snake.
     * @param expectedX    The expected x-coordinate after movement.
     * @param expectedY    The expected y-coordinate after movement.
     */
    private void testSnakeMovement(Snake snake, double startX, double startY, double expectedX, double expectedY) {
        Rectangle2D square = new Rectangle2D.Double(10, 10, 100, 100);
        snake.setNewSnakeX(startX);
        snake.setNewSnakeY(startY);
        snake.move(square);
        snake.move(square);
        assertEquals(expectedX, snake.getNewSnakeX(), 0.01);
        assertEquals(expectedY, snake.getNewSnakeY(), 0.01);
    }
}
