package repository;

import data.DatabaseConnection;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseRepository {
    private final DatabaseConnection db;

    public ExerciseRepository(DatabaseConnection db) {
        this.db = db;
    }

    public boolean createExercise(Exercise exercise) throws SQLException, exception.DatabaseOperationException {

        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "INSERT INTO exercises (workout_id, name, exercise_type, sets, reps, weight, distance, minutes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

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

            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("sql error during exercise creation: " + e.getMessage());
        }

        return false;
    }


    public List<Exercise> getAllExercises() throws SQLException, exception.DatabaseOperationException {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT * FROM exercises";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);
            List<Exercise> exercises = new ArrayList<>();
            while (rs.next()) {
                String type = rs.getString("exercise_type");
                if ("Strength".equals(type)) {
                    StrengthExercise se = new StrengthExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getInt("sets"), rs.getInt("reps"), rs.getDouble("weight"));
                    exercises.add(se);
                } else {
                    CardioExercise ce = new CardioExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getDouble("distance"), rs.getInt("minutes"));
                    exercises.add(ce);
                }
            }
            return exercises;
        } catch (SQLException e) {
            System.out.println("sql error fetching all exercises: " + e.getMessage());
        }

        return null;
    }

    public List<Exercise> getByWorkoutIdExercise(int workoutId) throws SQLException, exception.DatabaseOperationException {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT * FROM exercises WHERE workout_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, workoutId);
            ResultSet rs = ps.executeQuery();
            List<Exercise> exercises = new ArrayList<>();
            while (rs.next()) {
                String type = rs.getString("exercise_type");
                if ("Strength".equals(type)) {
                    StrengthExercise se = new StrengthExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getInt("sets"), rs.getInt("reps"), rs.getDouble("weight"));
                    exercises.add(se);
                } else {
                    CardioExercise ce = new CardioExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getDouble("distance"), rs.getInt("minutes"));
                    exercises.add(ce);
                }
            }
            return exercises;
        } catch (SQLException e) {
            System.out.println("sql error fetching exercise by workout ID: " + e.getMessage());
        }

        return null;
    }

    public Exercise getByIdExercise(int id) throws SQLException, exception.DatabaseOperationException {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT * FROM exercises WHERE exercise_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("exercise_type");
                if ("Strength".equals(type)) {
                    StrengthExercise se = new StrengthExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getInt("sets"), rs.getInt("reps"), rs.getDouble("weight"));
                    return se;
                } else {
                    CardioExercise ce = new CardioExercise(rs.getInt("exercise_id"), rs.getInt("workout_id"), rs.getString("name"), rs.getDouble("distance"), rs.getInt("minutes"));
                    return ce;
                }
            }
        } catch (SQLException e) {
            System.out.println("sql error fetching exercise by exercise ID: " + e.getMessage());
        }
        return null;

    }

    public boolean updateExercise(int id, Exercise exercise) throws SQLException, exception.DatabaseOperationException {

        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "UPDATE exercises SET workout_id = ?, name = ?, exercise_type = ?, distance = ?, minutes = ?, sets = ?, reps = ?, weight = ? WHERE exercise_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, exercise.getWorkoutId());
            ps.setString(2, exercise.getName());
            ps.setString(3, exercise.getExerciseType());

            if (exercise instanceof StrengthExercise) {
                ps.setNull(4, java.sql.Types.DOUBLE);
                ps.setNull(5, java.sql.Types.INTEGER);
                ps.setInt(6, ((StrengthExercise) exercise).getSets());
                ps.setInt(7, ((StrengthExercise) exercise).getReps());
                ps.setDouble(8, ((StrengthExercise) exercise).getWeight());
            } else if (exercise instanceof CardioExercise) {
                ps.setDouble(4, ((CardioExercise) exercise).getDistance());
                ps.setInt(5, ((CardioExercise) exercise).getMinutes());
                ps.setNull(6, java.sql.Types.INTEGER);
                ps.setNull(7, java.sql.Types.INTEGER);
                ps.setNull(8, java.sql.Types.DOUBLE);
            }
            ps.setInt(9, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("sql error during exercise update: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteExercise(int id) throws SQLException, exception.DatabaseOperationException {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "DELETE FROM exercises WHERE exercise_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("sql error during exercise deletion: " + e.getMessage());
        }
        return false;
    }

}