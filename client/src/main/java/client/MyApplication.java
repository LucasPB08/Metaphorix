package client;

import client.scenes.ChatOverviewCtrl;
import client.scenes.MainCtrl;
import client.scenes.SignInCtrl;
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
        Pair<SignInCtrl, Scene> pair = createPair("scenes/sign-in.fxml", SignInCtrl.class);
        Pair<ChatOverviewCtrl, Scene> pair2 = createPair("scenes/chatOverview.fxml", ChatOverviewCtrl.class);

        mainCtrl.init(stage, pair, pair2);
    }

    private <T> Pair<T, Scene> createPair(String resource, Class<T> type) throws IOException {
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