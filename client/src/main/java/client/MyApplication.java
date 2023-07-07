package client;

import client.scenes.*;
import client.utils.ServerUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;



public class MyApplication extends Application {
    private static final MainCtrl mainCtrl = new MainCtrl();
    private static final ServerUtils server = new ServerUtils();


    /**
     * Starts application
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLBuilder fxmlBuilder = new FXMLBuilder();

        Pair<SignInCtrl, Scene> signInPair = fxmlBuilder.buildPair("scenes/sign-in.fxml");
        Pair<ChatOverviewCtrl, Scene> chatOverviewPair = fxmlBuilder.buildPair("scenes/chatOverview.fxml");
        Pair<SignUpCtrl, Scene> signUpPair = fxmlBuilder.buildPair("scenes/sign-up.fxml");
        Pair<UserOverviewController,
                Scene> userOverviewPair = fxmlBuilder.buildPair("scenes/user-overview.fxml");

        mainCtrl.init(stage, signInPair, chatOverviewPair, signUpPair, userOverviewPair);
    }

    /**
     * Main function
     * @param args run time arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Getter for main controller
     * @return main controller
     */
    public static MainCtrl getMainCtrl(){
        return mainCtrl;
    }

    /**
     * Getter for server
     * @return server
     */
    public static ServerUtils getServer(){
        return server;
    }
}