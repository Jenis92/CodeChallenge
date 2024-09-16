package com.game;

public interface SnakeFactory {

    /**
     * Creates a new {@link Snake} instance with the specified parameters.
     *
     * @param length The initial length of the snake.
     * @param direction The movement direction of the snake, which should be either "Clockwise" or "Anticlockwise".
     * @param x The starting X coordinate of the snake.
     * @param y The starting Y coordinate of the snake.
     * @param speed The speed at which the snake moves, in milliseconds per update.
     * @return A new {@link Snake} instance configured with the given parameters.
     * @throws IllegalArgumentException if the direction is invalid.
     */
    Snake createSnake(int length, String direction, int x, int y, long speed);
}
