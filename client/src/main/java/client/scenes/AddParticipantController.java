package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.ChatUser;
import commons.GroupChat;
import commons.GroupParticipant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.List;

public class AddParticipantController {
    private ServerUtils server;

    @FXML
    private ListView<String> availableUsers;

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        availableUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Constructor
     * @param server The server to communicate with
     */
    @Inject
    public AddParticipantController(ServerUtils server){
        this.server = server;
    }

    /**
     * Sets up the listview to only display users that
     * are not yet part of the group chat.
     * @param groupChat The group chat to add users to.
     */
    public void setup(GroupChat groupChat){
        List<GroupParticipant> participantList = groupChat.getGroupParticipants();

        List<String> usersInGroup = participantList.stream()
                .map(participant -> participant.getUserId().getUserName()).toList();

        List<String> usersNotInGroup = server.getUsers().stream()
                .map(ChatUser::getUserName)
                .filter(user -> !usersInGroup.contains(user))
                .toList();

        availableUsers.setItems(FXCollections.observableList(usersNotInGroup));
    }

    /**
     * Used to get the selected users.
     * @return The selected users.
     */
    public List<String> ok(){
        return availableUsers.getSelectionModel().getSelectedItems();
    }
}
