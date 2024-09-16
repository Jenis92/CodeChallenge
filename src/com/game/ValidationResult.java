package com.game;

import java.util.Optional;

/**
 * Represents the result of a validation process.
 * This class encapsulates information about whether the validation was successful,
 * any error messages, and the relevant validation data (length, speed, and direction).
 */
public class ValidationResult {

    private final boolean valid;
    private final String errorMessage;
    private final int length;
    private final long speed;
    private final String direction;

    /**
     * Constructs a new {@link ValidationResult} with the specified values.
     *
     * @param valid         Indicates whether the validation was successful.
     * @param errorMessage  An error message to be provided if validation fails. If null, an empty string is used.
     * @param length        The validated length value.
     * @param speed         The validated speed value.
     * @param direction     The validated direction value.
     */
    public ValidationResult(boolean valid, String errorMessage, int length, long speed, String direction){
        this.valid = valid;
        this.errorMessage = Optional.ofNullable(errorMessage).orElse("");
        this.length = length;
        this.speed = speed;
        this.direction = direction;
    }

    /**
     * Returns whether the validation was successful.
     *
     * @return true if the validation was successful, false otherwise.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Returns the error message associated with the validation result.
     * If the validation was successful, this will be an empty string.
     *
     * @return The error message or an empty string if validation was successful.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the validated length value.
     *
     * @return The validated length value.
     */
    public int getLength() {
        return length;
    }


    /**
     * Returns the validated speed value.
     *
     * @return The validated speed value.
     */
    public long getSpeed() {
        return speed;
    }


    /**
     * Returns the validated direction value.
     *
     * @return The validated direction value.
     */
    public String getDirection() {
        return direction;
    }
}
