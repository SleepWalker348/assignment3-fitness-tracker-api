package models;

import exceptions.InvalidInputException;

import java.time.LocalDate;

public class StrengthWorkout extends Workout {
    private String muscleGroup;

    public StrengthWorkout(String name, String workoutLevel, LocalDate date, int duration, String muscleGroup) {
        super(name, workoutLevel, date, duration);
        setWorkoutType("Strength");
        setMuscleGroup(muscleGroup);
    }

    public StrengthWorkout(int workoutId, String name, String workoutLevel, LocalDate date, int duration, String muscleGroup) {
        this(name, workoutLevel, date, duration, muscleGroup);
        setWorkoutId(workoutId);
    }

    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }

    @Override
    public double calories() {
        double totalVolume = 0;
        for (Exercise e : getExercises()) {
            if (e instanceof StrengthExercise) {
                totalVolume += ((StrengthExercise) e).getVolume();
            }
        }
        return (getDuration() * 5) + (totalVolume * 0.05);
    }

    @Override
    public int getRecoveryTime() {
        return this.getExercises().size() * 8;
    }

    @Override
    public void validate() throws InvalidInputException {
        super.validate();
        if (muscleGroup == null || muscleGroup.isEmpty()) {
            throw new InvalidInputException("Muscle group can not be empty");
        }
    }
}