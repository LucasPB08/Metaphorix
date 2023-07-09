package client.utils;

import commons.ChatUser;
import commons.Message;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.springframework.stereotype.Component;

import java.util.List;

public class MessageHandler {
    private final static Color COLOR_RECEIVER = Color.AQUA;
    private final static Color COLOR_SENDER = Color.RED;
    private final static Insets BACKGROUND_INSETS = new Insets(-4.0);
    private final static CornerRadii RADII = new CornerRadii(7.0);
    private final static Insets HBOX_INSETS = new Insets(7.0);
    private final static Font FONT_SIZE = new Font(15.0);

    public void displayMessageSent(VBox messages, String message){
        Label textToSend = new Label(message);
        processLabel(textToSend);

        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.BASELINE_RIGHT);
        messageBox.getChildren().add(textToSend);

        messages.getChildren().add(messageBox);
    }

    public void loadMessagesOfChat(VBox messages, List<Message> messagesOfChat, ChatUser loggedInUser) {

        for (Message message : messagesOfChat) {
            String messageContent = message.getMessage();
            Label messageLabel = new Label(messageContent);
            messageLabel.setFont(FONT_SIZE);

            HBox messageBox = new HBox();
            messageBox.getChildren().add(messageLabel);

            if (isReceiver(message, loggedInUser)) {
                messageBox.setAlignment(Pos.BASELINE_LEFT);
                messageLabel.setBackground(new Background(new BackgroundFill(COLOR_RECEIVER, RADII, BACKGROUND_INSETS)));
            } else {
                messageBox.setAlignment(Pos.BASELINE_RIGHT);
                messageLabel.setBackground(new Background(new BackgroundFill(COLOR_SENDER, RADII, BACKGROUND_INSETS)));
            }

            HBox.setMargin(messageLabel, HBOX_INSETS);

            messages.getChildren().add(messageBox);
        }

    }

    public void loadWebsocketMessage(VBox messages, Message message){
        Label messageLabel = new Label(message.getMessage());
        processLabel(messageLabel);

        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.BASELINE_LEFT);
        messageBox.getChildren().add(messageLabel);

        messages.getChildren().add(messageBox);
    }

    private void processLabel(Label messageLabel){
        messageLabel.setBackground(new Background(new BackgroundFill(COLOR_RECEIVER, RADII, BACKGROUND_INSETS)));
        HBox.setMargin(messageLabel, HBOX_INSETS);

        messageLabel.setFont(FONT_SIZE);
    }

    private boolean isReceiver(Message message, ChatUser loggedInUser){
        return !message.getSender().getUserName().equals(loggedInUser.getUserName());
    }

}
