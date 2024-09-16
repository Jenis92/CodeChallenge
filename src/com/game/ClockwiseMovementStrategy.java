package com.game;

import java.awt.geom.Rectangle2D;

public class ClockwiseMovementStrategy implements MovementStrategy{

    private double xDirection = 0;
    private double yDirection = 0;

    /**
     * Updates the direction of the snake based on the clockwise movement strategy.
     *
     * @param snake The snake object whose direction needs to be updated.
     * @param square The rectangular area in which the snake is moving.
     */
    @Override
    public void updateDirection(Snake snake, Rectangle2D square) {
        //Movement logic for clockwise direction
        double newX = snake.getNewSnakeX() + xDirection;
        double newY = snake.getNewSnakeY() + yDirection;
        double snakeWidth = snake.getSegmentSize();
        double snakeHeight = snake.getSegmentSize();
        double squareX = square.getX();
        double squareY = square.getY();
        double squareSize = square.getWidth();

        if(newX == squareX + squareSize - snakeWidth && newY == squareY){
            xDirection = 0;
            yDirection = snakeHeight;
        } else if(newX == squareX + squareSize - snakeWidth && newY == squareY + squareSize - snakeHeight){
            xDirection = -snakeWidth;
            yDirection = 0;
        } else if(newX == squareX && newY == squareY + squareSize - snakeHeight){
            xDirection = 0;
            yDirection = -snakeHeight;
        } else if(newX == squareX && newY == squareY){
            xDirection = snakeWidth;
            yDirection = 0;
        }
        snake.setNewSnakeX(newX);
        snake.setNewSnakeY(newY);
    }
}
