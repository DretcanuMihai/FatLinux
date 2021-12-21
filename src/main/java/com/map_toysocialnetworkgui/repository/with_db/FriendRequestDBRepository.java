package com.map_toysocialnetworkgui.repository.with_db;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;
import com.map_toysocialnetworkgui.repository.CRUDException;
import com.map_toysocialnetworkgui.repository.skeletons.AbstractDBRepository;
import com.map_toysocialnetworkgui.repository.skeletons.operation_based.CreateOperationRepository;
import com.map_toysocialnetworkgui.repository.skeletons.operation_based.DeleteOperationRepository;
import com.map_toysocialnetworkgui.repository.skeletons.operation_based.ReadOperationRepository;
import com.map_toysocialnetworkgui.utils.structures.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Repository in database for friend request
 */
public class FriendRequestDBRepository extends AbstractDBRepository
        implements CreateOperationRepository<Pair<String, String>, FriendRequest>,
        DeleteOperationRepository<Pair<String, String>, FriendRequest>,
        ReadOperationRepository<Pair<String, String>, FriendRequest> {

    public FriendRequestDBRepository(String url, String username, String password) {
        super(url, username, password);
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
    public void save(FriendRequest friendRequest) throws CRUDException {
        if (contains(friendRequest.getId()))
            throw new CRUDException("Error: a friend request already exists from the sender to the receiver;\n");
        String sqlSave = "INSERT INTO friend_requests(sender_email, receiver_email, send_time) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
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

        try (Connection connection = getConnection();
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
    public void delete(Pair<String, String> id) throws CRUDException {
        String sqlDelete = "DELETE FROM friend_requests WHERE (sender_email = (?) AND receiver_email = (?))";
        try (Connection connection = getConnection();
             PreparedStatement statementDelete = connection.prepareStatement(sqlDelete)) {

            statementDelete.setString(1, id.getFirst());
            statementDelete.setString(2, id.getSecond());
            int rows = statementDelete.executeUpdate();
            if (rows == 0)
                throw new CRUDException("Error: a friend request with the given " +
                        "sender and receiver doesn't exist\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<FriendRequest> getAll() {
        Set<FriendRequest> friendRequests = new HashSet<>();
        String sql = "SELECT * FROM friend_requests";

        try (Connection connection = getConnection();
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

    /**
     * gets all friend requests of a user as a collection
     *
     * @param userEmail -> said user's emails
     * @return a collection of said friend requests
     */
    public Collection<FriendRequest> getFriendRequestsSentToUser(String userEmail) {
        Set<FriendRequest> friendRequests = new HashSet<>();
        String sql = "SELECT * FROM friend_requests where receiver_email=(?)";

        try (Connection connection = getConnection();
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