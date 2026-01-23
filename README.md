A. Project Overview
This project is a Fitness Tracker API that manages workouts and exercises using JDBC for database interactions with PostgreSQL. It demonstrates advanced OOP principles including abstraction, inheritance, polymorphism, encapsulation, interfaces, and composition
The OOP design follows a hierarchical structure where specialized workout and exercise types inherit logic from abstract base classes, using polymorphism to handle different activities through a unified interface. A composition relationship connects these entities, where a Workout acts as a parent container for a list of Exercise objects

B. OOP Design Documentation
Abstract class and subclasses
Abstract class Workout with StrengthWorkout and CardioWorkout subclasses
Workout class has calories and getRecoveryTime abstract methods and numberOfDays concrete method
Abstract class Exercise with StrengthExercise and CardioExercise subclasses
Interfaces and implemented methods
Interface Validatable with method validate to validate data
Interface Displayable with method display to display information about class
Composition
Workout owns Exercise. Workout is a container that holds a list of specific exercises
Polymorphism examples
Polymorphism allows to store StrengthWorkout and CardioWorkout in a single List<Workout>. An example is overriding an abstract calories() method, where the program automatically executes the correct weight-based or heart-rate-based formula at runtime depending on the object type.

C. Database Description
Schema, constraints, foreign keys
In my database, I have two main tables workouts and exercises. The schema is designed to link them using foreign keys, creating a one-to-many relationship where one workout contains a list of exercises. I use primary keys to accurately update or delete specific records, and constraint NOT NULL ensure there are no empty fields or negative values in the workout duration.
workout_id acts as a foreign key in exercises table
D. Controller
Summary of CRUD operations
I used CRUD operations in main.java, create, update, delete. The result is in screenshots.

E. Instructions to Compile and Run
To compile and run this program, use the following commands in your terminal:
1. Compile all classes:
   javac *.java
2. Run the driver program:
   java Main

G. Reflection Section
What you learned
I learned how to connect to a database and work with it through code. I also understood the purpose of business logic and validation, the repository layer, the service layer, and the controller layer. I also learned how to handle exceptions.
Challenges faced
At first, it was completely unclear what to do. I didn't understand the terms repository, service, and controller. The lecture didn't explain much, and the video at the beginning was also unclear. The rest was more or less familiar.
Benefits of JDBC and multi-layer design
JDBC provides a secure way to connect Java code to any database. The multi-layer design separates code into different levels. This structure ensures data integrity by centralizing business rules and validation in a service layer before any information is saved.
User do not interact with database directly.