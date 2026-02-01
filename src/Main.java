import controllers.ExerciseController;
import controllers.WorkoutController;
import data.DatabaseConnection;
import exceptions.DatabaseOperationException;
import exceptions.DuplicateResourceException;
import exceptions.InvalidInputException;
import exceptions.ResourceNotFoundException;
import models.CardioExercise;
import models.CardioWorkout;
import models.Exercise;
import models.StrengthExercise;
import models.StrengthWorkout;
import models.Workout;
import models.interfaces.Validatable;
import repositories.ExerciseRepository;
import repositories.WorkoutRepository;
import services.ExerciseService;
import services.WorkoutService;
import services.interfaces.IExerciseService;
import services.interfaces.IWorkoutService;
import utils.ReflectionUtils;
import utils.SortingUtils;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args)  {
        DatabaseConnection db = new DatabaseConnection();
        WorkoutRepository workoutRepo = new WorkoutRepository(db);
        ExerciseRepository exerciseRepo = new ExerciseRepository(db);
        IWorkoutService workoutService = new WorkoutService(workoutRepo, exerciseRepo);
        IExerciseService exerciseService = new ExerciseService(exerciseRepo, workoutService);
        WorkoutController workoutController = new WorkoutController(workoutService);
        ExerciseController exerciseController = new ExerciseController(exerciseService);

        try {
            StrengthWorkout newStrengthWorkout = new StrengthWorkout("Upper Body", "Intermediate",
                    LocalDate.of(2026, 1, 20), 60, "Chest and Back");
            System.out.println(workoutController.create(newStrengthWorkout));

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
            System.out.println(exerciseController.create(benchPress));

            StrengthExercise pullUps = new StrengthExercise(workoutId, "Pull-Ups", 3, 10, 0.0);
            System.out.println(exerciseController.create(pullUps));



            CardioWorkout newCardioWorkout = new CardioWorkout("Cardio Session", "Beginner",
                    LocalDate.of(2026, 1, 21), 45, 140);
            System.out.println(workoutController.create(newCardioWorkout));

            workouts = workoutService.getAllWorkouts();
            CardioWorkout createdCardioWorkout = null;
            for (Workout w : workouts) {
                if (w.getName().equals("Cardio Session")) {
                    createdCardioWorkout = (CardioWorkout) w;
                    break;
                }
            }
            if (createdCardioWorkout == null) {
                throw new RuntimeException("Failed to find created cardio workout");
            }
            int cardioWorkoutId = createdCardioWorkout.getWorkoutId();

            CardioExercise running = new CardioExercise(cardioWorkoutId, "Running", 5.0, 30);
            System.out.println(exerciseController.create(running));



            System.out.println();
            System.out.println(" - - - ");
            System.out.println();
            System.out.println(workoutController.getAll());



            System.out.println();
            System.out.println(" - - - ");
            System.out.println();
            workouts = workoutService.getAllWorkouts();
            SortingUtils.sortWorkoutsByExerciseCount(workouts, true);
            System.out.println("Sorted workouts by exercise count:");
            for (Workout w : workouts) {
                System.out.println(w.getName() + " - Exercises: " + w.getExercises().size());
            }



            System.out.println();
            System.out.println(" - - - ");
            System.out.println();
            ReflectionUtils.printClassInfo(StrengthWorkout.class);



            System.out.println();
            System.out.println(" - - - ");
            System.out.println();
            boolean isValid = Validatable.isValid(newStrengthWorkout);
            System.out.println("Is valid: " + isValid);



            System.out.println();
            System.out.println(" - - - ");
            System.out.println();
            StrengthWorkout updatedWorkout = new StrengthWorkout("Updated Upper Body", "Advanced",
                    LocalDate.of(2026, 1, 22), 75, "Chest, Back, Shoulders");
            System.out.println(workoutController.update(workoutId, updatedWorkout));

            List<Exercise> exercises = exerciseService.getAllExercises();
            if (!exercises.isEmpty()) {
                int exerciseIdToDelete = exercises.get(0).getExerciseId();
                System.out.println(exerciseController.delete(exerciseIdToDelete));
            }

            System.out.println(workoutController.delete(cardioWorkoutId));



            System.out.println();
            System.out.println(" - - - ");
            System.out.println();

            try {
                StrengthWorkout invalidWorkout = new StrengthWorkout("", "Beginner",
                        LocalDate.now(), 3, "Legs");
                System.out.println(workoutController.create(invalidWorkout));
            } catch (InvalidInputException e) {
                System.out.println("Exception: " + e.getMessage());
            }

            StrengthWorkout sw = new StrengthWorkout("Leg day", "Beginner",
                    LocalDate.of(2025, 12, 19), 89, "Legs");
            System.out.println(workoutController.create(sw));

            try {
                StrengthWorkout duplicate = new StrengthWorkout("Leg Day", "Advanced",
                        LocalDate.of(2026, 1, 20), 75, "Arms");
                System.out.println(workoutController.create(duplicate));
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
            System.out.println("Unexpected error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}

