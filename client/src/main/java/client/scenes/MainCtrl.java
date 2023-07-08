package client.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import commons.ChatUser;

public class MainCtrl {
    private Stage primaryStage;

    private SignInCtrl signInCtrl;
    private Scene signInScene;

    private SignUpCtrl signUpCtrl;
    private Scene signUpScene;

    private UserOverviewController userOverviewCtrl;
    private Scene userOverviewScene;

    /**
     * Initialises controller
     * @param stage primary stage
     * @param signIn signIn pair
     * @param signUp signUp pair
     * @param userOverview user overview pair
     */
    public void init(Stage stage, Pair<SignInCtrl, Scene> signIn,
                     Pair<SignUpCtrl, Scene> signUp,
                     Pair<UserOverviewController, Scene> userOverview){
        this.primaryStage = stage;

        this.signInCtrl = signIn.getKey();
        this.signInScene = signIn.getValue();

        this.signUpCtrl = signUp.getKey();
        this.signUpScene = signUp.getValue();

        this.userOverviewCtrl = userOverview.getKey();
        this.userOverviewScene = userOverview.getValue();

        showSignIn();
        stage.show();
    }

    /**
     * Shows sign in scene.
     */
    public void showSignIn(){
        primaryStage.setTitle("Sign in");
        primaryStage.setScene(signInScene);
    }

    /**
     * Shows sign up scene.
     */
    public void showSignUp(){
        primaryStage.setTitle("Sign up");
        primaryStage.setScene(signUpScene);
    }

    /**
     * Shows the user overview
     * @param user the user whose overview will be shown
     */
    public void showUserOverview(ChatUser user){
        userOverviewCtrl.setLoggedInUser(user);
        userOverviewCtrl.loadProfile();

        primaryStage.setTitle("Chats");
        primaryStage.setScene(userOverviewScene);
    }

}
