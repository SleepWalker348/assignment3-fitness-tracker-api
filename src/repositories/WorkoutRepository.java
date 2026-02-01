package repositories;

import data.DatabaseConnection;
import exceptions.DatabaseOperationException;
import models.*;
import repositories.interfaces.CrudRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutRepository implements CrudRepository<Workout> {
    private final DatabaseConnection db;
    public WorkoutRepository(DatabaseConnection db) {
        this.db = db;
    }
    @Override
    public boolean create(Workout workout) throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "INSERT INTO workouts (name, workout_level, workout_type, date, duration, muscle_group, heart_rate) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, workout.getName());
                ps.setString(2, workout.getWorkoutLevel());
                ps.setString(3, workout.getWorkoutType());
                ps.setDate(4, Date.valueOf(workout.getDate()));
                ps.setInt(5, workout.getDuration());
                if (workout instanceof StrengthWorkout) {
                    ps.setString(6, ((StrengthWorkout) workout).getMuscleGroup());
                    ps.setNull(7, Types.INTEGER);
                } else {
                    ps.setNull(6, Types.VARCHAR);
                    ps.setInt(7, ((CardioWorkout) workout).getHeartRate());
                }
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating workout: " + e.getMessage());
        }
    }
    @Override
    public List<Workout> getAll() throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "SELECT * FROM workouts";
            try (Statement st = con.createStatement()) {
                ResultSet rs = st.executeQuery(sql);
                List<Workout> workouts = new ArrayList<>();
                while (rs.next()) {
                    String type = rs.getString("workout_type");
                    if ("Strength".equals(type)) {
                        workouts.add(new StrengthWorkout(rs.getInt("workout_id"), rs.getString("name"), rs.getString("workout_level"), rs.getDate("date").toLocalDate(), rs.getInt("duration"), rs.getString("muscle_group")));
                    } else {
                        workouts.add(new CardioWorkout(rs.getInt("workout_id"), rs.getString("name"), rs.getString("workout_level"), rs.getDate("date").toLocalDate(), rs.getInt("duration"), rs.getInt("heart_rate")));
                    }
                }
                return workouts;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching all workouts: " + e.getMessage());
        }
    }
    @Override
    public Workout getById(int id) throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "SELECT * FROM workouts WHERE workout_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String type = rs.getString("workout_type");
                    if ("Strength".equals(type)) {
                        return new StrengthWorkout(rs.getInt("workout_id"), rs.getString("name"), rs.getString("workout_level"), rs.getDate("date").toLocalDate(), rs.getInt("duration"), rs.getString("muscle_group"));
                    } else {
                        return new CardioWorkout(rs.getInt("workout_id"), rs.getString("name"), rs.getString("workout_level"), rs.getDate("date").toLocalDate(), rs.getInt("duration"), rs.getInt("heart_rate"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching workout by ID: " + e.getMessage());
        }
        return null;
    }
    @Override
    public boolean update(int id, Workout workout) throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "UPDATE workouts SET name = ?, workout_level = ?, workout_type = ?, date = ?, duration = ?, muscle_group = ?, heart_rate = ? WHERE workout_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, workout.getName());
                ps.setString(2, workout.getWorkoutLevel());
                ps.setString(3, workout.getWorkoutType());
                ps.setDate(4, Date.valueOf(workout.getDate()));
                ps.setInt(5, workout.getDuration());
                if (workout instanceof StrengthWorkout) {
                    ps.setString(6, ((StrengthWorkout) workout).getMuscleGroup());
                    ps.setNull(7, Types.INTEGER);
                } else {
                    ps.setNull(6, Types.VARCHAR);
                    ps.setInt(7, ((CardioWorkout) workout).getHeartRate());
                }
                ps.setInt(8, id);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating workout: " + e.getMessage());
        }
    }
    @Override
    public boolean delete(int id) throws SQLException, DatabaseOperationException {
        try (Connection con = db.getConnection()) {
            String sql = "DELETE FROM workouts WHERE workout_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting workout: " + e.getMessage());
        }
    }
}