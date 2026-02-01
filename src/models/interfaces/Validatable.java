package models.interfaces;

import exceptions.InvalidInputException;

public interface Validatable {
    void validate() throws InvalidInputException;

    default void logValidation() {
        System.out.println("Validating " + this.getClass().getSimpleName() + "...");
    }

    static boolean isValid(Validatable entity) {
        try {
            entity.validate();
            return true;
        } catch (InvalidInputException e) {
            System.err.println("Validation failed: " + e.getMessage());
            return false;
        }
    }
}