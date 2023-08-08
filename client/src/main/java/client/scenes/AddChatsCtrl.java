package client.scenes;

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
    private MainCtrl mainCtrl;

    @FXML
    private ListView<String> users;

    /**
     * Constructor
     * @param server the server to communicate with.
     * @param mainCtrl the main controller of the application.
     */
    @Inject
    public AddChatsCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
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
        List<String> usersAlreadyChatting = mainCtrl.getListOfChattingUsers();

        List<String> users = server.getUsers().stream().map(ChatUser::getUserName)
                .filter(s -> !usersAlreadyChatting.contains(s)).toList();

        return FXCollections.observableArrayList(users);
    }
}
