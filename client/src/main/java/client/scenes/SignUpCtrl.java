package client.scenes;

import client.MyApplication;
import client.utils.ServerUtils;
import javafx.fxml.FXML;

import java.awt.*;

public class SignUpCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private TextField rePassword;

    @FXML
    public void initialise(){
        mainCtrl = MyApplication.getMainCtrl();
        server = MyApplication.getServer();
    }
}
