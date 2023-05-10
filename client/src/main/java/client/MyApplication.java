package client;

import client.scenes.ChatOverviewCtrl;
import client.scenes.MainCtrl;
import client.scenes.WelcomeCtrl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MyApplication extends Application {
    private static final MainCtrl mainCtrl = new MainCtrl();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/welcome.fxml"));
        Scene scene = new Scene(loader.load());
        WelcomeCtrl ctrl = loader.getController();

        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("scenes/chatOverview.fxml"));
        Scene scene2 = new Scene(loader2.load());
        ChatOverviewCtrl ctrl2 = loader2.getController();

        Pair<WelcomeCtrl, Scene> pair = new Pair<>(ctrl, scene);
        Pair<ChatOverviewCtrl, Scene> pair2 = new Pair<>(ctrl2, scene2);

        mainCtrl.init(stage, pair, pair2);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static MainCtrl getMainCtrl(){
        return mainCtrl;
    }
}