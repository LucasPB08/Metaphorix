package client.utils;

import com.google.inject.Inject;
import commons.ChatUser;
import commons.GroupMessage;
import commons.Message;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class GroupMessageHandler {
    private final TimeStampHandler timeStampHandler;

    private final static Insets HORIZONTAL_INSETS = new Insets(0, 0, 0, 5.0);
    private final static Insets VERTICAL_INSETS = new Insets(3.0);
    private final static Insets TIMESTAMP_INSETS = new Insets(8.0, 0, 0, 7.0);

    /**
     * Constructor
     * @param timeStampHandler Class that handles time stamps &
     *                         the display of the date of messages sent.
     */
    @Inject
    public GroupMessageHandler(TimeStampHandler timeStampHandler){
        this.timeStampHandler = timeStampHandler;
    }

    public void displayGroupMessage(VBox messages, GroupMessage message, ChatUser loggedInUser){
        Label messageLabel = new Label(message.getMessage());
        messageLabel.getStyleClass().add("content");

        Label timeSentLabel = timeStampHandler.getTimeSentLabel(message);
        HBox.setMargin(timeSentLabel, TIMESTAMP_INSETS);
        timeSentLabel.getStyleClass().add("timestamp");

        VBox messageBox = new VBox();
        messageBox.getStyleClass().add("messageBox");

        HBox contentTimestampBox = new HBox();
        contentTimestampBox.getChildren().addAll(messageLabel, timeSentLabel);

        messageBox.getChildren().add(contentTimestampBox);

        HBox messageLevel = new HBox();
        messageLevel.getChildren().add(messageBox);

        if (isReceiver(message, loggedInUser)) {
            messageLevel.setAlignment(Pos.BASELINE_LEFT);
            messageBox.getStyleClass().add("receiver");

            String senderUsername = message.getSender().getUserId().getUserName();
            Label senderLabel = new Label(senderUsername);

            // The name of the sender is only included
            // if the sender isn't the one logged-in
            messageBox.getChildren().add(0, senderLabel);

            // Slight offset for the content and timestamp
            // in comparison to the name of the sender
            VBox.setMargin(contentTimestampBox, HORIZONTAL_INSETS);
        } else {
            messageLevel.setAlignment(Pos.BASELINE_RIGHT);
            messageBox.getStyleClass().add("sender");
        }

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
                                   List<GroupMessage> messagesOfChat,
                                   ChatUser loggedInUser) {
        timeStampHandler.reset();

        for (GroupMessage message : messagesOfChat) {
            displayDate(messages, message);

            displayGroupMessage(messages, message, loggedInUser);
        }
    }

    private void displayDate(VBox messages, GroupMessage message){
        Label dateLabel = timeStampHandler.dateLabel(message);

        if(dateLabel == null) return;


        HBox dateBox = new HBox();
        dateBox.getChildren().add(dateLabel);

        HBox level = new HBox();
        level.setAlignment(Pos.CENTER);
        VBox.setMargin(level, VERTICAL_INSETS);

        level.getChildren().add(dateBox);

        messages.getChildren().add(level);
    }

    private boolean isReceiver(GroupMessage message, ChatUser loggedInUser){
        return !message.getSender().getUserId().getUserName().equals(loggedInUser.getUserName());
    }
}
