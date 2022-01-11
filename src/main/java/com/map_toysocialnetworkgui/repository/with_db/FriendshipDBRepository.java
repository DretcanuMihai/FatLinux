package com.map_toysocialnetworkgui.repository.with_db;

import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.PageImplementation;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.FriendshipRepositoryInterface;
import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * database repository for friendship
 */
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
     * creates a database repository with an url, a username and a password
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

    @Override
    public Friendship findOne(UnorderedPair<String> id) {
        Friendship toReturn = null;
        String sqlFind = "SELECT * FROM friendships WHERE (first_user_email = (?) AND second_user_email = (?))";

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
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        String sql = "SELECT * FROM friendships";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Friendship friendship = getNextFromSet(resultSet);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship friendship) {
        Friendship toReturn = friendship;
        String sqlSave = "INSERT INTO friendships(first_user_email, second_user_email, begin_date) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementSave = connection.prepareStatement(sqlSave)) {

            statementSave.setString(1, friendship.getEmails().getFirst());
            statementSave.setString(2, friendship.getEmails().getSecond());
            statementSave.setTimestamp(3, Timestamp.valueOf(friendship.getBeginDate()));
            statementSave.execute();
            toReturn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Friendship delete(UnorderedPair<String> id) {
        Friendship toReturn = null;
        Friendship friendship = findOne(id);
        if (friendship == null)
            return null;
        String sqlDelete = "DELETE FROM friendships WHERE (first_user_email = (?) AND second_user_email = (?))";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementDelete = connection.prepareStatement(sqlDelete)) {

            statementDelete.setString(1, id.getFirst());
            statementDelete.setString(2, id.getSecond());
            int rows = statementDelete.executeUpdate();
            if (rows != 0)
                toReturn = friendship;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Friendship update(Friendship friendship) {
        Friendship toReturn = friendship;
        String sqlUpdate = "UPDATE friendships SET begin_date = (?) WHERE first_user_email = (?) AND second_user_email = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate)) {

            statementUpdate.setTimestamp(1, Timestamp.valueOf(friendship.getBeginDate()));
            statementUpdate.setString(2, friendship.getEmails().getFirst());
            statementUpdate.setString(3, friendship.getEmails().getSecond());
            int affectedRows = statementUpdate.executeUpdate();
            if (affectedRows != 0)
                toReturn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Iterable<Friendship> getUserFriendships(String userEmail) {
        List<Friendship> friendships = new ArrayList<>();
        String sql = "SELECT * FROM friendships WHERE first_user_email = (?) OR second_user_email = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userEmail);
            statement.setString(2, userEmail);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Friendship friendship = getNextFromSet(resultSet);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Iterable<Friendship> getUserFriendshipsFromMonth(String userEmail, int month) {
        List<Friendship> friendships = new ArrayList<>();
        String sql = """
                SELECT * FROM friendships WHERE ((first_user_email = (?) OR second_user_email = (?))
                AND EXTRACT (MONTH FROM begin_date) = (?))
                """;

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

    @Override
    public Page<Friendship> findAll(Pageable pageable) {
        List<Friendship> friendships = new ArrayList<>();
        String sql = "SELECT * FROM friendships OFFSET (?) LIMIT (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statement.setInt(1, start);
            statement.setInt(2, pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Friendship friendship = getNextFromSet(resultSet);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable, friendships.stream());
    }

    @Override
    public Page<Friendship> getUserFriendships(String userEmail, Pageable pageable) {
        List<Friendship> friendships = new ArrayList<>();
        String sql = """
                SELECT * FROM friendships WHERE first_user_email = (?) OR second_user_email = (?)
                OFFSET (?) LIMIT (?)
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userEmail);
            statement.setString(2, userEmail);
            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statement.setInt(3, start);
            statement.setInt(4, pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Friendship friendship = getNextFromSet(resultSet);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable, friendships.stream());
    }

    @Override
    public Page<Friendship> getUserFriendshipsFromMonth(String userEmail, int month, Pageable pageable) {
        List<Friendship> friendships = new ArrayList<>();
        String sql = """
                SELECT * FROM friendships WHERE ((first_user_email = (?) OR second_user_email = (?))
                AND EXTRACT(MONTH FROM begin_date) = (?)) OFFSET (?) LIMIT (?)
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userEmail);
            statement.setString(2, userEmail);
            statement.setInt(3, month);
            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statement.setInt(4, start);
            statement.setInt(5, pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Friendship friendship = getNextFromSet(resultSet);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable, friendships.stream());
    }

    @Override
    public Page<Friendship> getUserFriendshipsFromInterval(String userEmail, LocalDate begin, LocalDate end, Pageable pageable) {
        List<Friendship> friendships = new ArrayList<>();
        String sql = """
                SELECT * FROM friendships WHERE ((first_user_email = (?) OR second_user_email = (?))
                AND ((?)<= begin_date AND begin_date<=(?))) OFFSET (?) LIMIT (?)
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userEmail);
            statement.setString(2, userEmail);
            statement.setTimestamp(3, Timestamp.valueOf(begin.atStartOfDay()));
            statement.setTimestamp(4, Timestamp.valueOf(end.atTime(23,59)));
            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statement.setInt(5, start);
            statement.setInt(6, pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Friendship friendship = getNextFromSet(resultSet);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable, friendships.stream());
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
        LocalDateTime beginDate = resultSet.getTimestamp("begin_date").toLocalDateTime();
        return new Friendship(email1, email2, beginDate);
    }
}
