package client.scenes;

import client.MyApplication;
import javafx.fxml.FXML;

public class ChatOverviewCtrl {
    private MainCtrl mainCtrl;

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
    }

    /**
     * Shows sign in scene.
     */
    public void showSignIn(){
        mainCtrl.showSignIn();
    }
}
