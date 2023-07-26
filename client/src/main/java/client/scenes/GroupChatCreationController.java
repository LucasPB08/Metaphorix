package client.scenes;

import client.exceptions.CreatorNotFoundException;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.ChatUser;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class GroupChatCreationController {

    private MainCtrl mainCtrl;
    private ServerUtils server;

    private ChatUser creator;

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
        availableUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setup(ChatUser creator){
        this.creator = creator;

        List<String> users = server.getUsers().stream()
                .map(ChatUser::getUserName)
                .filter(x -> !x.equals(creator.getUserName())).toList();

        availableUsers.setItems(FXCollections.observableList(users));
    }

    public void createGroup(){
        String groupName = this.groupName.getText();
        String groupDesc = this.groupDescription.getText();

        String creator = this.creator.getUserName();

        List<String> participants = this.availableUsers.getSelectionModel().getSelectedItems();

        try{
            server.createGroupChat(creator, groupName, groupDesc, participants);
            cancel();
        } catch(CreatorNotFoundException e){
            e.printStackTrace();
        }
    }

    public void cancel(){
        groupName.clear();
        groupDescription.clear();

        mainCtrl.showUserOverview();
    }


}
