package com.map_toysocialnetworkgui.repository.with_db;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;
import com.map_toysocialnetworkgui.repository.skeletons.CRUDRepository;
import com.map_toysocialnetworkgui.utils.structures.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Repository in database for friend request
 */
public class FriendRequestDBCRUDRepository implements CRUDRepository<Pair<String, String>, FriendRequest> {
    private final String url;
    private final String username;
    private final String password;

    /**
     * Constructor
     * @param url - url of database
     * @param username - name of database
     * @param password - password of database
     */
    public FriendRequestDBCRUDRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void save(FriendRequest friendRequest) {
        String sqlSave = "INSERT INTO friend_requests(sender_email, receiver_email, send_time) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementSave = connection.prepareStatement(sqlSave)) {

            statementSave.setString(1, friendRequest.getSender());
            statementSave.setString(2, friendRequest.getReceiver());
            statementSave.setTimestamp(3, Timestamp.valueOf(friendRequest.getSendTime()));
            statementSave.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FriendRequest tryGet(Pair<String, String> id) {
        String sqlFind = "SELECT * FROM friend_requests WHERE (sender_email = (?) AND receiver_email = (?))";
        FriendRequest toReturn = null;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementFind = connection.prepareStatement(sqlFind)) {

            statementFind.setString(1, id.getFirst());
            statementFind.setString(2, id.getSecond());
            ResultSet result=statementFind.executeQuery();
            if(result.next()) {
                String email1 = result.getString("sender_email");
                String email2 = result.getString("receiver_email");
                LocalDateTime sendTime = result.getTimestamp("send_time").toLocalDateTime();
                toReturn = new FriendRequest(email1, email2, sendTime);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public void update(FriendRequest friendRequest) {
        String sqlUpdate = "UPDATE friend_requests SET send_time = (?) WHERE (sender_email = (?) AND receiver_email = (?))";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate)) {

            statementUpdate.setTimestamp(1, Timestamp.valueOf(friendRequest.getSendTime()));
            statementUpdate.setString(2, friendRequest.getSender());
            statementUpdate.setString(3, friendRequest.getReceiver());
            statementUpdate.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Pair<String, String> id) {
        String sqlDelete = "DELETE FROM friend_requests WHERE (sender_email = (?) AND receiver_email = (?))";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementDelete = connection.prepareStatement(sqlDelete)) {

            statementDelete.setString(1, id.getFirst());
            statementDelete.setString(2, id.getSecond());
            statementDelete.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<FriendRequest> getAll() {
        Set<FriendRequest> friendRequsets = new HashSet<>();
        String sql = "SELECT * FROM friend_requests";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String email1 = resultSet.getString("sender_email");
                String email2 = resultSet.getString("receiver_email");
                LocalDateTime sendDate = resultSet.getTimestamp("send_time").toLocalDateTime();
                FriendRequest friendRequest = new FriendRequest(email1, email2, sendDate);
                friendRequsets.add(friendRequest);
            }
            return friendRequsets;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequsets;
    }
}