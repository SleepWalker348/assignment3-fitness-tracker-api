A. SOLID Documentation
Explain how each principle is applied:
• SRP per class
Workout, StrengthWorkout, CardioWorkout - represent workout entities and calculate specific metrics
Exercise, StrengthExercise, CardioExercise - represent individual exercises with type-specific validation and display logic
WorkoutController, ExerciseController - handle user-facing communication
WorkoutService, ExerciseService - contain business logic, validation coordination, duplicate checks
WorkoutRepository, ExerciseRepository - responsible only for database CRUD operations
DatabaseConnection - only manages database connection creation
• OCP in subclass extension
New workout types can be added by creating new subclasses of Workout without touching WorkoutController, WorkoutService, or WorkoutRepository
Same for Exercise class
• LSP between base class and children
Any Workout reference can hold StrengthWorkout or CardioWorkout and correctly call all methods
StrengthExercise and CardioExercise can be stored in List<Exercise> and correctly invoke all methods
• ISP for clean, narrow interfaces
Validatable interface contains only validate() and default logValidation() and static isValid()
Displayable interface has only one method display()
CrudRepository defines only the five basic CRUD operations
IWorkoutService and IExerciseService are separated and contain only methods relevant to their domain
• DIP for constructor-injected interfaces
WorkoutController depends on IWorkoutService
WorkoutService depends on CrudRepository<Workout>
ExerciseService depends on CrudRepository<Exercise> and IWorkoutService


B. Advanced OOP Features
Must include short explanations of where you used:
• Generics 
CrudRepository<T> interface - allows type-safe CRUD operations for any entity type
• Lambdas
WorkoutService.findByName() - stream + lambda + filter + findFirst
all.stream().filter(w -> w.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
Comparator<Workout> comparator = Comparator.comparingInt(w -> w.getExercises().size());
To compare using the size of workout exercise list.
• Reflection
ReflectionUtils.printClassInfo(Class<?> c) utility - prints fields and methods
• Interface default/static methods
Validatable interface:
default logValidation() - prints validation start message
static isValid(Validatable entity) - tries to validate and returns boolean + logs error


C. OOP Documentation
• Abstract class + subclasses
Workout - base for all workouts - StrengthWorkout, CardioWorkout
Exercise - base for all exercises - StrengthExercise, CardioExercise
• Composition relationships
Workout owns a list of exercises. Exercises cannot exist meaningfully without a workout
• Polymorphism examples
Calling workout.calories() invokes different formulas depending on whether it is StrengthWorkout or CardioWorkout
Calling exercise.display() shows different formats


D. Database Section
-

E. Architecture Explanation
• Controller, services, repositories roles
Controllers accept objects, call services, format results as strings for console.
Services - business logic, validation, duplicate checks, call repositories.
Repositories - translate objects into SQL.
• Examples of request/response behavior
Create workout - workoutController.create(strengthWorkout) - workoutService.createWorkout() - validates - checks duplicate → workoutRepository.create()
Get all workouts - workoutController.getAll() - workoutService.getAllWorkouts() - loads workouts - loads exercises for each - returns formatted string with polymorphic display()
Add exercise - checks workout exists - validates exercise type matches workout type - saves

F. Execution Instructions
• How to compile and run
To compile and run this program, use the following commands in your terminal:
1. Compile all classes:
   javac *.java
2. Run the driver program:
   java Main
• Requirements (Java version, DB connection)
Java version: Java 8 or higher
Database: PostgreSQL
Database name: fitness_tracker
User: postgres
Password: 1111
Run schema.sql to create tables and insert sample data

G. Screenshots
-

H. Reflection
• What you learned
I learned how to apply the SOLID principles. I also learned reflection, generics, lambda expressions, default and static interface methods. 
• Challenges
Lambdas, generics and reflection were a bit difficult.
• Value of SOLID architecture
Maintainable, flexible, testable, and scalable project.
