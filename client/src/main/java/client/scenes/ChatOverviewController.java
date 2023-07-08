package client.scenes;

import client.MyApplication;
import commons.ChatUser;
import commons.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class ChatOverviewController extends OverviewParent{

    @FXML
    private TextField messageBox;

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
        server = MyApplication.getServer();

        server.registerForWebsocketMessages("/topic/message", Message.class, m -> {
            Platform.runLater(() -> handleWebsocketMessage(m));
        });
    }


    /**
     * Sets the user whose overview will be shown.
     * @param loggedInUser
     */
    public void setLoggedInUser(ChatUser loggedInUser){
        this.loggedInUser = loggedInUser;
    }

    /**
     * Sends a message
     */
    public void sendMessage(){
        String message = messageBox.getText();
        try {
            Message messageSaved = server.sendMessage(selectedUser.getChatId(),
                    loggedInUser.getUserName() , message);
            server.send("/topic/message", messageSaved);
        } catch(Exception e){
            e.printStackTrace();
        }

        Label textToSend = new Label(message);

        HBox messageToView = new HBox();
        messageToView.setAlignment(Pos.BASELINE_RIGHT);
        messageToView.getChildren().add(textToSend);

        this.messages.getChildren().add(messageToView);
    }

    private void handleWebsocketMessage(Message message) {
        Long chatIdCurrentlyViewing = this.selectedUser.getChatId();

        if(!message.getChat().getId().equals(chatIdCurrentlyViewing)
            || message.getSender().equals(this.loggedInUser)) return;

        HBox messageToView = new HBox();
        messageToView.setAlignment(Pos.BASELINE_LEFT);

        Label messageLabel = new Label(message.getMessage());
        messageToView.getChildren().add(messageLabel);

        this.messages.getChildren().add(messageToView);
    }


}
