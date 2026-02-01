package models;

import exceptions.InvalidInputException;

import java.time.LocalDate;

public class CardioWorkout extends Workout {
    private int heartRate;

    public CardioWorkout(String name, String workoutLevel, LocalDate date, int duration, int heartRate) {
        super(name, workoutLevel, date, duration);
        setWorkoutType("Cardio");
        setHeartRate(heartRate);
    }

    public CardioWorkout(int workoutId, String name, String workoutLevel, LocalDate date, int duration, int heartRate) {
        this(name, workoutLevel, date, duration, heartRate);
        setWorkoutId(workoutId);
    }


    public int getHeartRate() { return heartRate; }
    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }

    @Override
    public double calories() {
        double totalDist = 0;
        for (Exercise e : getExercises()) {
            if (e instanceof CardioExercise) {
                totalDist += ((CardioExercise) e).getDistance();
            }
        }
        return totalDist * 65;
    }

    @Override
    public int getRecoveryTime() {
        return getDuration() > 60 ? 24 : 12;
    }

    @Override
    public void validate() throws InvalidInputException {
        super.validate();
        if (heartRate < 40 || heartRate > 220) {
            throw new InvalidInputException("Inappropriate heart rate for workout");
        }
    }
}