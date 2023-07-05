package client.scenes;

import client.MyApplication;
import client.utils.ServerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import server.commons.ChatUser;

import java.util.List;


public class AddChatsCtrl {
    private ServerUtils server;

    @FXML
    private ListView<String> users;

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        server = MyApplication.getServer();
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
