package com.game;

public class ConcreteSnakeFactory implements  SnakeFactory{
    /**
     * Creates a new Snake instance based on the provided parameters.
     *
     * @param length The length of the snake.
     * @param direction The direction in which the snake will move ("Clockwise" or "Anticlockwise").
     * @param x The initial x-coordinate of the snake.
     * @param y The initial y-coordinate of the snake.
     * @param speed The speed at which the snake moves.
     * @return A new Snake instance with the specified parameters and movement strategy.
     * @throws IllegalArgumentException If an invalid direction is provided.
     */
    @Override
    public Snake createSnake(int length, String direction, int x, int y, long speed) {
        MovementStrategy strategy;
        if("Clockwise".equalsIgnoreCase(direction)){
            strategy = new ClockwiseMovementStrategy();
        } else if("Anticlockwise".equalsIgnoreCase(direction)){
            strategy = new AnticlockwiseMovementStrategy();
        } else{
            throw new IllegalArgumentException("Invalid direction" + direction);
        }
        return new Snake(length,strategy,x,y,speed);
    }
}
