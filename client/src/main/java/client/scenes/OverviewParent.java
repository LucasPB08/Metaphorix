package client.scenes;

import client.utils.ChatUserBox;
import client.utils.ServerUtils;
import commons.Chat;
import commons.ChatUser;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.List;

public class OverviewParent {
    protected MainCtrl mainCtrl;
    protected ServerUtils server;

    protected ChatUser loggedInUser;
    protected int profilePictureRadius = 45;

    @FXML
    protected HBox chats;

    @FXML
    protected Pane userSection;

    /**
     * Loads the overview.
     */
    public void loadProfile(){
        ChatUserBox userToLoad = createProfileBox(loggedInUser.getUserName(), -1L);
        userSection.getChildren().add(userToLoad);

        loadChats();
    }

    /**
     * Sets the user whose overview will be shown.
     * @param loggedInUser
     */
    public void setLoggedInUser(ChatUser loggedInUser){
        this.loggedInUser = loggedInUser;
    }

    void loadChats(){
        this.chats.getChildren().clear();
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

    ChatUserBox createProfileBox(String user, Long chatId){
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

            //Check whether the clicked user is the logged-in user
            if(profileBox.getChatId() == -1){
                mainCtrl.showUserOverview();
                return;
            }

            mainCtrl.clickOnChat(profileBox);
        });
    }

}
