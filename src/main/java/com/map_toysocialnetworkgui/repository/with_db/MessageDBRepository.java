package com.map_toysocialnetworkgui.repository.with_db;

import com.map_toysocialnetworkgui.model.entities.Message;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.PageImplementation;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.MessageRepositoryInterface;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * a message repository that works with a database
 */
public class MessageDBRepository implements MessageRepositoryInterface {

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
    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message findOne(Integer id) {
        String sqlFind = "SELECT * FROM messages WHERE message_id = (?)";
        Message message = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementGetMessage = connection.prepareStatement(sqlFind)) {

            statementGetMessage.setInt(1, id);
            ResultSet resultSetMessages = statementGetMessage.executeQuery();
            if (resultSetMessages.next()) {
                message = getNextFromSet(resultSetMessages);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        String sqlMessages = "SELECT * FROM messages";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementMessages = connection.prepareStatement(sqlMessages)) {

            ResultSet resultSetMessages = statementMessages.executeQuery();
            while (resultSetMessages.next()) {
                Message message = getNextFromSet(resultSetMessages);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message message) {
        Message toReturn = message;
        String sqlSave = "INSERT INTO messages(sender_email, message_text, send_time, parent_message_id) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementInsertMessage = connection.prepareStatement(sqlSave, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statementInsertMessage.setString(1, message.getFromEmail());
            statementInsertMessage.setString(2, message.getMessageText());
            statementInsertMessage.setTimestamp(3, Timestamp.valueOf(message.getSendTime()));
            if (message.getParentMessageId() != null)
                statementInsertMessage.setInt(4, message.getParentMessageId());
            else
                statementInsertMessage.setNull(4, Types.INTEGER);

            statementInsertMessage.executeUpdate();

            // saves the list of receivers
            int id = getMessageIDGeneratedBy(statementInsertMessage);
            message.getToEmails().forEach(email -> saveDelivery(id, email));
            message.setId(id);
            toReturn = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Message delete(Integer id) {
        Message toReturn = null;
        Message message=findOne(id);
        if(message==null)
            return null;
        String sqlMessages = "DELETE FROM messages WHERE message_id = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementMessages = connection.prepareStatement(sqlMessages)) {

            statementMessages.setInt(1, id);
            int rows = statementMessages.executeUpdate();
            if(rows != 0)
                toReturn=message;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Message update(Message message) {
        Message toReturn=message;
        String sqlUpdateMessage="UPDATE messages set sender_email=(?),message_text=(?),send_time=(?),parent_message_id=(?) where message_id=(?)";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            PreparedStatement statementUpdateMessage = connection.prepareStatement(sqlUpdateMessage);

            statementUpdateMessage.setString(1,message.getFromEmail());
            statementUpdateMessage.setString(2,message.getMessageText());
            statementUpdateMessage.setTimestamp(3,Timestamp.valueOf(message.getSendTime()));
            if(message.getParentMessageId()==null)
                statementUpdateMessage.setNull(4,Types.INTEGER);
            else
                statementUpdateMessage.setInt(4,message.getParentMessageId());

            statementUpdateMessage.setInt(5,message.getId());
            int rows=statementUpdateMessage.executeUpdate();
            updateDeliveriesOf(message);
            if(rows!=0)
                toReturn=null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Iterable<Message> getMessagesBetweenUsersChronologically(String userEmail1, String userEmail2) {
        List<Message> conversation = new ArrayList<>();
        String sqlFilterConversationByTime = """
                SELECT m.message_id, m.sender_email, m.message_text, m.send_time, m.parent_message_id
                FROM messages m INNER JOIN message_deliveries md\s
                ON m.message_id = md.message_id\s
                WHERE ((m.sender_email = (?) AND md.receiver_email = (?)) OR (m.sender_email = (?) AND md.receiver_email = (?)))
                ORDER BY send_time""";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementConversation = connection.prepareStatement(sqlFilterConversationByTime)) {

            statementConversation.setString(1, userEmail1);
            statementConversation.setString(2, userEmail2);
            statementConversation.setString(3, userEmail2);
            statementConversation.setString(4, userEmail1);
            ResultSet resultSet = statementConversation.executeQuery();
            while (resultSet.next()) {
                Message message = getNextFromSet(resultSet);
                conversation.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversation;
    }

    @Override
    public Page<Message> findAll(Pageable pageable) {
        Set<Message> messages = new HashSet<>();
        String sql = "SELECT * FROM messages offset (?) limit (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int pageSize=pageable.getPageSize();
            int pageNr=pageable.getPageNumber();
            int start=(pageNr-1)*pageSize;
            statement.setInt(1,start);
            statement.setInt(2,pageSize);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Message message = getNextFromSet(resultSet);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable,messages.stream());
    }

    @Override
    public Page<Message> getMessagesBetweenUsersChronologically(String userEmail1, String userEmail2, Pageable pageable) {
        Set<Message> messages = new HashSet<>();
        String sql = """
                SELECT m.message_id, m.sender_email, m.message_text, m.send_time, m.parent_message_id
                FROM messages m INNER JOIN message_deliveries md
                ON m.message_id = md.message_id
                WHERE ((m.sender_email = (?) AND md.receiver_email = (?)) OR (m.sender_email = (?) AND md.receiver_email = (?)))
                ORDER BY send_time
                OFFSET (?)
                LIMIT (?)
                """;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int pageSize=pageable.getPageSize();
            int pageNr=pageable.getPageNumber();
            int start=(pageNr-1)*pageSize;
            statement.setString(1, userEmail1);
            statement.setString(2, userEmail2);
            statement.setString(3, userEmail2);
            statement.setString(4, userEmail1);
            statement.setInt(5,start);
            statement.setInt(6,pageSize);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Message message = getNextFromSet(resultSet);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable,messages.stream());
    }

    @Override
    public Iterable<Message> getMessagesSentToUserChronologically(String userEmail) {
        List<Message> conversation = new ArrayList<>();
        String sqlFilterConversationByTime = """
                SELECT m.message_id, m.sender_email, m.message_text, m.send_time, m.parent_message_id
                FROM messages m INNER JOIN message_deliveries md
                ON m.message_id = md.message_id
                WHERE md.receiver_email = (?)
                ORDER BY send_time""";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementConversation = connection.prepareStatement(sqlFilterConversationByTime)) {

            statementConversation.setString(1, userEmail);
            ResultSet resultSet = statementConversation.executeQuery();
            while (resultSet.next()) {
                Message message = getNextFromSet(resultSet);
                conversation.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversation;
    }

    @Override
    public Page<Message> getMessagesSentToUserChronologically(String userEmail, Pageable pageable) {
        List<Message> conversation = new ArrayList<>();
        String sqlFilterConversationByTime = """
                SELECT m.message_id, m.sender_email, m.message_text, m.send_time, m.parent_message_id
                FROM messages m INNER JOIN message_deliveries md
                ON m.message_id = md.message_id
                WHERE md.receiver_email = (?)
                ORDER BY send_time
                offset (?) limit (?)""";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementConversation = connection.prepareStatement(sqlFilterConversationByTime)) {

            statementConversation.setString(1, userEmail);
            int pageSize=pageable.getPageSize();
            int pageNr=pageable.getPageNumber();
            int start=(pageNr-1)*pageSize;
            statementConversation.setInt(2,start);
            statementConversation.setInt(3,pageSize);
            ResultSet resultSet = statementConversation.executeQuery();
            while (resultSet.next()) {
                Message message = getNextFromSet(resultSet);
                conversation.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable,conversation.stream());
    }

    @Override
    public Iterable<Message> getMessagesSentByUserChronologically(String userEmail) {
        List<Message> conversation = new ArrayList<>();
        String sqlFilterConversationByTime = """
                SELECT m.message_id, m.sender_email, m.message_text, m.send_time, m.parent_message_id
                FROM messages m
                WHERE m.sender_email = (?)
                ORDER BY send_time""";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementConversation = connection.prepareStatement(sqlFilterConversationByTime)) {

            statementConversation.setString(1, userEmail);
            ResultSet resultSet = statementConversation.executeQuery();
            while (resultSet.next()) {
                Message message = getNextFromSet(resultSet);
                conversation.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversation;
    }

    @Override
    public Page<Message> getMessagesSentByUserChronologically(String userEmail, Pageable pageable) {
        List<Message> conversation = new ArrayList<>();
        String sqlFilterConversationByTime = """
                SELECT m.message_id, m.sender_email, m.message_text, m.send_time, m.parent_message_id
                FROM messages m
                WHERE m.sender_email = (?)
                ORDER BY send_time
                offset (?) limit (?)
                """;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementConversation = connection.prepareStatement(sqlFilterConversationByTime)) {

            statementConversation.setString(1, userEmail);
            int pageSize=pageable.getPageSize();
            int pageNr=pageable.getPageNumber();
            int start=(pageNr-1)*pageSize;
            statementConversation.setInt(2,start);
            statementConversation.setInt(3,pageSize);
            ResultSet resultSet = statementConversation.executeQuery();
            while (resultSet.next()) {
                Message message = getNextFromSet(resultSet);
                conversation.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageImplementation<>(pageable,conversation.stream());
    }

    /**
     * extracts the current message from a ResultSet
     *
     * @param resultSet - said ResultSet
     * @return - said message
     * @throws SQLException - if any sql problems are present
     */
    private Message getNextFromSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("message_id");
        String fromEmail = resultSet.getString("sender_email");
        if (resultSet.wasNull())
            fromEmail = null;
        List<String> toEmails = getReceiverEmailsOf(id);
        String messageText = resultSet.getString("message_text");
        LocalDateTime sendTime = resultSet.getTimestamp("send_time").toLocalDateTime();
        Integer repliedMessageId = resultSet.getInt("parent_message_id");
        if (resultSet.wasNull())
            repliedMessageId = null;
        return new Message(id, fromEmail, toEmails, messageText, sendTime, repliedMessageId);
    }

    /**
     * saves a delivery into the database
     *
     * @param messageID     - the delivered message's ID (must exist in the DB)
     * @param receiverEmail - the receiver's email (must exist in the DB)
     */
    private void saveDelivery(Integer messageID, String receiverEmail) {
        String sqlInsertDelivery = "INSERT INTO message_deliveries(message_id, receiver_email) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementInsertDelivery = connection.prepareStatement(sqlInsertDelivery)) {

            statementInsertDelivery.setInt(1, messageID);
            statementInsertDelivery.setString(2, receiverEmail);
            statementInsertDelivery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the message id generated by a given message insertion statement
     *
     * @param statement - the insertion statement
     * @return - int representing the id
     * @throws SQLException - if there are any SQL problems
     */
    private int getMessageIDGeneratedBy(PreparedStatement statement) throws SQLException {
        ResultSet newKeys = statement.getGeneratedKeys();
        newKeys.next();
        return newKeys.getInt(1);
    }

    /**
     * gets the emails of the people who received the message with an id
     *
     * @param id - said id
     * @return - a list of said emails
     */
    private List<String> getReceiverEmailsOf(Integer id) {
        List<String> toEmails = new ArrayList<>();
        String sqlGetMessageDeliveries = "SELECT * FROM message_deliveries WHERE message_id = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementGetMessageDeliveries = connection.prepareStatement(sqlGetMessageDeliveries)) {

            statementGetMessageDeliveries.setInt(1, id);
            ResultSet resultSetDeliveries = statementGetMessageDeliveries.executeQuery();
            while (resultSetDeliveries.next()) {
                String toEmail = resultSetDeliveries.getString("receiver_email");
                toEmails.add(toEmail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toEmails;
    }

    /**
     * deletes all the deliveries of a given message
     * @param id - the message's id
     */
    private void deleteDeliveriesOf(Integer id){
        String sqlDeleteMessageDeliveries="DELETE from message_deliveries where message_id=(?)";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            PreparedStatement statementDeleteMessageDeliveries= connection.prepareStatement(sqlDeleteMessageDeliveries);
            statementDeleteMessageDeliveries.setInt(1,id);
            statementDeleteMessageDeliveries.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * updates the deliveries of a message in the database
     * @param message - said message - the new deliveries are taken from it
     */
    private void updateDeliveriesOf(Message message){
        deleteDeliveriesOf(message.getId());
        message.getToEmails().forEach(email-> saveDelivery(message.getId(),email));
    }
}
