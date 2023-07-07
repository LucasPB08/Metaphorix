package client.scenes;

import client.FXMLBuilder;
import client.MyApplication;
import client.utils.ChatUserBox;
import client.utils.HTTPException;
import client.utils.ServerUtils;
import commons.Chat;
import commons.ChatUser;
import commons.Message;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;


public class UserOverviewController{
    private MainCtrl mainCtrl;
    private ServerUtils server;

    private ChatUser user;

    private int profilePictureRadius = 45;

    private ChatUserBox selectedUser;

    @FXML
    private TextField messageBox;

    @FXML
    private HBox chats;

    @FXML
    private Pane userSection;

    @FXML
    private VBox messages;

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
        server = MyApplication.getServer();
    }

    /**
     * Sets the user whose overview will be shown.
     * @param user
     */
    public void setUser(ChatUser user){
        this.user = user;
    }

    /**
     * Loads the overview.
     */
    public void loadProfile(){
        ChatUserBox userToLoad = createProfileBox(user.getUserName(), -1L);
        userSection.getChildren().add(userToLoad);

        loadChats();
    }


    /**
     * Adds a chat
     */
    public void addChat(){
        Pair<AddChatsCtrl, Dialog<ButtonType>> pair = new FXMLBuilder()
                .buildDialogPane("scenes/add-user-dialog.fxml");
        if(pair == null) return;

        Dialog<ButtonType> dialog = pair.getValue();
        dialog.setTitle("Available users");
        Optional<ButtonType> pressed = dialog.showAndWait();

        if(pressed.isEmpty()) return;

        ButtonType type = pressed.get();
        AddChatsCtrl controller = pair.getKey();

        if(type == ButtonType.OK) {
            String userIdSelected  = controller.getSelected();

            if(userIdSelected != null)
                addUser(userIdSelected);
        }
    }

    /**
     * Sends a message
     */
    public void sendMessage(){
        String message = messageBox.getText();
        try {
            server.sendMessage(selectedUser.getChatId(),user.getUserName() , message);
        } catch(Exception e){
            e.printStackTrace();
        }

        Text textToSend = new Text(message);

        this.messages.getChildren().add(textToSend);
    }

    private void addUser(String userId){
        try {
            Long chatId = server.createChat(this.user.getUserName(), userId);

            ChatUserBox pair = createProfileBox(userId, chatId);

            chats.getChildren().add(pair);
        } catch(HTTPException e){
            e.printStackTrace();
        }
    }

    private void loadChats(){
        List<Chat> userChats = server.getChatsOfUser(this.user.getUserName());
        for(Chat chat: userChats){
            String initiator = chat.getInitiator().getUserName();
            String receiver = chat.getReceiver().getUserName();
            String myUserName = this.user.getUserName();

            // if the initiator's username equals the username of the user that
            // is logged in, then we should create the profile box with the receiver's
            // username, and vice versa.

            ChatUserBox toAdd = initiator.equals(myUserName) ?
                    createProfileBox(receiver, chat.getId()):
                    createProfileBox(initiator, chat.getId());

            chats.getChildren().add(toAdd);
        }
    }

    private ChatUserBox createProfileBox(String user, Long chatId){
        ChatUserBox profileBox = new ChatUserBox(chatId);

        Circle profilePicture = new Circle();
        profilePicture.setRadius(profilePictureRadius);
        profilePicture.setFill(loadImage());

        profileBox.getChildren().addAll(profilePicture, new Text(user));

        makeSelectable(profileBox);
        return profileBox;
    }

    private ImagePattern loadImage(){
        Image pic = new Image("images/profile-pic.jpg");

        return new ImagePattern(pic);
    }

    private void makeSelectable(ChatUserBox profileBox) {
        profileBox.setOnMouseClicked(event -> {
            if(selectedUser != null) selectedUser.setStyle("-fx-background-color: null;");

            this.selectedUser = profileBox;
            selectedUser.setStyle("-fx-background-color: blue;");

            messages.getChildren().clear();
            loadMessagesOfChat(profileBox.getChatId());
        });
    }

    private void loadMessagesOfChat(Long chatId) {
        List<Message> messagesOfChat = server.getMessagesOfChat(chatId);

        for(Message message: messagesOfChat){
            String messageContent = message.getMessage();
            Text messageText = new Text(messageContent);

            this.messages.getChildren().add(messageText);
        }
    }

}
