package client.scenes;

import client.MyApplication;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import server.commons.ChatUser;


public class SignInCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private Text errorMessage;


    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
        server = MyApplication.getServer();
        userName.setText("LucasPB");
        password.setText("R");
    }

    /**
     * Checks user input, if username and password are correct,
     * the user gets logged in.
     */
    public void login(){
        errorMessage.setText("");
        if(!validateUserName() || !validatePassword()) {
            setErrorMessage();
            return;
        }

        ChatUser user = server.getUserById(userName.getText());

        //ChatUser user = new ChatUser("User", "Lucas", "Ronnye");

        mainCtrl.showUserOverview(user);
    }

    private void setErrorMessage(){
        errorMessage.setText("User name or password incorrect");
    }

    private boolean validatePassword(){
        return server.validatePassword(userName.getText(), password.getText());
    }

    private boolean validateUserName(){
        return !(userName.getText().isBlank() || !server.existsUser(userName.getText()));
    }

    /**
     * Shows sign up scene.
     */
    public void showSignUp(){
        mainCtrl.showSignUp();
    }

    /**
     * Shows chat scene.
     */
    public void showChats(){
        mainCtrl.showChatOverview();
    }
}
