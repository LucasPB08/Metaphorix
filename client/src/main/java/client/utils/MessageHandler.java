package client.utils;

import com.google.inject.Inject;
import commons.ChatUser;
import commons.Message;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class MessageHandler {
    private TimeStampHandler timeStampHandler;

    private final static Insets HBOX_INSETS = new Insets(7.0);
    private final static Insets VERTICAL_INSETS = new Insets(3.0);
    private final static Insets TIMESTAMP_INSETS = new Insets(8.0, 0, 0, 7.0);

    @Inject
    public MessageHandler(TimeStampHandler timeStampHandler){
        this.timeStampHandler = timeStampHandler;
    }

    /**
     * Displays message with its timestamp in the client ui.
     * @param messages The container that holds the messages in the ui.
     * @param message The message to display.
     * @param loggedInUser The logged-in user.
     */
    public void displayMessageWithTimestamp(VBox messages, Message message, ChatUser loggedInUser){
        Label messageLabel = new Label(message.getMessage());
        messageLabel.getStyleClass().add("content");

        Label timeSentLabel = timeStampHandler.getTimeSentLabel(message);
        HBox.setMargin(timeSentLabel, TIMESTAMP_INSETS);
        timeSentLabel.getStyleClass().add("timestamp");

        HBox messageBox = new HBox();
        messageBox.getChildren().addAll(messageLabel, timeSentLabel);
        messageBox.getStyleClass().add("messageBox");

        HBox messageLevel = new HBox();
        messageLevel.getChildren().add(messageBox);

        if (isReceiver(message, loggedInUser)) {
            messageLevel.setAlignment(Pos.BASELINE_LEFT);
            messageBox.getStyleClass().add("receiver");
        } else {
            messageLevel.setAlignment(Pos.BASELINE_RIGHT);
            messageBox.getStyleClass().add("sender");
        }

        HBox.setMargin(messageBox, HBOX_INSETS);
        VBox.setMargin(messageLevel, VERTICAL_INSETS);

        messages.getChildren().add(messageLevel);
    }

    /**
     * Loads all the messages from a certain chat.
     * @param messages The container that holds the messages in the ui.
     * @param messagesOfChat All the messages from the chat to be loaded.
     * @param loggedInUser The logged-in user.
     */
    public void loadMessagesOfChat(VBox messages,
                                   List<Message> messagesOfChat,
                                   ChatUser loggedInUser) {
        for (Message message : messagesOfChat) {
            displayMessageWithTimestamp(messages, message, loggedInUser);
        }
    }

    private boolean isReceiver(Message message, ChatUser loggedInUser){
        return !message.getSender().getUserName().equals(loggedInUser.getUserName());
    }

}
