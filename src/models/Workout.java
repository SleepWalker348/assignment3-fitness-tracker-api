package models;

import exceptions.InvalidInputException;
import models.interfaces.Validatable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public abstract class Workout implements Validatable {
    private int workoutId;
    private String name;
    private String workoutLevel;
    private String workoutType;
    private LocalDate date;
    private int duration;
    private List<Exercise> exercises = new ArrayList<>();

    public Workout(String name, String workoutLevel, LocalDate date, int duration) {
        setName(name);
        setWorkoutLevel(workoutLevel);
        setDate(date);
        setDuration(duration);
    }


    public int getWorkoutId() { return workoutId; }
    public String getName() { return name; }
    public String getWorkoutLevel() { return workoutLevel; }
    public String getWorkoutType() { return workoutType; }
    public LocalDate getDate() { return date; }
    public int getDuration() { return duration; }
    public List<Exercise> getExercises() { return exercises; }

    public void setWorkoutId(int workoutID) { this.workoutId = workoutID; }
    public void setName(String name) { this.name = name; }
    public void setWorkoutLevel(String workoutLevel) { this.workoutLevel = workoutLevel; }
    public void setWorkoutType(String workoutType) { this.workoutType = workoutType; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setDuration(int duration) { this.duration = duration; }

    public void addExercise(Exercise exercise) {
        if (exercise != null) {
            this.getExercises().add(exercise);
        }
    }

    public double numberOfDays() {
        return ChronoUnit.DAYS.between(this.date, LocalDate.now());
    }

    public abstract double calories();
    public abstract int getRecoveryTime();

    @Override
    public void validate() throws InvalidInputException {
        if (name == null || name.isEmpty() || workoutLevel == null || workoutLevel.isEmpty()) {
            throw new InvalidInputException("Workout name or level cannot be empty");
        }
        if (duration < 5) {
            throw new InvalidInputException("The duration is too short");
        }

    }

    @Override
    public String toString() {
        return String.format(
                "Workout (ID: %d, Name: %s, Level: %s, Type: %s, Date: %s, Duration: %d min, Number of exercises: %d)",
                workoutId, name, workoutLevel, workoutType, date, duration, exercises.size()
        );
    }
}
