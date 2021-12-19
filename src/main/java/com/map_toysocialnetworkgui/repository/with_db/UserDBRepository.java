package com.map_toysocialnetworkgui.repository.with_db;


import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.repository.CRUDException;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * a user repository that works with a database
 */
public class UserDBRepository {
    private final String url;
    private final String username;
    private final String password;

    public UserDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * gets the user with specified email from repo
     *
     * @param email - said email
     * @return the entity if it exists, null otherwise
     */
    public User get(String email) {
        String sqlFind = "SELECT * FROM users WHERE email = (?)";
        User toReturn = null;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementFind = connection.prepareStatement(sqlFind)) {

            statementFind.setString(1, email);
            ResultSet result = statementFind.executeQuery();
            if (result.next()) {
                String firstName = result.getString("first_name");
                int passwordHash = result.getInt("password_hash");
                LocalDate joinDate = result.getDate("join_date").toLocalDate();
                String lastName = result.getString("last_name");
                int accountStatusCode = result.getInt("status_code");
                toReturn = new User(email, passwordHash, firstName, lastName, joinDate, accountStatusCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * checks if a user with a specified email exists in repo
     *
     * @param email - said email
     * @return true if it exists, false otherwise
     */
    public boolean contains(String email) {
        return get(email) != null;
    }

    /**
     * saves a valid user if one with its email doesn't already exist
     * @param user - said user
     * @throws CRUDException - if a user with the same email already exists
     */
    public void save(User user) throws CRUDException{
        if(contains(user.getEmail()))
            throw new CRUDException("Error: Email already in use!;\n");
        String sqlSave = "INSERT INTO users(email, first_name, password_hash, join_date, last_name) values (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
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


    public void update(User user) {
        String sqlUpdate = "UPDATE users SET first_name = (?), password_hash = (?), join_date = (?), last_name = (?) WHERE email = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate)) {

            statementUpdate.setString(1, user.getFirstName());
            statementUpdate.setInt(2, user.getPasswordHash());
            statementUpdate.setDate(3, Date.valueOf(user.getJoinDate()));
            statementUpdate.setString(4, user.getLastName());
            statementUpdate.setString(5, user.getEmail());
            statementUpdate.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        String sqlDelete = "DELETE FROM users WHERE email = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementDelete = connection.prepareStatement(sqlDelete)) {
            statementDelete.setString(1, id);
            statementDelete.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection<User> getAll() {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("first_name");
                int password = resultSet.getInt("password_hash");
                Date joinDateSQL = resultSet.getDate("join_date");
                LocalDate joinDate = joinDateSQL.toLocalDate();
                String lastName = resultSet.getString("last_name");
                User user = new User(email, password, firstName, lastName, joinDate);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}