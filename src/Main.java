
import data.DatabaseConnection;
import exception.DatabaseOperationException;
import exception.DuplicateResourceException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.CardioExercise;
import model.CardioWorkout;
import model.Exercise;
import model.StrengthExercise;
import model.StrengthWorkout;
import model.Workout;
import repository.ExerciseRepository;
import repository.WorkoutRepository;
import service.ExerciseService;
import service.WorkoutService;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        WorkoutRepository workoutRepo = new WorkoutRepository(db);
        ExerciseRepository exerciseRepo = new ExerciseRepository(db);
        WorkoutService workoutService = new WorkoutService(workoutRepo, exerciseRepo);
        ExerciseService exerciseService = new ExerciseService(exerciseRepo, workoutService);

        try {
            StrengthWorkout newStrengthWorkout = new StrengthWorkout("Upper Body", "Intermediate",
                    LocalDate.of(2026, 1, 20), 60, "Chest and Back");
            workoutService.createWorkout(newStrengthWorkout);
            System.out.println("Created new strength workout");

            List<Workout> workouts = workoutService.getAllWorkouts();
            StrengthWorkout createdStrengthWorkout = null;
            for (Workout w : workouts) {
                if (w.getName().equals("Upper Body")) {
                    createdStrengthWorkout = (StrengthWorkout) w;
                    break;
                }
            }
            if (createdStrengthWorkout == null) {
                throw new RuntimeException("Failed to find created workout");
            }
            int workoutId = createdStrengthWorkout.getWorkoutId();


            StrengthExercise benchPress = new StrengthExercise(workoutId, "Bench Press", 4, 8, 100.0);
            exerciseService.createExercise(benchPress);
            System.out.println("Added Bench Press exercise");

            StrengthExercise pullUps = new StrengthExercise(workoutId, "Pull-Ups", 3, 10, 0.0);
            exerciseService.createExercise(pullUps);
            System.out.println("Added Pull-Ups exercise");



            CardioWorkout newCardioWorkout = new CardioWorkout( "Jog", "Beginner",
                    LocalDate.of(2026, 1, 15), 30, 140);
            workoutService.createWorkout(newCardioWorkout);
            System.out.println("Created new cardio workout");

            workouts = workoutService.getAllWorkouts();
            CardioWorkout createdCardioWorkout = null;
            for (Workout w : workouts) {
                if (w.getName().equals("Jog")) {
                    createdCardioWorkout = (CardioWorkout) w;
                    break;
                }
            }
            if (createdCardioWorkout == null) {
                throw new RuntimeException("Failed to find created cardio workout");
            }
            int cardioWorkoutId = createdCardioWorkout.getWorkoutId();


            CardioExercise jog = new CardioExercise(cardioWorkoutId, "Jogging", 5.0, 30);
            exerciseService.createExercise(jog);
            System.out.println("Added Jogging exercise");



            System.out.println();
            System.out.println("All Workouts:");
            workouts = workoutService.getAllWorkouts();
            for (Workout w : workouts) {
                System.out.println(w.toString());
                System.out.println("Calories burned: " + w.calories());
                System.out.println("Recovery time: " + w.getRecoveryTime() + " hours");
                System.out.println("Days since workout: " + w.numberOfDays());
                System.out.println("Exercises");
                for (Exercise e : w.getExercises()) {
                    System.out.println(" - Exercise: " + e.display());
                }
            }



            StrengthWorkout updatedWorkout = new StrengthWorkout("Updated Upper Body", "Advanced",
                    LocalDate.of(2026, 1, 22), 75, "Chest, Back, Shoulders");
            workoutService.updateWorkout(workoutId, updatedWorkout);
            System.out.println("Updated workout with ID " + workoutId);




            List<Exercise> exercises = exerciseService.getAllExercises();
            if (!exercises.isEmpty()) {
                int exerciseIdToDelete = exercises.getFirst().getExerciseId();
                exerciseService.deleteExercise(exerciseIdToDelete);
                System.out.println("Deleted exercise with ID " + exerciseIdToDelete);
            }

            workoutService.deleteWorkout(cardioWorkoutId);
            System.out.println("Deleted cardio workout with ID " + cardioWorkoutId);



            try {
                StrengthWorkout invalidWorkout = new StrengthWorkout("", "Beginner",
                        LocalDate.now(), 3, "Legs");
                workoutService.createWorkout(invalidWorkout);
            } catch (InvalidInputException e) {
                System.out.println("Exception: " + e.getMessage());
            }

            StrengthWorkout sw = new StrengthWorkout("Leg day", "Beginner",
                    LocalDate.of(2025, 12, 19), 89, "Legs");
            workoutService.createWorkout(sw);

            try {
                StrengthWorkout duplicate = new StrengthWorkout("Leg Day", "Advanced",
                        LocalDate.of(2026, 1, 20), 75, "Arms");
                workoutService.createWorkout(duplicate);
            } catch (DuplicateResourceException e) {
                System.out.println("Exception: " + e.getMessage());
            }

            try {
                workoutService.getWorkoutById(9999);
            } catch (ResourceNotFoundException e) {
                System.out.println("Exception: " + e.getMessage());
            }

        } catch (InvalidInputException | ResourceNotFoundException | DatabaseOperationException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Another error: " + e.getMessage());
        }
    }
}