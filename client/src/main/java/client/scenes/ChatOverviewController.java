package client.scenes;

import client.MyApplication;
import client.utils.ChatUserBox;
import client.utils.MessageHandler;
import commons.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class ChatOverviewController extends OverviewParent{

    private ChatUserBox selectedUser;

    private MessageHandler messageHandler;

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
        messageHandler = new MessageHandler();

        server.registerForWebsocketMessages("/topic/message", Message.class, m -> {
            Platform.runLater(() -> handleWebsocketMessage(m));
        });
    }

    /**
     * Syncs the controller with the server.
     */
    public void sync(){
        loadChats();
        loadProfile();
    }

    /**
     * Sends a message
     */
    public void sendMessage(){
        try {
            String message = messageBox.getText();
            Message messageSaved = server.sendMessage(selectedUser.getChatId(),
                    loggedInUser.getUserName() , message);

            server.send("/topic/message", messageSaved);

            messageHandler.displayMessageWithTimestamp(this.messages,
                    messageSaved, this.loggedInUser);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Handles a click on a chat.
     * @param profileBox The clicked profile box.
     */
    public void clickOnChat(ChatUserBox profileBox){
        if(selectedUser != null) selectedUser.setStyle("-fx-background-color: null;");

        this.selectedUser = profileBox;

        selectedUser.setStyle("-fx-background-color: blue;");

        messages.getChildren().clear();

        List<Message> messageList = server.getMessagesOfChat(profileBox.getChatId());

        messageHandler.loadMessagesOfChat(messages, messageList, loggedInUser);
    }

    private void handleWebsocketMessage(Message message) {
        Long chatIdCurrentlyViewing = this.selectedUser.getChatId();

        if(!message.getChat().getId().equals(chatIdCurrentlyViewing)
            || message.getSender().equals(this.loggedInUser)) return;

        messageHandler.displayMessageWithTimestamp(this.messages, message, this.loggedInUser);
    }

}
