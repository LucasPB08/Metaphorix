package client.scenes;

import client.MyApplication;
import client.utils.ChatUserBox;
import commons.ChatUser;
import commons.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChatOverviewController extends OverviewParent{

    protected ChatUserBox selectedUser;

    @FXML
    protected VBox messages;

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

    public void sync(){
        loadChats();
        loadProfile();
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

    public void clickOnChat(ChatUserBox profileBox){
        if(selectedUser != null) selectedUser.setStyle("-fx-background-color: null;");

        this.selectedUser = profileBox;

        selectedUser.setStyle("-fx-background-color: blue;");

        messages.getChildren().clear();
        loadMessagesOfChat(profileBox.getChatId());
    }

    private void loadMessagesOfChat(Long chatId) {
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


    private boolean isReceiver(Message message){
        return !message.getSender().getUserName().equals(this.loggedInUser.getUserName());
    }


}
