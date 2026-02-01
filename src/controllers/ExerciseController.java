package controllers;

import exceptions.DatabaseOperationException;
import exceptions.InvalidInputException;
import exceptions.ResourceNotFoundException;
import models.Exercise;
import services.interfaces.IExerciseService;

import java.util.List;

public class ExerciseController {
    private final IExerciseService exerciseService;
    public ExerciseController(IExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    public String create(Exercise exercise) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        boolean created = exerciseService.createExercise(exercise);
        return created ? "Exercise created" : "Exercise creation failed!";
    }

    public String getAll() throws DatabaseOperationException {
        List<Exercise> exercises = exerciseService.getAllExercises();
        StringBuilder sb = new StringBuilder();
        for (Exercise e : exercises) {
            sb.append(e.display()).append("\n");
        }
        return sb.toString().isEmpty() ? "No exercises found!" : sb.toString();
    }

    public String getById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        Exercise e = exerciseService.getExerciseById(id);
        return e != null ? e.display() : "Exercise not found!";
    }

    public String update(int id, Exercise exercise) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        boolean updated = exerciseService.updateExercise(id, exercise);
        return updated ? "Exercise updated!" : "Exercise update failed!";
    }

    public String delete(int id) throws DatabaseOperationException, ResourceNotFoundException {
        boolean deleted = exerciseService.deleteExercise(id);
        return deleted ? "Exercise deleted!" : "Exercise deletion failed!";
    }
}