package services;

import exceptions.DatabaseOperationException;
import exceptions.InvalidInputException;
import exceptions.ResourceNotFoundException;
import models.Exercise;
import models.Workout;
import repositories.interfaces.CrudRepository;
import services.interfaces.IExerciseService;
import services.interfaces.IWorkoutService;

import java.sql.SQLException;
import java.util.List;

public class ExerciseService implements IExerciseService {
    private final CrudRepository<Exercise> exerciseRepository;
    private final IWorkoutService workoutService;
    public ExerciseService(CrudRepository<Exercise> exerciseRepository, IWorkoutService workoutService) {
        this.exerciseRepository = exerciseRepository;
        this.workoutService = workoutService;
    }
    public boolean createExercise(Exercise exercise) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {
        exercise.logValidation();
        exercise.validate();
        Workout workout = workoutService.getWorkoutById(exercise.getWorkoutId());
        exercise.validate(workout);
        try {
            exerciseRepository.create(exercise);
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error: " + e.getMessage());
        }
    }
    public List<Exercise> getAllExercises() throws DatabaseOperationException {
        try {
            return exerciseRepository.getAll();
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error: " + e.getMessage());
        }
    }
    public Exercise getExerciseById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        try {
            Exercise exercise = exerciseRepository.getById(id);
            if (exercise == null) {
                throw new ResourceNotFoundException("Exercise not found");
            }
            return exercise;
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error: " + e.getMessage());
        }
    }
    public boolean updateExercise(int id, Exercise exercise) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {
        exercise.validate();
        getExerciseById(id);
        Workout workout = workoutService.getWorkoutById(exercise.getWorkoutId());
        exercise.validate(workout);
        try {
            exerciseRepository.update(id, exercise);
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error: " + e.getMessage());
        }
    }
    public boolean deleteExercise(int id) throws ResourceNotFoundException, DatabaseOperationException {
        getExerciseById(id);
        try {
            exerciseRepository.delete(id);
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error: " + e.getMessage());
        }
    }
}