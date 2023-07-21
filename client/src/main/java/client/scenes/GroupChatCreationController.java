package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.ChatUser;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class GroupChatCreationController {

    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField groupName;

    @FXML
    private TextArea groupDescription;

    @FXML
    private ListView<String> availableUsers;

    @Inject
    public GroupChatCreationController(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    public void initialize(){
        String loggedInUserId = mainCtrl.loggedInUser().getUserName();

        List<String> users = server.getUsers().stream()
                .map(ChatUser::getUserName)
                .filter(x -> !x.equals(loggedInUserId)).toList();

        availableUsers.setItems(FXCollections.observableList(users));
    }

    public void cancel(){
        groupName.clear();
        groupDescription.clear();
    }


}
