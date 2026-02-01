package controllers;

import exceptions.DatabaseOperationException;
import exceptions.InvalidInputException;
import exceptions.ResourceNotFoundException;
import models.Exercise;
import models.Workout;
import services.interfaces.IWorkoutService;

import java.util.List;


public class WorkoutController {
    private final IWorkoutService workoutService;
    public WorkoutController(IWorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    public String create(Workout workout) throws InvalidInputException, DatabaseOperationException {
        boolean created = workoutService.createWorkout(workout);
        return created ? "Workout created" : "Workout creation failed!";
    }

    public String getAll() throws DatabaseOperationException {
        List<Workout> workouts = workoutService.getAllWorkouts();
        StringBuilder sb = new StringBuilder();
        for (Workout w : workouts) {
            sb.append(w.toString()).append("\n");
            sb.append("Exercises:\n");
            for (Exercise e : w.getExercises()) {
                sb.append(" - ").append(e.display()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString().isEmpty() ? "No workouts found!" : sb.toString();
    }

    public String getById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        Workout w = workoutService.getWorkoutById(id);
        if (w == null) return "Workout not found!";
        StringBuilder sb = new StringBuilder(w.toString());
        sb.append("\nExercises:\n");
        for (Exercise e : w.getExercises()) {
            sb.append(" - ").append(e.display()).append("\n");
        }
        return sb.toString();
    }

    public String update(int id, Workout workout) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        boolean updated = workoutService.updateWorkout(id, workout);
        return updated ? "Workout updated!" : "Workout update failed!";
    }

    public String delete(int id) throws DatabaseOperationException, ResourceNotFoundException {
        boolean deleted = workoutService.deleteWorkout(id);
        return deleted ? "Workout deleted!" : "Workout deletion failed!";
    }
}