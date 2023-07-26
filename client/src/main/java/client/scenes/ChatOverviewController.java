package client.scenes;

import client.utils.ChatBox;
import client.utils.GroupMessageHandler;
import client.utils.MessageHandler;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.GroupMessage;
import commons.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.util.List;

public class ChatOverviewController extends OverviewParent{

    private ChatBox selectedChat;

    private MessageHandler messageHandler;

    private GroupMessageHandler groupMessageHandler;

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
                                  ServerUtils server,
                                  MessageHandler messageHandler,
                                  GroupMessageHandler groupMessageHandler){
        this.messageHandler = messageHandler;
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.groupMessageHandler = groupMessageHandler;
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

        server.registerForWebsocketMessages("/topic/message", Message.class,
                m -> Platform.runLater(() -> handleWebsocketMessage(m)));

        server.registerForWebsocketMessages("/topic/group-message", GroupMessage.class,
                m -> Platform.runLater(() -> handleWebsocketGroupMessage(m)));
    }

    /**
     * Syncs the controller with the server.
     */
    public void sync(){
        loadChats();
        loadProfile();
    }

    /**
     * Is triggered when the SEND button is clicked.
     * Checks whether the clicked chat is a chat between two users,
     * or if it's a group chat.
     * This stores the message in the database, and displays it in the UI
     */
    public void sendMessage(){
        if(this.selectedChat.isGroupChat())
            sendGroupMessage();
        else
            sendPersonalMessage();

        this.messageBox.clear();
    }

    private void sendPersonalMessage(){
        try {
            String message = messageBox.getText();
            Message messageSaved = server.sendMessage(selectedChat.getChatId(),
                    loggedInUser.getUserName() , message);

            server.send("/topic/message", messageSaved);

            messageHandler.displayMessageWithTimestamp(this.messages,
                    messageSaved, this.loggedInUser);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void sendGroupMessage(){
        try {
            String message = messageBox.getText();
            GroupMessage messageSaved = server.sendGroupMessage(selectedChat.getChatId(),
                    loggedInUser.getUserName() , message);

            server.send("/topic/group-message", messageSaved);

            groupMessageHandler.displayGroupMessage(this.messages,
                    messageSaved, this.loggedInUser);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Loads the messages of a chat, once the user clicks on it.
     * @param profileBox The clicked profile box.
     */
    public void clickOnChat(ChatBox profileBox){
        if(selectedChat != null) selectedChat.setStyle("-fx-background-color: null;");

        this.selectedChat = profileBox;

        selectedChat.setStyle("-fx-background-color: blue;");

        messages.getChildren().clear();

        if(!profileBox.isGroupChat()) {
            List<Message> messageList = server.getMessagesOfChat(profileBox.getChatId());
            messageHandler.loadMessagesOfChat(messages, messageList, loggedInUser);
        } else {
            List<GroupMessage> messageList = server.getGroupChatMessages(profileBox.getChatId());
            groupMessageHandler.loadMessagesOfChat(messages, messageList, loggedInUser);
        }
    }

    private void handleWebsocketMessage(Message message) {
        Long chatIdCurrentlyViewing = this.selectedChat.getChatId();

        if(!message.getChat().getId().equals(chatIdCurrentlyViewing)
            || message.getSender().equals(this.loggedInUser)) return;

        messageHandler.displayMessageWithTimestamp(this.messages, message, this.loggedInUser);
    }

    private void handleWebsocketGroupMessage(GroupMessage message) {
        Long chatIdCurrentlyViewing = this.selectedChat.getChatId();

        Long chatIdOfMessage = message.getGroupChat().getId();

        if(!chatIdOfMessage.equals(chatIdCurrentlyViewing)
                || message.getSender().getUserId().equals(this.loggedInUser)) return;

        groupMessageHandler.displayGroupMessage(this.messages, message, this.loggedInUser);
    }

}
