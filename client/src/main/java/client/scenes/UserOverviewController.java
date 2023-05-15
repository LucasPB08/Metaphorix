package client.scenes;

import client.MyApplication;
import client.utils.ServerUtils;
import javafx.fxml.FXML;

public class UserOverviewController{
    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
        server = MyApplication.getServer();
    }


}
