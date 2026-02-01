package services;

import exceptions.DatabaseOperationException;
import exceptions.DuplicateResourceException;
import exceptions.InvalidInputException;
import exceptions.ResourceNotFoundException;
import models.Workout;
import repositories.ExerciseRepository;
import repositories.interfaces.CrudRepository;
import services.interfaces.IWorkoutService;

import java.sql.SQLException;
import java.util.List;

public class WorkoutService implements IWorkoutService {
    private final CrudRepository<Workout> workoutRepository;
    private final ExerciseRepository exerciseRepository;
    public WorkoutService(CrudRepository<Workout> workoutRepository, ExerciseRepository exerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
    }
    public boolean createWorkout(Workout workout) throws InvalidInputException, DuplicateResourceException, DatabaseOperationException {
        workout.logValidation();
        workout.validate();
        if (findByName(workout.getName()) != null) {
            throw new DuplicateResourceException("Duplicate workout name");
        }
        try {
            workoutRepository.create(workout);
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error: " + e.getMessage());

        }

    }
    public List<Workout> getAllWorkouts() throws DatabaseOperationException {
        try {
            List<Workout> workouts = workoutRepository.getAll();
            for (Workout w : workouts) {
                loadExercises(w);
            }
            return workouts;
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error: " + e.getMessage());
        }
    }
    public Workout getWorkoutById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        try {
            Workout workout = workoutRepository.getById(id);
            if (workout == null) {
                throw new ResourceNotFoundException("Workout not found");
            }
            loadExercises(workout);
            return workout;
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error: " + e.getMessage());
        }
    }
    public boolean updateWorkout(int id, Workout workout) throws InvalidInputException, ResourceNotFoundException, DuplicateResourceException, DatabaseOperationException {
        workout.validate();
        getWorkoutById(id);
        Workout existing = findByName(workout.getName());
        if (existing != null && existing.getWorkoutId() != id) {
            throw new DuplicateResourceException("Duplicate name");
        }
        try {
            workoutRepository.update(id, workout);
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error: " + e.getMessage());
        }
    }
    public boolean deleteWorkout(int id) throws ResourceNotFoundException, DatabaseOperationException {
        getWorkoutById(id);
        try {
            workoutRepository.delete(id);
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error: " + e.getMessage());
        }
    }
    private void loadExercises(Workout workout) throws DatabaseOperationException {
        try {
            workout.getExercises().addAll(exerciseRepository.getByWorkoutId(workout.getWorkoutId()));
        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error loading exercises: " + e.getMessage());
        }
    }
    private Workout findByName(String name) throws DatabaseOperationException {
        List<Workout> all = getAllWorkouts();
        return all.stream().filter(w -> w.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}