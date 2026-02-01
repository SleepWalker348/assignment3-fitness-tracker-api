package models;

import exceptions.InvalidInputException;

public class CardioExercise extends Exercise {
    private double distance;
    private int minutes;

    public CardioExercise(int workoutId, String name, double distance, int minutes) {
        super(workoutId, name);
        setExerciseType("Cardio");
        setDistance(distance);
        setMinutes(minutes);
    }

    public CardioExercise(int exerciseId, int workoutId, String name, double distance, int minutes) {
        this(workoutId, name, distance, minutes);
        setExerciseId(exerciseId);
    }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public int getMinutes() { return minutes; }
    public void setMinutes(int minutes) { this.minutes = minutes; }

    @Override
    public void validate() throws InvalidInputException {
        super.validate();
        if (distance < 0 || minutes <= 0) {
            throw new InvalidInputException("The distance or minutes entered is incorrect");
        }
    }

    @Override
    public String display() {
        return String.format("%s: %.2f km in %d min", getName(), distance, minutes);
    }
}