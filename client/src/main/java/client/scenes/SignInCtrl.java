package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import commons.ChatUser;

import java.util.Timer;
import java.util.TimerTask;


public class SignInCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Text errorMessage;

    /**
     * Constructor
     * @param mainCtrl The main controller of the client
     * @param server The server to communicate with
     */
    @Inject
    public SignInCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        userName.setOnKeyPressed(key -> {
            if(key.getCode() == KeyCode.ENTER)
                password.requestFocus();
        });

        password.setOnKeyPressed(key -> {
            if(key.getCode() == KeyCode.ENTER)
                login();
        });

    }

    /**
     * Checks user input, if username and password are correct,
     * the user gets logged in.
     */
    public void login(){
        if(!validateUserName() || !validatePassword()) {
            setErrorMessage();
            return;
        }

        ChatUser user = server.getUserById(userName.getText());

        mainCtrl.login(user);
        mainCtrl.showUserOverview();
    }

    private void setErrorMessage(){
        errorMessage.setText("User name or password incorrect");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                clearErrorMessage();
            }
        }, 2000);
    }

    private void clearErrorMessage(){
        Platform.runLater(() -> errorMessage.setText(""));
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

}
