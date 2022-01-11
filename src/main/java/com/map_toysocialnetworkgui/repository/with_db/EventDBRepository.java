package com.map_toysocialnetworkgui.repository.with_db;

import com.map_toysocialnetworkgui.model.entities.Event;
import com.map_toysocialnetworkgui.model.entities.Message;
import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.PageImplementation;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.EventRepositoryInterface;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class EventDBRepository implements EventRepositoryInterface {
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
    public EventDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Event findOne(Integer id) throws IllegalArgumentException {
        Event event = null;
        String sqlFind = "SELECT * FROM events WHERE event_id = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementGetEvent = connection.prepareStatement(sqlFind)) {

            statementGetEvent.setInt(1, id);
            ResultSet resultSetEvents = statementGetEvent.executeQuery();
            if (resultSetEvents.next()) {
                event = getNextFromSet(resultSetEvents);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return event;
    }

    @Override
    public Iterable<Event> findAll() {
        Set<Event> events = new HashSet<>();
        String sqlEvents = "SELECT * FROM events";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementEvents = connection.prepareStatement(sqlEvents)) {

            ResultSet resultSet = statementEvents.executeQuery();
            while (resultSet.next()) {
                Event event = getNextFromSet(resultSet);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        Set<Event> events = new HashSet<>();
        String sqlEvents = "SELECT * FROM events OFFSET (?) LIMIT (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sqlEvents)) {

            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statement.setInt(1, start);
            statement.setInt(2, pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Event event = getNextFromSet(resultSet);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable,events.stream());
    }

    @Override
    public Event save(Event event) {
        Event toReturn = event;
        String sqlSave = """
                INSERT INTO events(title, description, host_email, date)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementInsert = connection.prepareStatement(sqlSave, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statementInsert.setString(1, event.getTitle());
            statementInsert.setString(2, event.getDescription());
            statementInsert.setString(3, event.getHostEmail());
            statementInsert.setTimestamp(4, Timestamp.valueOf(event.getDate()));
            statementInsert.executeUpdate();

            // Saves the list of receivers
            int id = getEventIDGeneratedBy(statementInsert);
            event.getAttendees().forEach(email -> saveAttendance(id, email));
            event.setId(id);
            toReturn = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Event delete(Integer id) {
        Event toReturn = null;
        Event event = findOne(id);
        if (event == null)
            return null;
        String sqlMessages = "DELETE FROM events WHERE event_id = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementEvents = connection.prepareStatement(sqlMessages)) {

            statementEvents.setInt(1, id);
            int rows = statementEvents.executeUpdate();
            if (rows != 0)
                toReturn = event;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Event update(Event event) {
        Event toReturn = event;
        String sqlUpdateMessage = """
                UPDATE events SET title = (?), description = (?), host_email = (?), date= (?)
                WHERE event_id = (?)
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statementUpdateMessage = connection.prepareStatement(sqlUpdateMessage);

            statementUpdateMessage.setString(1, event.getTitle());
            statementUpdateMessage.setString(2, event.getDescription());
            statementUpdateMessage.setString(3, event.getHostEmail());
            statementUpdateMessage.setTimestamp(4, Timestamp.valueOf(event.getDate()));
            statementUpdateMessage.setInt(5, event.getId());
            int rows = statementUpdateMessage.executeUpdate();
            updateAttendancesOf(event);
            if (rows != 0)
                toReturn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Iterable<Event> getUserNotificationEvents(String email) {
        Collection<Event> events = new ArrayList<>();
        String sqlEvents = """
                SELECT e.event_id, e.title, e.description, e.host_email, e.date
                FROM events e inner join attendances a on e.event_id = a.event_id
                WHERE a.user_email = (?) and e.date >= now()
                ORDER BY e.date DESC
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementEvents = connection.prepareStatement(sqlEvents)) {

            statementEvents.setString(1,email);
            ResultSet resultSet = statementEvents.executeQuery();
            while (resultSet.next()) {
                Event event = getNextFromSet(resultSet);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public Page<Event> getUserNotificationEvents(String email, Pageable pageable) {
        Collection<Event> events = new ArrayList<>();
        String sqlEvents = """
                SELECT e.event_id, e.title, e.description, e.host_email, e.date
                FROM events e inner join attendances a on e.event_id = a.event_id
                WHERE a.user_email = (?) and e.date >= now()
                ORDER BY e.date DESC
                OFFSET (?) LIMIT (?)
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementEvents = connection.prepareStatement(sqlEvents)) {

            statementEvents.setString(1,email);
            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statementEvents.setInt(2, start);
            statementEvents.setInt(3, pageSize);
            ResultSet resultSet = statementEvents.executeQuery();
            while (resultSet.next()) {
                Event event = getNextFromSet(resultSet);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable,events.stream());
    }

    @Override
    public Iterable<Event> getUsersEventsDesc(String email) {
        Collection<Event> events = new ArrayList<>();
        String sqlEvents = """
                SELECT e.event_id, e.title, e.description, e.host_email, e.date
                FROM events e inner join attendances a on e.event_id = a.event_id
                WHERE a.user_email = (?)
                ORDER BY e.date DESC
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementEvents = connection.prepareStatement(sqlEvents)) {

            statementEvents.setString(1,email);
            ResultSet resultSet = statementEvents.executeQuery();
            while (resultSet.next()) {
                Event event = getNextFromSet(resultSet);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public Page<Event> getUsersEventsDesc(String email, Pageable pageable) {
        Collection<Event> events = new ArrayList<>();
        String sqlEvents = """
                SELECT e.event_id, e.title, e.description, e.host_email, e.date
                FROM events e inner join attendances a on e.event_id = a.event_id
                WHERE a.user_email = (?)
                ORDER BY e.date DESC
                OFFSET (?) LIMIT (?)
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementEvents = connection.prepareStatement(sqlEvents)) {

            statementEvents.setString(1,email);
            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statementEvents.setInt(2, start);
            statementEvents.setInt(3, pageSize);
            ResultSet resultSet = statementEvents.executeQuery();
            while (resultSet.next()) {
                Event event = getNextFromSet(resultSet);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable,events.stream());
    }

    @Override
    public Iterable<Event> getEventsFilter(String string) {
        Collection<Event> events = new ArrayList<>();
        String sqlEvents = """
                SELECT *
                FROM events
                WHERE title like '%' || (?) || '%' or description like '%' || (?) || '%'
                ORDER BY date DESC
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementEvents = connection.prepareStatement(sqlEvents)) {

            statementEvents.setString(1, string);
            statementEvents.setString(2, string);
            ResultSet resultSet = statementEvents.executeQuery();
            while (resultSet.next()) {
                Event event = getNextFromSet(resultSet);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public Page<Event> getEventsFilter(String string, Pageable pageable) {
        Collection<Event> events = new ArrayList<>();
        String sqlEvents = """
                SELECT *
                FROM events
                WHERE title like '%' || (?) || '%' or description like '%' || (?) || '%'
                ORDER BY date DESC
                OFFSET (?) LIMIT (?)
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementEvents = connection.prepareStatement(sqlEvents)) {

            statementEvents.setString(1, string);
            statementEvents.setString(2, string);
            int pageSize = pageable.getPageSize();
            int pageNr = pageable.getPageNumber();
            int start = (pageNr - 1) * pageSize;
            statementEvents.setInt(3, start);
            statementEvents.setInt(4, pageSize);
            ResultSet resultSet = statementEvents.executeQuery();
            while (resultSet.next()) {
                Event event = getNextFromSet(resultSet);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable,events.stream());
    }

    @Override
    public int getNumberOfNotification(User user) {
        int toReturn=0;
        String sqlEvents = """
                SELECT count()
                FROM events e inner join attendances a on e.event_id = a.event_id
                WHERE a.user_email = (?) and e.date >= now() and e.date >= (?)
                """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementEvents = connection.prepareStatement(sqlEvents)) {

            statementEvents.setString(1,user.getEmail());
            statementEvents.setDate(2, Date.valueOf(user.getLastLoginTime().toLocalDate()));
            ResultSet resultSet = statementEvents.executeQuery();
            Long l=resultSet.getLong(1);
            toReturn=l.intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * extracts the current event from a ResultSet
     *
     * @param resultSet - said ResultSet
     * @return - said event
     * @throws SQLException - if any sql problems are present
     */
    private Event getNextFromSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("event_id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        String hostEmail = resultSet.getString("host_email");
        List<String> attendeesEmails = getAttendeeEmailsOf(id);
        LocalDateTime date=resultSet.getTimestamp("date").toLocalDateTime();
        return new Event(id,title,description,hostEmail,attendeesEmails,date);
    }

    /**
     * gets the emails of the people who attend an event
     *
     * @param id - said id
     * @return - a list of said emails
     */
    private List<String> getAttendeeEmailsOf(Integer id) {
        List<String> emails = new ArrayList<>();
        String sqlGetAttendeesEmails = "SELECT * FROM attendances WHERE event_id = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementGetAttendeesEmails = connection.prepareStatement(sqlGetAttendeesEmails)) {

            statementGetAttendeesEmails.setInt(1, id);
            ResultSet resultSet = statementGetAttendeesEmails.executeQuery();
            while (resultSet.next()) {
                String email = resultSet.getString("user_email");
                emails.add(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emails;
    }

    /**
     * gets the message id generated by a given event insertion statement
     *
     * @param statement - the insertion statement
     * @return - int representing the id
     * @throws SQLException - if there are any SQL problems
     */
    private int getEventIDGeneratedBy(PreparedStatement statement) throws SQLException {
        ResultSet newKeys = statement.getGeneratedKeys();
        newKeys.next();
        return newKeys.getInt(1);
    }

    /**
     * saves an attendance into the database
     *
     * @param eventID     - the event ID
     * @param attendeeEmail - the attendee's email
     */
    private void saveAttendance(Integer eventID, String attendeeEmail) {
        String sqlInsertDelivery = "INSERT INTO attendances(event_id, user_email) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementInsertDelivery = connection.prepareStatement(sqlInsertDelivery)) {

            statementInsertDelivery.setInt(1, eventID);
            statementInsertDelivery.setString(2, attendeeEmail);
            statementInsertDelivery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * updates the attendances of an event in the database
     *
     * @param event - said event - the new deliveries are taken from it
     */
    private void updateAttendancesOf(Event event) {
        deleteAttendancesOf(event.getId());
        event.getAttendees().forEach(email -> saveAttendance(event.getId(), email));
    }

    /**
     * deletes all the attendances of a given event
     *
     * @param id - the event's id
     */
    private void deleteAttendancesOf(Integer id) {
        String sqlDeleteMessageDeliveries = "DELETE from attendances WHERE event_id = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            PreparedStatement statementDeleteMessageDeliveries = connection.prepareStatement(sqlDeleteMessageDeliveries);
            statementDeleteMessageDeliveries.setInt(1, id);
            statementDeleteMessageDeliveries.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
