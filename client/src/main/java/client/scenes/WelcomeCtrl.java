package client.scenes;

import client.MyApplication;
import javafx.fxml.FXML;

public class WelcomeCtrl {
    private MainCtrl mainCtrl;

    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
    }

    public void showChats(){
        mainCtrl.showChatOverview();
    }
}
