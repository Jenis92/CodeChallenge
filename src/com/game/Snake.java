package com.game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Represents a snake in the game. The snake consists of a series of segments that can move,
 * grow, and be rendered onto a graphical context.
 */
public class Snake implements  Movable,Growable, Renderable{

    //Width and height of the snake segments
    private static final int SEGMENT_SIZE = 5;
    private static final int SNAKE_BODY_CURVE = 2;

    //Time in milliseconds for gradual growth
    private static final int GROWTH_INTERVAL = 2000;

    //Number of segments to grow per interval
    private static final int GROWTH_STEP = 1;

    private int length;
    private double snakeStartX;
    private double snakeStartY;
    private double newSnakeX;
    private double newSnakeY;
    private long lastGrowthTime;
    private int growthCounter;
    private double speed;
    private MovementStrategy movementStrategy;
    private Deque<RoundRectangle2D> segments;
    private final Object lock = new Object();


    /**
     * Constructs a Snake instance with the specified parameters.
     *
     * @param length The initial length of the snake.
     * @param movementStrategy The movement strategy for the snake.
     * @param snakeStartX The starting X coordinate of the snake.
     * @param snakeStartY The starting Y coordinate of the snake.
     * @param speed The speed at which the snake moves.
     */
    public Snake(int length, MovementStrategy movementStrategy, int snakeStartX, int snakeStartY, double speed){
        this.length = length;
        this.movementStrategy = movementStrategy;
        this.snakeStartX = snakeStartX;
        this.snakeStartY = snakeStartY;
        this.speed = speed;
        this.segments = new ArrayDeque<>();
        this.lastGrowthTime = System.currentTimeMillis();
        this.growthCounter = 0;
        this.newSnakeX = snakeStartX;
        this.newSnakeY = snakeStartY;
        segments.add(new RoundRectangle2D.Double(snakeStartX, snakeStartY, SEGMENT_SIZE, SEGMENT_SIZE,SNAKE_BODY_CURVE,SNAKE_BODY_CURVE));

    }

    /**
     * Removes the last segment of the snake's body.
     */
    private void removeTrailSegment(){
        synchronized (lock){
            if(segments.size() > GROWTH_STEP){
                segments.removeLast();
            }
        }
    }

    /**
     * Trims the snake's length to ensure it does not exceed the desired length.
     */
    private void trimExcesslength(){
        synchronized (lock){
            while(segments.size()>length){
                segments.removeLast();
            }
        }
    }

    /**
     * Adds a new segment to the front of the snake's body.
     *
     * @param x The X coordinate of the new segment.
     * @param y The Y coordinate of the new segment.
     */
    private void addSegment(double x, double y){
        synchronized (lock){
            segments.addFirst(new RoundRectangle2D.Double(x,y,SEGMENT_SIZE,SEGMENT_SIZE,SNAKE_BODY_CURVE,SNAKE_BODY_CURVE));
        }
    }

    public double getSnakeStartX() {
        return snakeStartX;
    }

    public double getSnakeStartY() {
        return snakeStartY;
    }

    public double getNewSnakeX() {
        return newSnakeX;
    }

    public void setNewSnakeX(double newSnakeX) {
        this.newSnakeX = newSnakeX;
    }

    public double getNewSnakeY() {
        return newSnakeY;
    }

    public void setNewSnakeY(double newSnakeY) {
        this.newSnakeY = newSnakeY;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSegmentSize(){
        return SEGMENT_SIZE;
    }

    @Override
    public void grow() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastGrowthTime >= GROWTH_INTERVAL){
            lastGrowthTime = currentTime;
            growthCounter++;
            if(growthCounter >= GROWTH_STEP){
                RoundRectangle2D lastSegment = segments.getLast();
                double newX = lastSegment.getX();
                double newY = lastSegment.getY();
                double width = lastSegment.getWidth();
                double height = lastSegment.getHeight();
                segments.addLast(new RoundRectangle2D.Double(newX, newY, width, height, SNAKE_BODY_CURVE, SNAKE_BODY_CURVE));
                growthCounter = 0;
            }
        }
        trimExcesslength();
    }

    @Override
    public void move(Rectangle2D square) {
        movementStrategy.updateDirection(this, square);
        addSegment(newSnakeX, newSnakeY);
        removeTrailSegment();
    }

    @Override
    public void render(Graphics2D g) {
        synchronized (lock) {
            for(RoundRectangle2D segment : segments){
                g.fill(segment);
            }
        }

    }
}
