package client.scenes;

import client.FXMLBuilder;
import client.MyApplication;
import client.utils.ChatUserBox;
import client.utils.HTTPException;
import commons.Chat;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;

public class UserOverviewController extends OverviewParent{

    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
        server = MyApplication.getServer();
    }

    /**
     * Loads the overview.
     */
    public void loadProfile(){
        ChatUserBox userToLoad = createProfileBox(loggedInUser.getUserName(), -1L);
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

    private void addUser(String userId){
        try {
            Long chatId = server.createChat(this.loggedInUser.getUserName(), userId);

            ChatUserBox pair = createProfileBox(userId, chatId);

            chats.getChildren().add(pair);
        } catch(HTTPException e){
            e.printStackTrace();
        }
    }

    private void loadChats(){
        List<Chat> userChats = server.getChatsOfUser(this.loggedInUser.getUserName());
        for(Chat chat: userChats){
            String initiator = chat.getInitiator().getUserName();
            String receiver = chat.getReceiver().getUserName();
            String myUserName = this.loggedInUser.getUserName();

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

}
