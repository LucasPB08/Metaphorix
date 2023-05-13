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

    /**
     * Initialises controller
     * @param stage primary stage
     * @param signIn signIn pair
     * @param chatOverview chatOverview pair
     */
    public void init(Stage stage, Pair<SignInCtrl, Scene> signIn,
                     Pair<ChatOverviewCtrl, Scene> chatOverview){
        this.primaryStage = stage;

        this.signInCtrl = signIn.getKey();
        this.signInScene = signIn.getValue();

        this.chatOverviewCtrl = chatOverview.getKey();
        this.chatOverview = chatOverview.getValue();

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
        primaryStage.setTitle("Metaphorix");
        primaryStage.setScene(signInScene);
    }

}
