package repositories;

import data.DatabaseConnection;
import exceptions.DatabaseOperationException;
import models.*;
import repositories.interfaces.CrudRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseRepository implements CrudRepository<Exercise> {
    private final DatabaseConnection db;
    public ExerciseRepository(DatabaseConnection db) {
        this.db = db;
    }
    @Override
    public boolean create(Exercise exercise) throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "INSERT INTO exercises (workout_id, name, exercise_type, sets, reps, weight, distance, minutes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, exercise.getWorkoutId());
                ps.setString(2, exercise.getName());
                ps.setString(3, exercise.getExerciseType());
                if (exercise instanceof StrengthExercise) {
                    StrengthExercise se = (StrengthExercise) exercise;
                    ps.setInt(4, se.getSets());
                    ps.setInt(5, se.getReps());
                    ps.setDouble(6, se.getWeight());
                    ps.setNull(7, Types.DOUBLE);
                    ps.setNull(8, Types.INTEGER);
                } else {
                    CardioExercise ce = (CardioExercise) exercise;
                    ps.setNull(4, Types.INTEGER);
                    ps.setNull(5, Types.INTEGER);
                    ps.setNull(6, Types.DOUBLE);
                    ps.setDouble(7, ce.getDistance());
                    ps.setInt(8, ce.getMinutes());
                }
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating exercise: " + e.getMessage());
        }
    }
    @Override
    public List<Exercise> getAll() throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "SELECT * FROM exercises";
            try (Statement st = con.createStatement()) {
                ResultSet rs = st.executeQuery(sql);
                List<Exercise> exercises = new ArrayList<>();
                while (rs.next()) {
                    String type = rs.getString("exercise_type");
                    if ("Strength".equals(type)) {
                        exercises.add(new StrengthExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getInt("sets"), rs.getInt("reps"), rs.getDouble("weight")));
                    } else {
                        exercises.add(new CardioExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getDouble("distance"), rs.getInt("minutes")));
                    }
                }
                return exercises;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching exercises: " + e.getMessage());
        }
    }
    @Override
    public Exercise getById(int id) throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "SELECT * FROM exercises WHERE exercise_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String type = rs.getString("exercise_type");
                    if ("Strength".equals(type)) {
                        return new StrengthExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getInt("sets"), rs.getInt("reps"), rs.getDouble("weight"));
                    } else {
                        return new CardioExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getDouble("distance"), rs.getInt("minutes"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching exercise by ID: " + e.getMessage());
        }
        return null;
    }
    @Override
    public boolean update(int id, Exercise exercise) throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "UPDATE exercises SET workout_id = ?, name = ?, exercise_type = ?, sets = ?, reps = ?, weight = ?, distance = ?, minutes = ? WHERE exercise_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, exercise.getWorkoutId());
                ps.setString(2, exercise.getName());
                ps.setString(3, exercise.getExerciseType());
                if (exercise instanceof StrengthExercise) {
                    StrengthExercise se = (StrengthExercise) exercise;
                    ps.setInt(4, se.getSets());
                    ps.setInt(5, se.getReps());
                    ps.setDouble(6, se.getWeight());
                    ps.setNull(7, Types.DOUBLE);
                    ps.setNull(8, Types.INTEGER);
                } else {
                    CardioExercise ce = (CardioExercise) exercise;
                    ps.setNull(4, Types.INTEGER);
                    ps.setNull(5, Types.INTEGER);
                    ps.setNull(6, Types.DOUBLE);
                    ps.setDouble(7, ce.getDistance());
                    ps.setInt(8, ce.getMinutes());
                }
                ps.setInt(9, id);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating exercise: " + e.getMessage());
        }
    }
    @Override
    public boolean delete(int id) throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "DELETE FROM exercises WHERE exercise_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting exercise: " + e.getMessage());
        }
    }
    // Additional method for loading exercises by workout ID (not part of CRUD, but needed)
    public List<Exercise> getByWorkoutId(int workoutId) throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "SELECT * FROM exercises WHERE workout_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, workoutId);
                ResultSet rs = ps.executeQuery();
                List<Exercise> exercises = new ArrayList<>();
                while (rs.next()) {
                    String type = rs.getString("exercise_type");
                    if ("Strength".equals(type)) {
                        exercises.add(new StrengthExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getInt("sets"), rs.getInt("reps"), rs.getDouble("weight")));
                    } else {
                        exercises.add(new CardioExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getDouble("distance"), rs.getInt("minutes")));
                    }
                }
                return exercises;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching exercises by workout ID: " + e.getMessage());
        }
    }
}