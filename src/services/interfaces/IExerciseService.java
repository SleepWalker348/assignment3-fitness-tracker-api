package services.interfaces;

import exceptions.DatabaseOperationException;
import exceptions.InvalidInputException;
import exceptions.ResourceNotFoundException;
import models.Exercise;

import java.util.List;

public interface IExerciseService {
    boolean createExercise(Exercise exercise) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException;
    List<Exercise> getAllExercises() throws DatabaseOperationException;
    Exercise getExerciseById(int id) throws ResourceNotFoundException, DatabaseOperationException;
    boolean updateExercise(int id, Exercise exercise) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException;
    boolean deleteExercise(int id) throws ResourceNotFoundException, DatabaseOperationException;
}