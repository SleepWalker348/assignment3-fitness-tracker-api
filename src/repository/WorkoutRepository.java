package repository;

import data.DatabaseConnection;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutRepository {
    private final DatabaseConnection db;

    public WorkoutRepository(DatabaseConnection db) {
        this.db = db;
    }

    public boolean createWorkout(Workout workout) throws SQLException, exception.DatabaseOperationException {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "INSERT INTO workouts (name, workout_level, workout_type, date, duration, muscle_group, heart_rate) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, workout.getName());
            ps.setString(2, workout.getWorkoutLevel());
            ps.setString(3, workout.getWorkoutType());
            ps.setDate(4, Date.valueOf(workout.getDate()));
            ps.setInt(5, workout.getDuration());

            if (workout instanceof StrengthWorkout) {
                ps.setString(6, ((StrengthWorkout) workout).getMuscleGroup());
                ps.setNull(7, Types.DOUBLE);
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setInt(7, ((CardioWorkout) workout).getHeartRate());
            }
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("sql error during workout creation: " + e.getMessage());
        }

        return false;
    }

    public List<Workout> getAllWorkouts() throws SQLException, exception.DatabaseOperationException {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT * FROM workouts";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);
            List<Workout> workouts = new ArrayList<>();
            while (rs.next()) {
                String type = rs.getString("workout_type");
                if ("Strength".equals(type)) {
                    StrengthWorkout sw = new StrengthWorkout(rs.getInt("workout_id"), rs.getString("name"), rs.getString("workout_level"), rs.getDate("date").toLocalDate(), rs.getInt("duration"), rs.getString("muscle_group"));
                    workouts.add(sw);
                } else {
                    CardioWorkout cw = new CardioWorkout(rs.getInt("workout_id"), rs.getString("name"), rs.getString("workout_level"), rs.getDate("date").toLocalDate(), rs.getInt("duration"), rs.getInt("heart_rate"));
                    workouts.add(cw);
                }
            }
            return workouts;
        } catch (SQLException e) {
            System.out.println("sql error fetching all workouts: " + e.getMessage());
        }

        return null;
    }

    public Workout getByIdWorkout(int id) throws SQLException, exception.DatabaseOperationException {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT * FROM workouts WHERE workout_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String type = rs.getString("workout_type");
                if ("Strength".equals(type)) {
                    StrengthWorkout sw = new StrengthWorkout(rs.getInt("workout_id"), rs.getString("name"), rs.getString("workout_level"), rs.getDate("date").toLocalDate(), rs.getInt("duration"), rs.getString("muscle_group"));
                    return sw;
                } else {
                    CardioWorkout cw = new CardioWorkout(rs.getInt("workout_id"), rs.getString("name"), rs.getString("workout_level"), rs.getDate("date").toLocalDate(), rs.getInt("duration"), rs.getInt("heart_rate"));
                    return cw;
                }
            }
        } catch (SQLException e) {
            System.out.println("sql error fetching workout by ID: " + e.getMessage());
        }
        return null;
    }

    public boolean updateWorkout(int id, Workout workout) throws SQLException, exception.DatabaseOperationException {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "UPDATE workouts SET name = ?, workout_level = ?, workout_type = ?, date = ?, duration = ?, muscle_group = ?, heart_rate = ? WHERE workout_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, workout.getName());
            ps.setString(2, workout.getWorkoutLevel());
            ps.setString(3, workout.getWorkoutType());
            ps.setDate(4, java.sql.Date.valueOf(workout.getDate()));
            ps.setInt(5, workout.getDuration());

            if (workout instanceof StrengthWorkout) {
                ps.setString(6, ((StrengthWorkout) workout).getMuscleGroup());
                ps.setNull(7, java.sql.Types.DOUBLE);
            } else if (workout instanceof CardioWorkout) {
                ps.setNull(6, java.sql.Types.VARCHAR);
                ps.setInt(7, ((CardioWorkout) workout).getHeartRate());
            }
            ps.setInt(8, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("sql error during workout update: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteWorkout(int id) throws SQLException, exception.DatabaseOperationException {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "DELETE FROM workouts WHERE workout_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("sql error during workout deletion: " + e.getMessage());
        }
        return false;
    }
}