package com.map_toysocialnetworkgui.repository.with_db;


import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.UserRepositoryInterface;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * a user repository that works with a database
 */
public class UserDBRepository implements UserRepositoryInterface {

    /**
     * the database's URL
     */
    private final String url;
    /**
     * the database's username
     */
    private final String username;
    /**
     * the database's password
     */
    private final String password;

    /**
     * constructor
     *
     * @param url      - url of database
     * @param username - username of database
     * @param password - password of database
     */
    public UserDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
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
    public boolean save(User user) {
        if (contains(user.getEmail()))
            return false;
        boolean toReturn = false;
        String sqlSave = "INSERT INTO users(email, password_hash, first_name, last_name, join_date,status_code) values (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementSave = connection.prepareStatement(sqlSave)) {

            statementSave.setString(1, user.getEmail());
            statementSave.setInt(2, user.getPasswordHash());
            statementSave.setString(3, user.getFirstName());
            statementSave.setString(4, user.getLastName());
            statementSave.setDate(5, Date.valueOf(user.getJoinDate()));
            statementSave.setInt(6, user.getAccountStatus().getStatusCode());
            statementSave.execute();
            toReturn = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public User get(String email) {
        User toReturn = null;
        String sqlFind = "SELECT * FROM users WHERE email = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
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
    public boolean update(User user) {
        boolean toReturn = false;
        String sqlUpdate = "UPDATE users SET password_hash = (?),first_name = (?), last_name = (?),join_date=(?), status_code = (?) WHERE email = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate)) {

            statementUpdate.setInt(1, user.getPasswordHash());
            statementUpdate.setString(2, user.getFirstName());
            statementUpdate.setString(3, user.getLastName());
            statementUpdate.setDate(4, Date.valueOf(user.getJoinDate()));
            statementUpdate.setInt(5, user.getAccountStatus().getStatusCode());
            statementUpdate.setString(6, user.getEmail());
            int affectedRows = statementUpdate.executeUpdate();
            toReturn = (affectedRows != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public boolean delete(String id) {
        boolean toReturn = false;
        String sqlDelete = "DELETE FROM users WHERE email = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementDelete = connection.prepareStatement(sqlDelete)) {
            statementDelete.setString(1, id);
            int rows = statementDelete.executeUpdate();
            toReturn = (rows != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Iterable<User> getAll() {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = DriverManager.getConnection(url, username, password);
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