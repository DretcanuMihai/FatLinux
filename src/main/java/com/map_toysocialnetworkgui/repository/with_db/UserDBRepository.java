package com.map_toysocialnetworkgui.repository.with_db;


import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.PageImplementation;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
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

    @Override
    public User findOne(String email) {
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
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = getNextFromSet(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User user) {
        User toReturn = user;
        String sqlSave = "INSERT INTO users(email, password_hash, first_name, last_name, join_date) values (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementSave = connection.prepareStatement(sqlSave)) {

            statementSave.setString(1, user.getEmail());
            statementSave.setInt(2, user.getPasswordHash());
            statementSave.setString(3, user.getFirstName());
            statementSave.setString(4, user.getLastName());
            statementSave.setDate(5, Date.valueOf(user.getJoinDate()));
            statementSave.execute();
            toReturn = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public User delete(String id) {
        User toReturn = null;
        User user=findOne(id);
        if(user==null)
            return null;
        String sqlDelete = "DELETE FROM users WHERE email = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementDelete = connection.prepareStatement(sqlDelete)) {
            statementDelete.setString(1, id);
            int rows = statementDelete.executeUpdate();
            if(rows != 0)
                toReturn=user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public User update(User user) {
        User toReturn = user;
        String sqlUpdate = "UPDATE users SET password_hash = (?),first_name = (?), last_name = (?),join_date=(?) WHERE email = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate)) {

            statementUpdate.setInt(1, user.getPasswordHash());
            statementUpdate.setString(2, user.getFirstName());
            statementUpdate.setString(3, user.getLastName());
            statementUpdate.setDate(4, Date.valueOf(user.getJoinDate()));
            statementUpdate.setString(5, user.getEmail());
            int affectedRows = statementUpdate.executeUpdate();
            if(affectedRows != 0)
                toReturn=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * FROM users offset (?) limit (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int pageSize=pageable.getPageSize();
            int pageNr=pageable.getPageNumber();
            int start=(pageNr-1)*pageSize;
            statement.setInt(1,start);
            statement.setInt(2,pageSize);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = getNextFromSet(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable,users.stream());
    }

    @Override
    public Iterable<User> getUsersByName(String string) {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * FROM users u where u.first_name || ' ' || u.last_name like '%' || (?) ||'%'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1,string);
            ResultSet resultSet = statement.executeQuery();

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

    @Override
    public Page<User> getUsersByName(String string, Pageable pageable) {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * FROM users u where u.first_name || ' ' || u.last_name like '%' || (?) ||'%' offset (?) limit (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1,string);
            int pageSize=pageable.getPageSize();
            int pageNr=pageable.getPageNumber();
            int start=(pageNr-1)*pageSize;
            statement.setInt(2,start);
            statement.setInt(3,pageSize);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = getNextFromSet(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable,users.stream());
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
        return new User(email, passwordHash, firstName, lastName, joinDate);
    }
}