CREATE TABLE workouts (
    workout_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    workout_level VARCHAR(50),
    workout_type VARCHAR(50),
    date DATE NOT NULL,
    duration INT NOT NULL,
    muscle_group VARCHAR(100),
    heart_rate INT
);

CREATE TABLE exercises (
    exercise_id SERIAL PRIMARY KEY,
    workout_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    exercise_type VARCHAR(50),
    distance DOUBLE PRECISION,
    minutes INT,
    sets INT,
    reps INT,
    weight DOUBLE PRECISION,
    CONSTRAINT fk_workout FOREIGN KEY (workout_id) REFERENCES workouts(workout_id) ON DELETE CASCADE
);

INSERT INTO workouts (name, workout_level, workout_type, date, duration, muscle_group)
VALUES ('Heavy Leg Day', 'Advanced', 'Strength', '2026-01-20', 75, 'Legs');

INSERT INTO exercises (workout_id, name, exercise_type, sets, reps, weight)
VALUES (1, 'Back Squat', 'Strength', 5, 5, 120.0);

INSERT INTO workouts (name, workout_level, workout_type, date, duration, heart_rate)
VALUES ('Morning Run', 'Intermediate', 'Cardio', '2026-01-21', 40, 155);

INSERT INTO exercises (workout_id, name, exercise_type, distance, minutes)
VALUES (2, 'Treadmill Run', 'Cardio', 6.2, 35);