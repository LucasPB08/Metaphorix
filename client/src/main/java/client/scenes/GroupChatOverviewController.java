package client.scenes;

import client.FXMLBuilder;
import client.MyApplication;
import client.exceptions.EntityNotFoundException;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.ChatUser;
import commons.GroupChat;
import commons.GroupParticipant;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;

public class GroupChatOverviewController {
    private MainCtrl mainCtrl;
    private ServerUtils server;

    private GroupChat displayedGroup;
    private ChatUser loggedInUser;

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

    @FXML
    private Button backButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addButton;

    /**
     * Constructor
     * @param mainCtrl The main controller of the application
     * @param server Class to communicate with the server
     */
    @Inject
    public GroupChatOverviewController(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Sets the information fields with the
     * appropriate information of the group.
     * @param groupChat The group chat being viewed.
     * @param loggedInUser The logged-in user.
     */
    public void setInfo(GroupChat groupChat, ChatUser loggedInUser){

        this.displayedGroup = groupChat;
        this.loggedInUser = loggedInUser;

        title.setText(groupChat.getGroupName());
        groupDescription.setText(groupChat.getGroupDescription());

        setParticipantsList();
    }

    /**
     * Saves the new description
     */
    public void saveDesc(){
        String newDesc = groupDescEditable.getText();

        groupDescription.setText(newDesc);

        groupDescEditable.setDisable(true);
        groupDescEditable.setVisible(false);

        this.backButton.setDisable(false);
        this.removeButton.setDisable(false);
        this.deleteButton.setDisable(false);
        this.addButton.setDisable(false);

        editDescButton.setOnAction(event -> editDesc());
        editDescButton.setText("EDIT");

        if(newDesc.equals(groupDescription.getText())) return;

        try{
            server.editGroupDescription(displayedGroup.getId(), newDesc);
        } catch(EntityNotFoundException e){
            e.printStackTrace();
        }

    }

    /**
     * Makes the description editable, and disables the other buttons.
     */
    public void editDesc(){
        groupDescEditable.setDisable(false);
        groupDescEditable.setVisible(true);

        this.backButton.setDisable(true);
        this.removeButton.setDisable(true);
        this.deleteButton.setDisable(true);
        this.addButton.setDisable(true);

        groupDescEditable.setText(groupDescription.getText());
        groupDescEditable.requestFocus();

        editDescButton.setOnAction(event -> saveDesc());
        editDescButton.setText("SAVE");
    }

    /**
     * Makes the participants removable,
     * also disables the other buttons.
     */
    public void setRemovableMode(){
        this.backButton.setDisable(true);
        this.editDescButton.setDisable(true);
        this.deleteButton.setDisable(true);
        this.addButton.setDisable(true);

        setRemovableParticipantsList();

        this.removeButton.setText("SAVE");
        this.removeButton.setOnAction(event -> saveParticipants());
    }

    private void saveParticipants(){
        this.backButton.setDisable(false);
        this.editDescButton.setDisable(false);
        this.deleteButton.setDisable(false);
        this.addButton.setDisable(false);

        setParticipantsList();

        this.removeButton.setText("REMOVE");
        this.removeButton.setOnAction(event -> setRemovableMode());
    }

    /**
     * Called when user presses the "BACK" button.
     * Goes back to the chat overview.
     */
    public void back(){
        mainCtrl.showChatOverview();
    }

    /**
     * Deletes the group being viewed.
     */
    public void deleteGroup(){
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText("Are you sure you want to delete: " +
                displayedGroup.getGroupName());

        Optional<ButtonType> button = confirmationAlert.showAndWait();

        if(button.isEmpty() || button.get().equals(ButtonType.CANCEL))
            return;

        try{
            server.deleteGroupChat(displayedGroup.getId());

            mainCtrl.showUserOverview();
        } catch (EntityNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Shows a dialog to add new participants to the group.
     */
    public void addParticipant(){
        Pair<AddParticipantController, Dialog<ButtonType>> pair =
                new FXMLBuilder(MyApplication.getInjector())
                        .buildDialogPane("scenes/add-participant-dialog.fxml");

        if(pair == null) return;

        AddParticipantController controller = pair.getKey();
        controller.setup(displayedGroup);

        Dialog<ButtonType> addDialog = pair.getValue();
        addDialog.setTitle("Available Users");

        Optional<ButtonType> buttonPressed = addDialog.showAndWait();

        if(buttonPressed.isEmpty() ||
                buttonPressed.get().equals(ButtonType.CANCEL))
            return;

        List<String> usersToAdd = controller.ok();
        if(usersToAdd.isEmpty()) return;

        try{

            this.displayedGroup = server.addParticipantsToGroup(displayedGroup.getId(), usersToAdd);
            setParticipantsList();

        } catch(EntityNotFoundException e){
            e.printStackTrace();
        }
    }

    private void setRemovableParticipantsList(){
        this.participantsList.getChildren().clear();

        List<GroupParticipant> participants = this.displayedGroup.getGroupParticipants();

        for(GroupParticipant participant: participants){
            String userNameToDisplay = participant.getUserId().getUserName();

            Button removeButton = new Button("X");
            removeButton.setOnAction(event -> removeParticipant(participant));
            removeButton.getStyleClass().add("remove-user-button");

            HBox.setMargin(removeButton, new Insets(0, 4.0, 0, 4.0));

            if(userNameToDisplay.equals(loggedInUser.getUserName())) {
                userNameToDisplay += " (you)";
                removeButton.setDisable(true);
            }

            Label userNameLabel = new Label(userNameToDisplay);

            // The use of an HBox now, so that later
            // buttons can be added next to the labels
            HBox userBox = new HBox();
            userBox.getChildren().addAll(removeButton, userNameLabel);

            VBox.setMargin(userBox, new Insets(2.0, 0, 2.0, 2.0));

            participantsList.getChildren().add(userBox);
        }
    }

    private void removeParticipant(GroupParticipant participant){
        try{
            Long groupId = this.displayedGroup.getId();
            String userName = participant.getUserId().getUserName();

            this.displayedGroup.getGroupParticipants().remove(participant);

            server.removeUserFromGroup(groupId, userName);

            setRemovableParticipantsList();
        } catch(EntityNotFoundException e){
            e.printStackTrace();
        }
    }

    private void setParticipantsList(){
        participantsList.getChildren().clear();

        List<GroupParticipant> participants = this.displayedGroup.getGroupParticipants();

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

}
