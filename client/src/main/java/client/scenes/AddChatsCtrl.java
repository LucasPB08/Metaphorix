package client.scenes;

import client.MyApplication;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import commons.ChatUser;

import java.util.List;


public class AddChatsCtrl {
    private ServerUtils server;

    @FXML
    private ListView<String> users;

    @Inject
    public AddChatsCtrl(ServerUtils server){
        this.server = server;
    }

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        users.setItems(getUsers());
    }

    /**
     * Gets the user that was chosen to start a chat with
     * @return Selected user, or null if no user was selected
     */
    public String getSelected(){
        List<String> selected = users.getSelectionModel().getSelectedItems();

        return selected.isEmpty() ? null:selected.get(0);
    }

    private ObservableList<String> getUsers(){
        List<String> users = server.getUsers().stream().map(ChatUser::getUserName).toList();
        return FXCollections.observableArrayList(users);
    }
}
