package model;

import exception.InvalidInputException;

public class StrengthExercise extends Exercise {
    private int sets;
    private int reps;
    private double weight;

    public StrengthExercise(int workoutId, String name, int sets, int reps, double weight) {
        super(workoutId, name);
        setExerciseType("Strength");
        setSets(sets);
        setReps(reps);
        setWeight(weight);
    }

    public StrengthExercise(int exerciseId, int workoutId, String name, int sets, int reps, double weight) {
        this(workoutId, name, sets, reps, weight);
        setExerciseId(exerciseId);
    }

    public int getSets() { return sets; }
    public void setSets(int sets) { this.sets = sets; }
    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    @Override
    public void validate() throws InvalidInputException {
        super.validate();
        if (sets <= 0 || reps <= 0 || weight < 0) {
            throw new InvalidInputException("Incorrect parameters of strength exercise");
        }
    }

    @Override
    public String display() {
        return String.format("%s: %d x %d of %.1f kg", getName(), sets, reps, weight);
    }

    public double getVolume() { return sets * reps * weight; }
}