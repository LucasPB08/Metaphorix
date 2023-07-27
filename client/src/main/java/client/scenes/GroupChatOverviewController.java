package client.scenes;

import com.google.inject.Inject;
import com.sun.javafx.binding.StringFormatter;
import commons.ChatUser;
import commons.GroupChat;
import commons.GroupParticipant;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class GroupChatOverviewController {
    private MainCtrl mainCtrl;

    @FXML
    private Text title;

    @FXML
    private Text groupDescription;

    @FXML
    private VBox participantsList;

    @Inject
    public GroupChatOverviewController(MainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
    }

    public void setInfo(GroupChat groupChat, ChatUser loggedInUser){
        title.setText(groupChat.getGroupName());
        groupDescription.setText(groupChat.getGroupDescription());

        setParticipantsList(groupChat.getGroupParticipants(), loggedInUser);
    }

    private void setParticipantsList(List<GroupParticipant> participants,
                                     ChatUser loggedInUser){

        for(GroupParticipant participant: participants){
            String userNameToDisplay =participant.getUserId().getUserName();

            if(userNameToDisplay.equals(loggedInUser.getUserName()))
                userNameToDisplay += " (you)";

            userNameToDisplay = "- " + userNameToDisplay;

            Label userNameLabel = new Label(userNameToDisplay);

            // The use of an HBox now, so that later
            // buttons can be added next to the labels
            HBox userBox = new HBox();
            userBox.getChildren().add(userNameLabel);

            VBox.setMargin(userBox, new Insets(2.0, 0, 2.0, 2.0));

            participantsList.getChildren().add(userBox);
        }

    }

    public void back(){
        mainCtrl.showChatOverview();
    }

}
