package client.scenes;

import client.MyApplication;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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

    /**
     * Initialises controller
     */
    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
        server = MyApplication.getServer();
    }

    /**
     * Goes back to log in scene
     */
    public void back(){
        clearFields();
        mainCtrl.showSignIn();
    }

    /**
     * Checks user input. If input is valid, a new user is created
     * and stored in the database.
     */
    public void signUp(){
        if(firstName.getText().isBlank() || lastName.getText().isBlank()
                || userName.getText().isBlank()){
            showNameErrors();
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

        if(server.existsUser(userName.getText())){
            errorMessages.setText("This username is already in use.");
            return;
        }

        String fullName = firstName.getText() + " " + lastName.getText();

        try {
            server.storeUser(fullName, userName.getText(), password.getText());
        } catch(Exception e){
            System.err.println(e.getMessage());
            errorMessages.setText("Something went wrong");
        }

        back();
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

    private void clearFields() {
        firstName.clear();
        lastName.clear();
        userName.clear();
        password.clear();
        rePassword.clear();
    }

}
