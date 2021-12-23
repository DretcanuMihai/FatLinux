package com.map_toysocialnetworkgui.repository.with_db;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.FriendRequestRepositoryInterface;
import com.map_toysocialnetworkgui.utils.structures.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Repository in database for friend request
 */
public class FriendRequestDBRepository implements FriendRequestRepositoryInterface {

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
    public FriendRequestDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * gets the next Friend Request from a given Result Set
     *
     * @param result - said set
     * @return the next friend request
     * @throws SQLException - if any problems occur
     */
    private FriendRequest getNextFromSet(ResultSet result) throws SQLException {
        String email1 = result.getString("sender_email");
        String email2 = result.getString("receiver_email");
        LocalDateTime sendTime = result.getTimestamp("send_time").toLocalDateTime();
        return new FriendRequest(email1, email2, sendTime);
    }

    @Override
    public FriendRequest save(FriendRequest friendRequest) {
        if (findOne(friendRequest.getId()) != null)
            return false;
        boolean toReturn = false;
        String sqlSave = "INSERT INTO friend_requests(sender_email, receiver_email, send_time) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementSave = connection.prepareStatement(sqlSave)) {

            statementSave.setString(1, friendRequest.getSender());
            statementSave.setString(2, friendRequest.getReceiver());
            statementSave.setTimestamp(3, Timestamp.valueOf(friendRequest.getSendTime()));
            statementSave.execute();
            toReturn = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public FriendRequest findOne(Pair<String, String> id) {
        FriendRequest toReturn = null;
        String sqlFind = "SELECT * FROM friend_requests WHERE (sender_email = (?) AND receiver_email = (?))";

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
    public Iterable<FriendRequest> findAll() {
        Set<FriendRequest> friendRequests = new HashSet<>();
        String sql = "SELECT * FROM friend_requests";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                FriendRequest friendRequest = getNextFromSet(resultSet);
                friendRequests.add(friendRequest);
            }
            return friendRequests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequests;
    }

    @Override
    public FriendRequest update(FriendRequest friendRequest) {
        boolean toReturn = false;
        String sqlUpdate = "UPDATE friend_requests SET send_time=(?) WHERE sender_email = (?) and receiver_email=(?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate)) {

            statementUpdate.setTimestamp(1, Timestamp.valueOf(friendRequest.getSendTime()));
            statementUpdate.setString(2, friendRequest.getSender());
            statementUpdate.setString(3, friendRequest.getReceiver());
            int affectedRows = statementUpdate.executeUpdate();
            toReturn = (affectedRows != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public FriendRequest delete(Pair<String, String> id) {
        boolean toReturn = false;
        String sqlDelete = "DELETE FROM friend_requests WHERE (sender_email = (?) AND receiver_email = (?))";
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
    public Iterable<FriendRequest> getFriendRequestsSentToUser(String userEmail) {
        Set<FriendRequest> friendRequests = new HashSet<>();
        String sql = "SELECT * FROM friend_requests where receiver_email=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FriendRequest friendRequest = getNextFromSet(resultSet);
                friendRequests.add(friendRequest);
            }
            return friendRequests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequests;
    }
}