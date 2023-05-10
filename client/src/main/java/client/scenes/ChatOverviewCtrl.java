package client.scenes;

import client.MyApplication;
import com.sun.tools.javac.Main;
import javafx.fxml.FXML;

public class ChatOverviewCtrl {
    private MainCtrl mainCtrl;

    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
    }

    public void showWelcome(){
        mainCtrl.showWelcome();
    }
}
