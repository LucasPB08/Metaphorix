package client.scenes;

import client.exceptions.EntityNotFoundException;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.sun.javafx.binding.StringFormatter;
import commons.ChatUser;
import commons.GroupChat;
import commons.GroupChatDTO;
import commons.GroupParticipant;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class GroupChatOverviewController {
    private MainCtrl mainCtrl;
    private ServerUtils server;

    private GroupChat displayedGroup;

    @FXML
    private Text title;

    @FXML
    private Text groupDescription;

    @FXML
    private VBox participantsList;

    @FXML
    private TextArea groupDescEditable;

    @FXML
    private Button editDescButton;

    @Inject
    public GroupChatOverviewController(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setInfo(GroupChat groupChat, ChatUser loggedInUser){
        participantsList.getChildren().clear();

        this.displayedGroup = groupChat;

        title.setText(groupChat.getGroupName());
        groupDescription.setText(groupChat.getGroupDescription());

        setParticipantsList(groupChat.getGroupParticipants(), loggedInUser);
    }

    public void saveDesc(){
        String newDesc = groupDescEditable.getText();

        try{
            server.editGroupDescription(displayedGroup.getId(), newDesc);
        } catch(EntityNotFoundException e){
            e.printStackTrace();
        }

        groupDescription.setText(newDesc);

        groupDescEditable.setDisable(true);
        groupDescEditable.setVisible(false);

        editDescButton.setOnAction(event -> editDesc());
        editDescButton.setText("EDIT");
    }

    public void editDesc(){
        groupDescEditable.setDisable(false);
        groupDescEditable.setVisible(true);

        groupDescEditable.setText(groupDescription.getText());
        groupDescEditable.requestFocus();

        editDescButton.setOnAction(event -> saveDesc());
        editDescButton.setText("SAVE");
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
