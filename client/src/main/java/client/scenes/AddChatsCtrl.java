package client.scenes;

import client.MyApplication;
import client.utils.ServerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import server.commons.ChatUser;

import java.util.List;


public class AddChatsCtrl {
    private ServerUtils server;

    @FXML
    private ListView<String> users;

    @FXML
    public void initialize(){
        server = MyApplication.getServer();
        users.setItems(getUsers());
    }


    public String getSelected(){
        List<String> selected = users.getSelectionModel().getSelectedItems();

        return selected.isEmpty() ? null:selected.get(0);
    }

    private ObservableList<String> getUsers(){
        List<String> users = server.getUsers().stream().map(ChatUser::getUserName).toList();
        return FXCollections.observableArrayList(users);
    }
}
