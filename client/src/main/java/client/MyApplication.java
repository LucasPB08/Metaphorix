package client;

import client.scenes.*;
import client.utils.ServerUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;


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
        Pair<SignInCtrl, Scene> signInPair = createPair("scenes/sign-in.fxml");
        Pair<ChatOverviewCtrl, Scene> chatOverviewPair = createPair("scenes/chatOverview.fxml");
        Pair<SignUpCtrl, Scene> signUpPair =createPair("scenes/sign-up.fxml");
        Pair<UserOverviewController,
                Scene> userOverviewPair = createPair("scenes/user-overview.fxml");

        mainCtrl.init(stage, signInPair, chatOverviewPair, signUpPair, userOverviewPair);
    }

    private <T> Pair<T, Scene> createPair(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Scene scene = new Scene(loader.load());
        T controller = loader.getController();
        return new Pair<>(controller, scene);
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