package client.scenes;

import client.MyApplication;
import client.utils.ServerUtils;
import javafx.fxml.FXML;

public class WelcomeCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils server;

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
        server = MyApplication.getServer();
    }

    /**
     * Shows chat scene.
     */
    public void showChats(){
        System.out.println(server.getUsers());
        //mainCtrl.showChatOverview();
    }
}
