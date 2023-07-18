package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GroupChatCreationController {

    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField groupName;

    @FXML
    private TextArea groupDescription;

    @FXML
    private ListView<String> availableUsers;


    public GroupChatCreationController(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }



}
