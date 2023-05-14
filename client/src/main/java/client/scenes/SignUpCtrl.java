package client.scenes;

import client.MyApplication;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    private Text errorMessages;

    @FXML
    public void initialise(){
        mainCtrl = MyApplication.getMainCtrl();
        server = MyApplication.getServer();
    }

    private void back(){
        mainCtrl.showSignIn();
    }

    private void signUp(){
        if(firstName.getText().isBlank() || lastName.getText().isBlank() || userName.getText().isBlank()){
            showNameErrors();
            return;
        }

        if(server.existsUser(userName.getText())){
            errorMessages.setText("This username is already in use.");
            return;
        }

        if(password.getText().isBlank()) {
            errorMessages.setText("Please fill in a password.");
            return;
        }

        if(rePassword.getText().isBlank()){
            errorMessages.setText("Please re-enter your password.");
            return;
        }

        if(!password.getText().equals(rePassword.getText())){
            errorMessages.setText("The passwords don't match.");
            return;
        }

        String fullName = firstName.getText() + " " + lastName.getText();

        server.storeUser(fullName, userName.getText(), password.getText());
    }

    private void showNameErrors(){
        List<String> errors = new ArrayList<>();
        if(firstName.getText().isBlank()) errors.add("First name is missing.");
        if(lastName.getText().isBlank()) errors.add("Last name is missing.");
        if(userName.getText().isBlank()) errors.add("User name is missing");

        String errorMessage = "";

        for(String error: errors){
            errorMessage += "-" + error + "\n";
        }

        errorMessages.setText(errorMessage);
    }
}
