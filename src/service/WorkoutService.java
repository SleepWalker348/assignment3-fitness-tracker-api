package service;

import exception.DatabaseOperationException;
import exception.DuplicateResourceException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Workout;
import repository.ExerciseRepository;
import repository.WorkoutRepository;

import java.sql.SQLException;
import java.util.List;

public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutService(WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public void createWorkout(Workout workout) throws InvalidInputException, DatabaseOperationException {
        workout.validate();
        Workout existing = findByName(workout.getName());
        if (existing != null) {
            throw new DuplicateResourceException("Workout with name '" + workout.getName() + "' already exists");
        }
        try {
            boolean success = workoutRepository.createWorkout(workout);
            if (!success) {
                throw new DatabaseOperationException("Failed to create workout");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error during workout creation: " + e.getMessage());
        }
    }

    public List<Workout> getAllWorkouts() throws DatabaseOperationException {
        try {
            List<Workout> workouts = workoutRepository.getAllWorkouts();
            if (workouts != null) {
                for (Workout w : workouts) {
                    loadExercises(w);
                }
            }
            return workouts;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error fetching all workouts: " + e.getMessage());
        }
    }

    public Workout getWorkoutById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        try {
            Workout workout = workoutRepository.getByIdWorkout(id);
            if (workout == null) {
                throw new ResourceNotFoundException("Workout with ID " + id + " not found");
            }
            loadExercises(workout);
            return workout;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error fetching workout by ID: " + e.getMessage());
        }
    }

    public void updateWorkout(int id, Workout workout) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {
        workout.validate();
        getWorkoutById(id);
        Workout existing = findByName(workout.getName());
        if (existing != null && existing.getWorkoutId() != id) {
            throw new DuplicateResourceException("Workout with name '" + workout.getName() + "' already exists");
        }
        try {
            boolean success = workoutRepository.updateWorkout(id, workout);
            if (!success) {
                throw new DatabaseOperationException("Failed to update workout");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error during workout update: " + e.getMessage());
        }
    }

    public void deleteWorkout(int id) throws ResourceNotFoundException, DatabaseOperationException {
        getWorkoutById(id);
        try {
            boolean success = workoutRepository.deleteWorkout(id);
            if (!success) {
                throw new DatabaseOperationException("Failed to delete workout");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error during workout deletion: " + e.getMessage());
        }
    }

    private void loadExercises(Workout workout) throws DatabaseOperationException {
        try {
            workout.getExercises().addAll(exerciseRepository.getByWorkoutIdExercise(workout.getWorkoutId()));
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error loading exercises: " + e.getMessage());
        }
    }

    private Workout findByName(String name) throws DatabaseOperationException {
        List<Workout> all = getAllWorkouts();
        if (all != null) {
            for (Workout w : all) {
                if (w.getName().equalsIgnoreCase(name)) {
                    return w;
                }
            }
        }
        return null;
    }
}