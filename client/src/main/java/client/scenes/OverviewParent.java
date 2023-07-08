package client.scenes;

import client.utils.ChatUserBox;
import client.utils.ServerUtils;
import commons.ChatUser;
import commons.Message;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

public class OverviewParent {
    protected MainCtrl mainCtrl;
    protected ServerUtils server;

    protected ChatUser loggedInUser;
    protected int profilePictureRadius = 45;

    protected ChatUserBox selectedUser;

    @FXML
    protected VBox messages;

    @FXML
    protected HBox chats;

    @FXML
    protected Pane userSection;

    protected void loadMessagesOfChat(Long chatId) {
        List<Message> messagesOfChat = server.getMessagesOfChat(chatId);

        for(Message message: messagesOfChat){
            String messageContent = message.getMessage();
            Label messageLabel = new Label(messageContent);

            HBox messageToView = new HBox();
            messageToView.getChildren().add(messageLabel);

            if(isReceiver(message)) messageToView.setAlignment(Pos.BASELINE_LEFT);
            else messageToView.setAlignment(Pos.BASELINE_RIGHT);

            this.messages.getChildren().add(messageToView);
        }
    }

    private boolean isReceiver(Message message){
        return !message.getSender().getUserName().equals(this.loggedInUser.getUserName());
    }

}
