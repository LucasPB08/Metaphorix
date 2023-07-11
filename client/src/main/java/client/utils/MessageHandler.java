package client.utils;

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
    private final static Color COLOR_RECEIVER = Color.AQUA;
    private final static Color COLOR_SENDER = Color.RED;
    private final static Insets BACKGROUND_INSETS = new Insets(-4.0);
    private final static CornerRadii RADII = new CornerRadii(7.0);

    private final static Insets HBOX_INSETS = new Insets(7.0);
    private final static Insets VERTICAL_INSETS = new Insets(3.0);
    private final static Insets TIMESTAMP_INSETS = new Insets(8.0, 0, 0, 7.0);

    private final static Font FONT_SIZE_TEXT = new Font(15.0);
    private final static Font FONT_SIZE_TIMESTAMP = new Font(10.0);

    /**
     * Displays message with its timestamp in the client ui.
     * @param messages The container that holds the messages in the ui.
     * @param message The message to display.
     * @param loggedInUser The logged-in user.
     */
    public void displayMessageWithTimestamp(VBox messages, Message message, ChatUser loggedInUser){
        Label messageLabel = new Label(message.getMessage());
        messageLabel.setFont(FONT_SIZE_TEXT);
        messageLabel.getStyleClass().add("content");

        Timestamp timeSent = message.getTimestampSent();
        Label timeSentLabel = getTimeSentLabel(timeSent);
        timeSentLabel.setFont(FONT_SIZE_TIMESTAMP);
        HBox.setMargin(timeSentLabel, TIMESTAMP_INSETS);
        timeSentLabel.getStyleClass().add("timestamp");

        HBox messageBox = new HBox();
        messageBox.getChildren().addAll(messageLabel, timeSentLabel);
        messageBox.getStyleClass().add("messageBox");

        HBox messageLevel = new HBox();
        messageLevel.getChildren().add(messageBox);

        if (isReceiver(message, loggedInUser)) {
            messageLevel.setAlignment(Pos.BASELINE_LEFT);
            messageBox.setBackground(new Background(
                    new BackgroundFill(COLOR_RECEIVER, RADII, BACKGROUND_INSETS))
            );
        } else {
            messageLevel.setAlignment(Pos.BASELINE_RIGHT);
            messageBox.setBackground(new Background(
                    new BackgroundFill(COLOR_SENDER, RADII, BACKGROUND_INSETS))
            );
        }

        HBox.setMargin(messageBox, HBOX_INSETS);
        VBox.setMargin(messageLevel, VERTICAL_INSETS);

        messages.getChildren().add(messageLevel);
    }

    private Label getTimeSentLabel(Timestamp timestamp){
        LocalDateTime timeSent = timestamp.toLocalDateTime();

        int hour = timeSent.getHour();
        int minute = timeSent.getMinute();

        return new Label(hour + ":" + minute);
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
