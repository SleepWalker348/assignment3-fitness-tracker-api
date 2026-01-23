package model;

import exception.InvalidInputException;

public abstract class Exercise implements Validatable, Displayable {
    private int exerciseId;
    private int workoutId;
    private String name;
    private String exerciseType;

    public Exercise(int workoutId, String name) {
        setWorkoutId(workoutId);
        setName(name);
    }

    public int getExerciseId() { return exerciseId; }
    public void setExerciseId(int exerciseId) { this.exerciseId = exerciseId; }
    public int getWorkoutId() { return workoutId; }
    public void setWorkoutId(int workoutId) { this.workoutId = workoutId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getExerciseType() { return exerciseType; }
    public void setExerciseType(String exerciseType) { this.exerciseType = exerciseType; }


    @Override
    public void validate() throws InvalidInputException {
        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("Exercise name cannot be empty");
        }
        if (workoutId < 0) {
            throw new InvalidInputException("WorkoutId is incorrect");
        }
    }

    public void validate(Workout workout) throws InvalidInputException {
        int strengthCount = 0, cardioCount = 0;
        for (Exercise e : workout.getExercises()) {
            if (e instanceof StrengthExercise) {
                strengthCount++;
            } else if (e instanceof CardioExercise) {
                cardioCount++;
            }
        }
        if (cardioCount == 0 && strengthCount == 0) {
            return;
        } else if (workout instanceof StrengthWorkout) {
            if (cardioCount >= strengthCount) {
                throw new InvalidInputException("Too much cardio exercises in strength workout");
            }
        } else if (workout instanceof CardioWorkout) {
            if (strengthCount >= cardioCount) {
                throw new InvalidInputException("Too much strength exercises in strength workout");
            }
        }
    }
}