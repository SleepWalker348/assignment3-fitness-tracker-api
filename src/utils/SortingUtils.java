package utils;
import models.Workout;
import java.util.Comparator;
import java.util.List;

public class SortingUtils {
    public static void sortWorkoutsByExerciseCount(List<Workout> workouts, boolean ascending) {
        Comparator<Workout> comparator = Comparator.comparingInt(w -> w.getExercises().size());
        if (!ascending) {
            comparator = comparator.reversed();
        }
        workouts.sort(comparator);
    }
}