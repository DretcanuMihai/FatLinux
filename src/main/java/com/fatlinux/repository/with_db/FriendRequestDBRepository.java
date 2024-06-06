package com.fatlinux.repository.with_db;

import com.fatlinux.model.entities.FriendRequest;
import com.fatlinux.model.entities.User;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.PageImplementation;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.skeletons.entity_based.FriendRequestRepositoryInterface;
import com.fatlinux.utils.structures.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * database repository for friend request
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
     * creates a database repository with an url, a username and a password
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
    public FriendRequest save(FriendRequest friendRequest) {
        FriendRequest toReturn = friendRequest;
        String sqlSave = "INSERT INTO friend_requests(sender_email, receiver_email, send_time) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementSave = connection.prepareStatement(sqlSave)) {

            statementSave.setString(1, friendRequest.getSender());
            statementSave.setString(2, friendRequest.getReceiver());
            statementSave.setTimestamp(3, Timestamp.valueOf(friendRequest.getSendTime()));
            statementSave.execute();
            toReturn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public FriendRequest delete(Pair<String, String> id) {
        FriendRequest toReturn = null;
        FriendRequest friendRequest = findOne(id);
        if (friendRequest == null)
            return null;
        String sqlDelete = "DELETE FROM friend_requests WHERE (sender_email = (?) AND receiver_email = (?))";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementDelete = connection.prepareStatement(sqlDelete)) {

            statementDelete.setString(1, id.getFirst());
            statementDelete.setString(2, id.getSecond());
            int rows = statementDelete.executeUpdate();
            if (rows != 0)
                toReturn = friendRequest;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public FriendRequest update(FriendRequest friendRequest) {
        FriendRequest toReturn = friendRequest;
        String sqlUpdate = "UPDATE friend_requests SET send_time=(?) WHERE sender_email = (?) AND receiver_email=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate)) {

            statementUpdate.setTimestamp(1, Timestamp.valueOf(friendRequest.getSendTime()));
            statementUpdate.setString(2, friendRequest.getSender());
            statementUpdate.setString(3, friendRequest.getReceiver());
            int affectedRows = statementUpdate.executeUpdate();
            if (affectedRows != 0)
                toReturn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Iterable<FriendRequest> getFriendRequestsSentToUser(String userEmail) {
        List<FriendRequest> friendRequests = new ArrayList<>();
        String sql = "SELECT * FROM friend_requests WHERE receiver_email = (?)";

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

    @Override
    public Page<FriendRequest> findAll(Pageable pageable) {
        List<FriendRequest> friendRequests = new ArrayList<>();
        String sql = "SELECT * FROM friend_requests OFFSET (?) LIMIT (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statement.setInt(1, start);
            statement.setInt(2, pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FriendRequest friendRequest = getNextFromSet(resultSet);
                friendRequests.add(friendRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable, friendRequests.stream());
    }

    @Override
    public Page<FriendRequest> getFriendRequestsSentToUser(String userEmail, Pageable pageable) {
        List<FriendRequest> friendRequests = new ArrayList<>();
        String sql = "SELECT * FROM friend_requests WHERE receiver_email = (?) OFFSET (?) LIMIT (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userEmail);
            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statement.setInt(2, start);
            statement.setInt(3, pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FriendRequest friendRequest = getNextFromSet(resultSet);
                friendRequests.add(friendRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable, friendRequests.stream());
    }

    @Override
    public Iterable<FriendRequest> getFriendRequestsSentByUser(String userEmail) {
        List<FriendRequest> friendRequests = new ArrayList<>();
        String sql = "SELECT * FROM friend_requests WHERE sender_email = (?)";

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

    @Override
    public Page<FriendRequest> getFriendRequestsSentByUser(String userEmail, Pageable pageable) {
        List<FriendRequest> friendRequests = new ArrayList<>();
        String sql = "SELECT * FROM friend_requests WHERE sender_email = (?) OFFSET (?) LIMIT (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userEmail);
            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statement.setInt(2, start);
            statement.setInt(3, pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FriendRequest friendRequest = getNextFromSet(resultSet);
                friendRequests.add(friendRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable, friendRequests.stream());
    }

    @Override
    public int getNewFriendRequestCount(User user) {
        int toReturn = 0;
        String sql = "SELECT count(*) FROM friend_requests WHERE receiver_email = (?) and send_time > (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getEmail());
            statement.setTimestamp(2, Timestamp.valueOf(user.getLastLoginTime()));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long l=resultSet.getLong(1);
            toReturn=l.intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
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
}
