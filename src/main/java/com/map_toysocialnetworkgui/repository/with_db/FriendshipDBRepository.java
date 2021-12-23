package com.map_toysocialnetworkgui.repository.with_db;


import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.FriendshipRepositoryInterface;
import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDBRepository implements FriendshipRepositoryInterface {

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
    public FriendshipDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * gets the next Friendship from a given Result Set
     *
     * @param resultSet - said set
     * @return the next friendship
     * @throws SQLException - if any problems occur
     */
    private Friendship getNextFromSet(ResultSet resultSet) throws SQLException {
        String email1 = resultSet.getString("first_user_email");
        String email2 = resultSet.getString("second_user_email");
        LocalDate beginDate = resultSet.getDate("begin_date").toLocalDate();
        return new Friendship(email1, email2, beginDate);
    }

    @Override
    public boolean save(Friendship friendship) {
        if (contains(friendship.getId()))
            return false;
        boolean toReturn = false;
        String sqlSave = "INSERT INTO friendships(first_user_email, second_user_email, begin_date) values (?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementSave = connection.prepareStatement(sqlSave)) {

            statementSave.setString(1, friendship.getEmails().getFirst());
            statementSave.setString(2, friendship.getEmails().getSecond());
            statementSave.setDate(3, Date.valueOf(friendship.getBeginDate()));
            statementSave.execute();
            toReturn = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Friendship get(UnorderedPair<String> id) {
        Friendship toReturn = null;
        String sqlFind = "SELECT * from friendships where (first_user_email=(?) and second_user_email=(?))";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementFind = connection.prepareStatement(sqlFind)) {

            statementFind.setString(1, id.getFirst());
            statementFind.setString(2, id.getSecond());
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
    public Iterable<Friendship> getAll() {
        Set<Friendship> friendships = new HashSet<>();
        String sql = "SELECT * from friendships";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Friendship friendship = getNextFromSet(resultSet);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public boolean update(Friendship friendship) {
        boolean toReturn = false;
        String sqlUpdate = "UPDATE friendships SET begin_date=(?) WHERE first_user_email = (?) and second_user_email=(?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate)) {

            statementUpdate.setDate(1, Date.valueOf(friendship.getBeginDate()));
            statementUpdate.setString(2, friendship.getEmails().getFirst());
            statementUpdate.setString(3, friendship.getEmails().getSecond());
            int affectedRows = statementUpdate.executeUpdate();
            toReturn = (affectedRows != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public boolean delete(UnorderedPair<String> id) {
        boolean toReturn = false;
        String sqlDelete = "DELETE FROM friendships where (first_user_email=(?) and second_user_email=(?))";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementDelete = connection.prepareStatement(sqlDelete)) {

            statementDelete.setString(1, id.getFirst());
            statementDelete.setString(2, id.getSecond());
            int rows = statementDelete.executeUpdate();
            toReturn = (rows != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Iterable<Friendship> getUserFriendships(String userEmail) {
        Set<Friendship> friendships = new HashSet<>();
        String sql = "SELECT * from friendships where first_user_email=(?) or second_user_email=(?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userEmail);
            statement.setString(2, userEmail);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Friendship friendship = getNextFromSet(resultSet);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Iterable<Friendship> getUserFriendshipsFromMonth(String userEmail, int month) {
        Set<Friendship> friendships = new HashSet<>();
        String sql = "SELECT * from friendships where ((first_user_email= (?) or second_user_email=(?)) " +
                "and EXTRACT(MONTH FROM begin_date) = (?)) ";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userEmail);
            statement.setString(2, userEmail);
            statement.setInt(3, month);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Friendship friendship = getNextFromSet(resultSet);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }
}