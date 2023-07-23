package client.scenes;

import client.utils.ChatUserBox;
import client.utils.ServerUtils;
import commons.Chat;
import commons.ChatUser;
import commons.GroupChatDTO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
        ChatUserBox userToLoad = createProfileBox(loggedInUser.getUserName(), -1L, false);
        userSection.getChildren().add(userToLoad);
    }

    /**
     * Sets the user whose overview will be shown.
     * @param loggedInUser
     */
    public void setLoggedInUser(ChatUser loggedInUser){
        this.loggedInUser = loggedInUser;
    }

    /**
     * Loads the chat section of the UI with the chats that this user
     * takes part in.
     */
    public void loadChats(){
        this.chats.getChildren().clear();

        String userName = this.loggedInUser.getUserName();

        List<Chat> userChats = server.getChatsOfUser(userName);
        List<GroupChatDTO> groupChats = server.getGroupChatsOfUser(userName);

        for(Chat chat: userChats){
            String initiator = chat.getInitiator().getUserName();
            String receiver = chat.getReceiver().getUserName();

            // if the initiator's username equals the username of the user that
            // is logged in, then we should create the profile box with the receiver's
            // username, and vice versa.

            ChatUserBox toAdd = initiator.equals(userName) ?
                    createProfileBox(receiver, chat.getId(), false):
                    createProfileBox(initiator, chat.getId(), false);

            chats.getChildren().add(toAdd);
        }

        for(GroupChatDTO groupChat: groupChats){
            ChatUserBox box = createProfileBox(groupChat.getGroupName(), groupChat.getId(), true);

            chats.getChildren().add(box);
        }
    }

    ChatUserBox createProfileBox(String user, Long chatId, boolean isGroupChat){
        ChatUserBox profileBox = new ChatUserBox(chatId);
        HBox.setMargin(profileBox, new Insets(0, 2.0, 0, 2.0));

        Circle profilePicture = new Circle();
        profilePicture.setRadius(profilePictureRadius);
        profilePicture.setFill(loadImage(isGroupChat));

        profileBox.getChildren().addAll(profilePicture, new Text(user));

        makeSelectable(profileBox);
        return profileBox;
    }

    private ImagePattern loadImage(boolean isGroupChat){
        Image pic = isGroupChat ?
                new Image("images/group-icon.jpg") : new Image("images/profile-pic.jpg");

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
