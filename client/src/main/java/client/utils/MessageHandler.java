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

        HBox messageToView = new HBox();
        messageToView.setAlignment(Pos.BASELINE_RIGHT);
        messageToView.getChildren().add(textToSend);

        messages.getChildren().add(messageToView);
    }

    public void loadMessagesOfChat(VBox messages, List<Message> messagesOfChat, ChatUser loggedInUser) {

        for (Message message : messagesOfChat) {
            String messageContent = message.getMessage();
            Label messageLabel = new Label(messageContent);
            messageLabel.setFont(FONT_SIZE);

            HBox messageToView = new HBox();
            messageToView.getChildren().add(messageLabel);

            if (isReceiver(message, loggedInUser)) {
                messageToView.setAlignment(Pos.BASELINE_LEFT);
                messageLabel.setBackground(new Background(new BackgroundFill(COLOR_RECEIVER, RADII, BACKGROUND_INSETS)));
            } else {
                messageToView.setAlignment(Pos.BASELINE_RIGHT);
                messageLabel.setBackground(new Background(new BackgroundFill(COLOR_SENDER, RADII, BACKGROUND_INSETS)));
            }

            HBox.setMargin(messageLabel, HBOX_INSETS);

            messages.getChildren().add(messageToView);
        }

    }

    public void loadWebsocketMessage(VBox messages, Message message){
        Label messageLabel = new Label(message.getMessage());
        processLabel(messageLabel);

        HBox messageToView = new HBox();
        messageToView.setAlignment(Pos.BASELINE_LEFT);
        messageToView.getChildren().add(messageLabel);

        messages.getChildren().add(messageToView);
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
