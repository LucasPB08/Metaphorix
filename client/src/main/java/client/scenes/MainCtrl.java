package client.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage;

    private SignInCtrl signInCtrl;
    private Scene signInScene;

    private ChatOverviewCtrl chatOverviewCtrl;
    private Scene chatOverview;

    private SignUpCtrl signUpCtrl;
    private Scene signUpScene;

    /**
     * Initialises controller
     * @param stage primary stage
     * @param signIn signIn pair
     * @param chatOverview chatOverview pair
     * @param signUp signUp pair
     */
    public void init(Stage stage, Pair<SignInCtrl, Scene> signIn,
                     Pair<ChatOverviewCtrl, Scene> chatOverview,
                     Pair<SignUpCtrl, Scene> signUp){
        this.primaryStage = stage;

        this.signInCtrl = signIn.getKey();
        this.signInScene = signIn.getValue();

        this.chatOverviewCtrl = chatOverview.getKey();
        this.chatOverview = chatOverview.getValue();

        this.signUpCtrl = signUp.getKey();
        this.signUpScene = signUp.getValue();

        showSignIn();
        stage.show();
    }

    /**
     * Shows chat overview scene.
     */
    public void showChatOverview(){
        primaryStage.setTitle("Chat overview");
        primaryStage.setScene(chatOverview);
    }

    /**
     * Shows welcome scene.
     */
    public void showSignIn(){
        primaryStage.setTitle("Sign in");
        primaryStage.setScene(signInScene);
    }

    public void showSignUp(){
        primaryStage.setTitle("Sign up");
        primaryStage.setScene(signUpScene);
    }

}
