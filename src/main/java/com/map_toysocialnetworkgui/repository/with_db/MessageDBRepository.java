package com.map_toysocialnetworkgui.repository.with_db;

import com.map_toysocialnetworkgui.model.entities.Message;
import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.skeletons.AbstractDBRepository;
import com.map_toysocialnetworkgui.repository.skeletons.operation_based.CreateOperationRepository;
import com.map_toysocialnetworkgui.repository.skeletons.operation_based.DeleteOperationRepository;
import com.map_toysocialnetworkgui.repository.skeletons.operation_based.ReadOperationRepository;
import com.map_toysocialnetworkgui.service.AdministrationException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * a message repository that works with a database
 */
public class MessageDBRepository extends AbstractDBRepository implements CreateOperationRepository<Integer, Message>,
        ReadOperationRepository<Integer, Message>, DeleteOperationRepository<Integer, Message> {

    /**
     * constructor
     *
     * @param url      - url of database
     * @param username - username of database
     * @param password - password of database
     */
    public MessageDBRepository(String url, String username, String password) {
        super(url, username, password);
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
        try (Connection connection = getConnection();
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

    @Override
    public void save(Message message) {
        String sqlSave = "INSERT INTO messages(sender_email, message_text, send_time, parent_message_id) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statementInsertMessage = connection.prepareStatement(sqlSave, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // saves everything except the list of receivers
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try (Connection connection = getConnection();
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

    @Override
    public Message tryGet(Integer id) {
        String sqlFind = "SELECT * FROM messages WHERE message_id = (?)";
        Message message = null;
        try (Connection connection = getConnection();
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
    public void delete(Integer id) {
        String sqlMessages = "DELETE FROM messages WHERE message_id = (?)";
        try (Connection connection = getConnection();
             PreparedStatement statementMessages = connection.prepareStatement(sqlMessages)) {

            statementMessages.setInt(1, id);
            statementMessages.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Message> getAll() {
        Set<Message> messages = new HashSet<>();
        String sqlMessages = "SELECT * FROM messages";
        try (Connection connection = getConnection();
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

    /**
     * gets a list of all the messages between two users sorted chronologically
     *
     * @param userEmail1 - first user email
     * @param userEmail2 - second user email
     * @return said list of messages
     */
    public List<Message> getMessagesBetweenUsersChronologically(String userEmail1, String userEmail2) {
        List<Message> conversation = new ArrayList<>();
        String sqlFilterConversationByTime = "SELECT m.message_id, m.sender_email, m.message_text, m.send_time, m.parent_message_id\n" +
                "FROM messages m INNER JOIN message_deliveries md ON m.message_id = md.message_id\n" +
                "WHERE m.sender_email = (?) AND md.receiver_email = (?)\n" +
                "\t  OR m.sender_email = (?) AND md.receiver_email = (?)\n" +
                "ORDER BY send_time ASC";
        try (Connection connection = getConnection();
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
}
