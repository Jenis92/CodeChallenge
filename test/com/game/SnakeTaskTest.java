package com.game;

import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link SnakeTask} class.
 * Tests the behavior of the SnakeTask class to ensure it correctly interacts with its dependencies.
 */
public class SnakeTaskTest {

    private static class MockMovable implements Movable {
        private boolean moveCalled = false;
        private Rectangle2D lastSquare;

        /**
         * Mock implementation of the {@link Movable} interface for testing.
         * Records whether the move method was called and the last square it was called with.
         */
        @Override
        public void move(Rectangle2D square) {
            moveCalled = true;
            lastSquare = square;
        }

        public boolean isMoveCalled() {
            return moveCalled;
        }

        public Rectangle2D getLastSquare() {
            return lastSquare;
        }
    }

    /**
     * Mock implementation of the {@link Growable} interface for testing.
     * Records whether the grow method was called.
     */
    private static class MockGrowable implements Growable {
        private boolean growCalled = false;

        @Override
        public void grow() {
            growCalled = true;
        }

        public boolean isGrowCalled() {
            return growCalled;
        }
    }

    /**
     * Tests the {@link SnakeTask#run()} method.
     * Verifies that the move and grow methods are called appropriately.
     */
    @Test
    public void testRun() {
        // Create mock objects
        MockMovable mockMovable = new MockMovable();
        MockGrowable mockGrowable = new MockGrowable();
        Rectangle2D square = new Rectangle2D.Double(10, 10, 50, 50);

        // Create SnakeTask with mocks
        SnakeTask snakeTask = new SnakeTask(mockMovable, mockGrowable, square);

        // Run the task
        snakeTask.run();

        // Assert that move() was called on Movable
        assertTrue(mockMovable.isMoveCalled(), "Movable move() should be called");

        // Assert that grow() was called on Growable
        assertTrue(mockGrowable.isGrowCalled(), "Growable grow() should be called");

        // Assert that the correct square was passed to move()
        assertEquals(square, mockMovable.getLastSquare(), "The square passed to move() should be the same as the one provided");
    }
}
