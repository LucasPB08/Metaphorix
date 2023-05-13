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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/welcome.fxml"));
        Scene scene = new Scene(loader.load());
        SignInCtrl ctrl = loader.getController();

        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("scenes/chatOverview.fxml"));
        Scene scene2 = new Scene(loader2.load());
        ChatOverviewCtrl ctrl2 = loader2.getController();

        Pair<SignInCtrl, Scene> pair = new Pair<>(ctrl, scene);
        Pair<ChatOverviewCtrl, Scene> pair2 = new Pair<>(ctrl2, scene2);

        mainCtrl.init(stage, pair, pair2);
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