package services.interfaces;

import exceptions.DatabaseOperationException;
import exceptions.DuplicateResourceException;
import exceptions.InvalidInputException;
import exceptions.ResourceNotFoundException;
import models.Workout;

import java.util.List;

public interface IWorkoutService {
    boolean createWorkout(Workout workout) throws InvalidInputException, DuplicateResourceException, DatabaseOperationException;
    List<Workout> getAllWorkouts() throws DatabaseOperationException;
    Workout getWorkoutById(int id) throws ResourceNotFoundException, DatabaseOperationException;
    boolean updateWorkout(int id, Workout workout) throws InvalidInputException, ResourceNotFoundException, DuplicateResourceException, DatabaseOperationException;
    boolean deleteWorkout(int id) throws ResourceNotFoundException, DatabaseOperationException;
}