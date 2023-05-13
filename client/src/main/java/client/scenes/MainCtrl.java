package client.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage;

    private WelcomeCtrl welcomeCtrl;
    private Scene welcomeScene;

    private ChatOverviewCtrl chatOverviewCtrl;
    private Scene chatOverview;

    /**
     * Initialises controller
     * @param stage primary stage
     * @param welcome welcome pair
     * @param chatOverview chatOverview pair
     */
    public void init(Stage stage, Pair<WelcomeCtrl, Scene> welcome,
                     Pair<ChatOverviewCtrl, Scene> chatOverview){
        this.primaryStage = stage;

        this.welcomeCtrl = welcome.getKey();
        this.welcomeScene = welcome.getValue();

        this.chatOverviewCtrl = chatOverview.getKey();
        this.chatOverview = chatOverview.getValue();

        showWelcome();
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
    public void showWelcome(){
        primaryStage.setTitle("Metaphorix");
        primaryStage.setScene(welcomeScene);
    }

}
