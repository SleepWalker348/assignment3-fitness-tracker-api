package repositories.interfaces;

import exceptions.DatabaseOperationException;
import java.sql.SQLException;
import java.util.List;
public interface CrudRepository<T> {
    boolean create(T entity) throws SQLException, DatabaseOperationException;
    List<T> getAll() throws SQLException, DatabaseOperationException;
    T getById(int id) throws SQLException, DatabaseOperationException;
    boolean update(int id, T entity) throws SQLException, DatabaseOperationException;
    boolean delete(int id) throws SQLException, DatabaseOperationException;
}

