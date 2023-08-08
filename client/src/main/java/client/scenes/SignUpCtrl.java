package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private PasswordField password;

    @FXML
    private PasswordField rePassword;

    @FXML
    private Text errorMessages;

    /**
     * Constructor
     * @param mainCtrl The main controller of the application
     * @param server The server to communicate with.
     */
    @Inject
    public SignUpCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
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

        String passwordRequirements = validatePassword();
        if(passwordRequirements != null){
            errorMessages.setText(passwordRequirements);
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

    private String validatePassword(){
        String password =  this.password.getText();

        List<String> errors = new ArrayList<>();

        Pattern letter = Pattern.compile("[a-zA-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        Matcher hasLetter = letter.matcher(password);
        Matcher hasDigit = digit.matcher(password);
        Matcher hasSpecial = special.matcher(password);

        if(password.length() < 8)
            errors.add("At least eight characters");

        if(!hasLetter.find())
            errors.add("At least one alphabetical letter");

        if(!hasDigit.find())
            errors.add("At least one digit");

        if(!hasSpecial.find())
            errors.add("At least one special character");

        if(errors.isEmpty())
            return null;

        StringBuilder errorMessage = new StringBuilder("Your password should contain: \n");

        for(String error: errors){
            errorMessage.append("-").append(error).append("\n");
        }

        return errorMessage.toString();
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
