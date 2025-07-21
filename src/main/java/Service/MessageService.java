package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = new AccountDAO(); 
    }

    // 3: Our API should be able to process the creation of new messages.
    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()) return null;
        if (message.getMessage_text().length() > 255) return null;

        if (accountDAO.getAccountByUsernameId(message.getPosted_by()) == null) {
            return null; // posted_by is invalid
        }

        return messageDAO.createMessage(message);
    }
    // 4: Our API should be able to retrieve all messages.

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // 5: Our API should be able to retrieve a message by its ID.

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    // 6: Our API should be able to delete a message identified by a message ID.

    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    // 7: Our API should be able to update a message text identified by a message ID.
    public Message updateMessage(int messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return null;
        }

        Message existing = messageDAO.getMessageById(messageId);
        if (existing == null) {
            return null;
        }

        return messageDAO.updateMessage(messageId, newText);
    }
    // 8: Our API should be able to retrieve all messages written by a particular user.
    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }
}
