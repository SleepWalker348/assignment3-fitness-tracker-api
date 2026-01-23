package service;

import exception.DatabaseOperationException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Exercise;
import model.Workout;
import repository.ExerciseRepository;

import java.sql.SQLException;
import java.util.List;

public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final WorkoutService workoutService;

    public ExerciseService(ExerciseRepository exerciseRepository, WorkoutService workoutService) {
        this.exerciseRepository = exerciseRepository;
        this.workoutService = workoutService;
    }

    public void createExercise(Exercise exercise) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {
        exercise.validate();
        Workout workout = workoutService.getWorkoutById(exercise.getWorkoutId());
        exercise.validate(workout);
        try {
            boolean success = exerciseRepository.createExercise(exercise);
            if (!success) {
                throw new DatabaseOperationException("Failed to create exercise");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error during exercise creation: " + e.getMessage());
        }
    }

    public List<Exercise> getAllExercises() throws DatabaseOperationException {
        try {
            return exerciseRepository.getAllExercises();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error fetching all exercises: " + e.getMessage());
        }
    }

    public Exercise getExerciseById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        try {
            Exercise exercise = exerciseRepository.getByIdExercise(id);
            if (exercise == null) {
                throw new ResourceNotFoundException("Exercise with ID " + id + " not found");
            }
            return exercise;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error fetching exercise by exercise ID: " + e.getMessage());
        }
    }

    public void updateExercise(int id, Exercise exercise) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {
        exercise.validate();
        getExerciseById(id);
        workoutService.getWorkoutById(exercise.getWorkoutId());
        try {
            boolean success = exerciseRepository.updateExercise(id, exercise);
            if (!success) {
                throw new DatabaseOperationException("Failed to update exercise");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error during exercise update: " + e.getMessage());
        }
    }

    public void deleteExercise(int id) throws ResourceNotFoundException, DatabaseOperationException {
        getExerciseById(id);
        try {
            boolean success = exerciseRepository.deleteExercise(id);
            if (!success) {
                throw new DatabaseOperationException("Failed to delete exercise");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error during exercise deletion: " + e.getMessage());
        }
    }
}