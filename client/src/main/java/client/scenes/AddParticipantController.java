package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class AddParticipantController {
    private ServerUtils server;

    @FXML
    private ListView<String> availableUsers;

    @Inject
    public AddParticipantController(ServerUtils server){
        this.server = server;
    }



}
