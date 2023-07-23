package client.scenes;

import client.utils.ChatBox;
import client.utils.MessageHandler;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.util.List;

public class ChatOverviewController extends OverviewParent{

    private ChatBox selectedUser;

    private MessageHandler messageHandler;

    @FXML
    protected VBox messages;

    @FXML
    private TextField messageBox;

    /**
     * Constructor
     * @param mainCtrl The main controller of the client
     * @param server The server to communicate with
     * @param messageHandler Class that handles the creation of messages.
     */
    @Inject
    public ChatOverviewController(MainCtrl mainCtrl,
                                  ServerUtils server, MessageHandler messageHandler){
        this.messageHandler = messageHandler;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        messageBox.setOnKeyPressed(event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                sendMessage();
            }
        } );

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
    public void clickOnChat(ChatBox profileBox){
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
