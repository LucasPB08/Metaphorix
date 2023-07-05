package client.scenes;

import client.MyApplication;
import client.utils.ChatUserBox;
import client.utils.HTTPException;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import server.commons.Chat;
import server.commons.ChatUser;

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

    private void loadChats(){
        List<Chat> userChats = server.getChatsOfUser(this.user.getUserName());
        for(Chat chat: userChats){
            String initiator = chat.getInitiator().getUserName();
            String receiver = chat.getReceiver().getUserName();
            String myUserName = this.user.getUserName();

            if (initiator.equals(myUserName)) {
                addUser(receiver);
            } else {
                addUser(initiator);
            }
        }

    }

    /**
     * Adds a chat
     */
    public void addChat(){
        Pair<AddChatsCtrl, Dialog<ButtonType>> pair = makeDialog();
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
        try {
            String message = messageBox.getText();

            server.sendMessage(selectedUser.getChatId(),user.getUserName() , message);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private Pair<AddChatsCtrl ,Dialog<ButtonType>> makeDialog(){
        try {
            FXMLLoader loader = new FXMLLoader(MyApplication.class.
                    getResource("scenes/add-user-dialog.fxml"));

            DialogPane pane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);

            return new Pair<>(loader.getController(), dialog);
        } catch(Exception e){
            System.err.println("Something went wrong");
            e.printStackTrace();
            return null;
        }
    }

    private void addUser(String userId){
        try {
            Long chatId = server.createChat(this.user.getUserName(), userId);

            ChatUserBox pair = createProfileBox(userId, chatId);
            makeSelectable(pair);

            chats.getChildren().add(pair);
        } catch(HTTPException e){
            e.printStackTrace();
        }
    }

    private ChatUserBox createProfileBox(String user, Long chatId){
        ChatUserBox profileBox = new ChatUserBox(chatId);

        Circle profilePicture = new Circle();
        profilePicture.setRadius(profilePictureRadius);
        profilePicture.setFill(loadImage());

        profileBox.getChildren().addAll(profilePicture, new Text(user));

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
        });
    }
}
