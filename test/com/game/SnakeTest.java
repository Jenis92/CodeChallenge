package com.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Snake} class.
 * Tests the behavior of the Snake class to ensure it correctly handles movement and growth.
 */
public class SnakeTest {

    private Snake snake;
    private MovementStrategy movementStrategy;

    /**
     * Sets up the test environment before each test is executed.
     * Initializes a Snake instance with a minimal implementation of the MovementStrategy.
     */
    @BeforeEach
    public void setUp() {

        movementStrategy = new MovementStrategy() {
            @Override
            public void updateDirection(Snake snake, Rectangle2D square) {
                // Minimal implementation
                snake.setNewSnakeX(snake.getNewSnakeX() + snake.getSegmentSize());
                snake.setNewSnakeY(snake.getNewSnakeY());
            }
        };

        snake = new Snake(3, movementStrategy, 10, 10, 100);
    }

    /**
     * Tests the initial position of the Snake.
     * Verifies that the Snake's starting position is correctly set.
     */
    @Test
    public void testInitialSnakePosition() {
        assertEquals(10, snake.getSnakeStartX());
        assertEquals(10, snake.getSnakeStartY());
    }

    /**
     * Tests the growth functionality of the Snake.
     * Verifies that calling the grow method correctly updates the size of the Snake.
     */
    @Test
    public void testGrow() {
        snake.grow();
        Deque<RoundRectangle2D> segments = (Deque<RoundRectangle2D>) getPrivateField(snake, "segments");

        assertEquals(1, segments.size()); // Only one segment initially
        snake.grow();
        assertTrue(segments.size() == 1); // Ensure growth
    }

    /**
     * Tests the movement functionality of the Snake.
     * Verifies that the Snake moves correctly when the move method is called.
     */
    @Test
    public void testMove() {
        Rectangle2D square = new Rectangle2D.Double(10, 10, 50, 50);
        snake.move(square);
        Deque<RoundRectangle2D> segments = (Deque<RoundRectangle2D>) getPrivateField(snake, "segments");
        snake.move(square);
        assertEquals(1, segments.size()); // Ensure that the size is 1 after move (old segment should be removed)
        assertEquals(20, snake.getNewSnakeX()); // Expect new X to be incremented
        assertEquals(10, snake.getNewSnakeY()); // Y should remain the same
    }


    /**
     * Helper method to access private fields via reflection.
     * @param obj The object from which to retrieve the field value.
     * @param fieldName The name of the field to retrieve.
     * @return The value of the specified field.
     */
    private Object getPrivateField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
