package client.scenes;

import client.MyApplication;
import javafx.fxml.FXML;

public class WelcomeCtrl {
    private MainCtrl mainCtrl;

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
    }

    /**
     * Shows chat scene.
     */
    public void showChats(){
        mainCtrl.showChatOverview();
    }
}
