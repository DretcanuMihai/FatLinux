package com.map_toysocialnetworkgui.repository.with_db;


import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.repository.skeletons.AbstractDBRepository;
import com.map_toysocialnetworkgui.repository.CRUDException;
import com.map_toysocialnetworkgui.repository.skeletons.CRUDRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * a user repository that works with a database
 */
public class UserDBRepository extends AbstractDBRepository implements CRUDRepository<String,User> {

    public UserDBRepository(String url, String username, String password) {
        super(url, username, password);
    }

    /**
     * gets the next User from a given Result Set
     *
     * @param resultSet - said set
     * @return the next user
     * @throws SQLException - if any problems occur
     */
    private User getNextFromSet(ResultSet resultSet) throws SQLException {
        String email = resultSet.getString("email");
        String firstName = resultSet.getString("first_name");
        int passwordHash = resultSet.getInt("password_hash");
        LocalDate joinDate = resultSet.getDate("join_date").toLocalDate();
        String lastName = resultSet.getString("last_name");
        int accountStatusCode = resultSet.getInt("status_code");
        return new User(email, passwordHash, firstName, lastName, joinDate, accountStatusCode);
    }

    @Override
    public void save(User user) throws CRUDException {
        if (contains(user.getEmail()))
            throw new CRUDException("Error: Email already in use!;\n");
        String sqlSave = "INSERT INTO users(email, first_name, password_hash, join_date, last_name) values (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statementSave = connection.prepareStatement(sqlSave)) {

            statementSave.setString(1, user.getEmail());
            statementSave.setString(2, user.getFirstName());
            statementSave.setInt(3, user.getPasswordHash());
            statementSave.setDate(4, Date.valueOf(user.getJoinDate()));
            statementSave.setString(5, user.getLastName());
            statementSave.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User get(String email) {
        String sqlFind = "SELECT * FROM users WHERE email = (?)";
        User toReturn = null;

        try (Connection connection = getConnection();
             PreparedStatement statementFind = connection.prepareStatement(sqlFind)) {

            statementFind.setString(1, email);
            ResultSet result = statementFind.executeQuery();
            if (result.next()) {
                toReturn = getNextFromSet(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public void update(User user) throws CRUDException{
        String sqlUpdate = "UPDATE users SET first_name = (?), last_name = (?), password_hash = (?), status_code = (?) WHERE email = (?)";
        try (Connection connection = getConnection();
             PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate)) {

            statementUpdate.setString(1, user.getFirstName());
            statementUpdate.setString(2, user.getLastName());
            statementUpdate.setInt(3, user.getPasswordHash());
            int affectedRows = statementUpdate.executeUpdate();
            if (affectedRows == 0)
                throw new CRUDException("Error: Email not in use!;\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sqlDelete = "DELETE FROM users WHERE email = (?)";
        try (Connection connection = getConnection();
             PreparedStatement statementDelete = connection.prepareStatement(sqlDelete)) {
            statementDelete.setString(1,id);
            statementDelete.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns a collection of all users
     *
     * @return said collection
     */
    public Collection<User> getAll() {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = getNextFromSet(resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}