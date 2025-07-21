package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Service.AccountService;
import Service.MessageService;
import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class SocialMediaController {

    AccountService accountService = new AccountService(new AccountDAO());
    MessageService messageService = new MessageService(new MessageDAO());

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // Javalin object has post and get methods that take a path string and
        // a handler that accepts the context using this::handler function syntax

        // Example endpoint defined by project
        app.get("/example-endpoint", this::exampleHandler);

        // User endpoints
        app.post("/register", this::handleRegister); 
        app.post("/login", this::handleLogin);

        // Message endpoints
        app.post("/messages", this::handleCreateMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.delete("/messages/{message_id}", this::handleDeleteMessage);
        app.patch("/messages/{message_id}", this::handleUpdateMessage);
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByUser);

        return app;
    }

    private void exampleHandler(Context context) {
        // Example method defined by the project
        context.json("sample text");
    }

    private void handleRegister(Context context) {
        Account account = context.bodyAsClass(Account.class); // Turns Context into a Account class obj
        Account registered = accountService.register(account); // could return null

        if (registered != null) {
            context.json(registered); // Return Json obj to user
        } else { // Account registration not valid
            context.status(400); // HTTP Bad request Code
        }
    }

    private void handleLogin(Context context) {
        Account credentials = context.bodyAsClass(Account.class);
        Account result = accountService.login(credentials);

        if (result != null) {
            context.json(result);
        } else {
            context.status(401); // HTTP Unauthorized Code
        }
    }

    private void handleCreateMessage(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Message created = messageService.createMessage(message);

        if (created != null) {
            context.json(created);
        } else {
            context.status(400); // HTTP Bad request Code
        }
    }

    private void handleGetAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void handleGetMessageById(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            context.json(message);
        } else {
            context.json(""); // Empty if not found
        }
    }

    private void handleDeleteMessage(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deleted = messageService.deleteMessage(messageId);

        if (deleted != null) {
            context.json(deleted);
        } else {
            context.json(""); // Still return 200, empty if not found
        }
    }

    private void handleUpdateMessage(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message updateRequest = context.bodyAsClass(Message.class);
        Message updated = messageService.updateMessage(messageId, updateRequest.getMessage_text());

        if (updated != null) {
            context.json(updated);
        } else {
            context.status(400);
        }
    }

    private void handleGetMessagesByUser(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUser(accountId);
        context.json(messages);
    }
}
