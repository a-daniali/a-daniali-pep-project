package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    // Post new Message
    public Message createMessage(Message message) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, message.getPosted_by());
            statement.setString(2, message.getMessage_text());
            statement.setLong(3, message.getTime_posted_epoch());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 1) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    message.setMessage_id(rs.getInt(1));
                    return message;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Get all messages
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    // Get message by ID
    public Message getMessageById(int messageId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, messageId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Delete a message by its Id
    public Message deleteMessage(int messageId) {
        Message toDelete = getMessageById(messageId);
        if (toDelete == null) return null;

        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, messageId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toDelete;
    }

    // Post for a message update
    public Message updateMessage(int messageId, String newText) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newText);
            statement.setInt(2, messageId);

            int affected = statement.executeUpdate();
            if (affected == 1) { // We dont return if multiple affected as check 
                return getMessageById(messageId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Get all messages by an account Id
    public List<Message> getMessagesByUser(int accountId) {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
