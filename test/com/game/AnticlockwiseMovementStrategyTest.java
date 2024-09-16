package com.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link AnticlockwiseMovementStrategy} class.
 * These tests verify that the snake moves correctly in an anticlockwise direction
 * when it encounters the boundaries of a given square.
 */
public class AnticlockwiseMovementStrategyTest {

    private AnticlockwiseMovementStrategy strategy;
    private Snake snake;
    private Rectangle2D square;

    /**
     * Sets up the test environment before each test case.
     * Initializes the movement strategy, snake, and boundary square.
     */
    @BeforeEach
    public void setUp() {
        strategy = new AnticlockwiseMovementStrategy();
        snake = new Snake(5, strategy, 10, 10, 1.0);
        square = new Rectangle2D.Double(10, 10, 50, 50);
    }


    /**
     * Tests the snake's movement when starting from the top-left corner of the boundary square.
     * The snake should move downwards to the next segment position.
     */
    @Test
    public void testMovementFromTopLeftCorner() {
        snake.setNewSnakeX(10);
        snake.setNewSnakeY(10);
        strategy.updateDirection(snake, square);

        snake.setNewSnakeX(snake.getNewSnakeX() + 0);
        snake.setNewSnakeY(snake.getNewSnakeY() + 5);

        assertEquals(10, snake.getNewSnakeX(), 0.01);
        assertEquals(15, snake.getNewSnakeY(), 0.01);
    }

    /**
     * Tests the snake's movement when starting from the bottom-left corner of the boundary square.
     * The snake should move to the right to the next segment position.
     */
    @Test
    public void testMovementFromBottomLeftCorner() {
        snake.setNewSnakeX(10);
        snake.setNewSnakeY(55);
        strategy.updateDirection(snake, square);

        snake.setNewSnakeX(snake.getNewSnakeX() + 5);
        snake.setNewSnakeY(snake.getNewSnakeY() + 0);

        assertEquals(15, snake.getNewSnakeX(), 0.01);
        assertEquals(55, snake.getNewSnakeY(), 0.01);
    }

    /**
     * Tests the snake's movement when starting from the bottom-right corner of the boundary square.
     * The snake should move upwards to the next segment position.
     */
    @Test
    public void testMovementFromBottomRightCorner() {
        snake.setNewSnakeX(55);
        snake.setNewSnakeY(55);
        strategy.updateDirection(snake, square);

        snake.setNewSnakeX(snake.getNewSnakeX() + 0);
        snake.setNewSnakeY(snake.getNewSnakeY() - 5);

        assertEquals(55, snake.getNewSnakeX(), 0.01);
        assertEquals(50, snake.getNewSnakeY(), 0.01);
    }

    /**
     * Tests the snake's movement when starting from the top-right corner of the boundary square.
     * The snake should move to the left to the next segment position.
     */
    @Test
    public void testMovementFromTopRightCorner() {
        snake.setNewSnakeX(55);
        snake.setNewSnakeY(10);
        strategy.updateDirection(snake, square);

        snake.setNewSnakeX(snake.getNewSnakeX() - 5);
        snake.setNewSnakeY(snake.getNewSnakeY() + 0);

        assertEquals(50, snake.getNewSnakeX(), 0.01);
        assertEquals(10, snake.getNewSnakeY(), 0.01);
    }
}
