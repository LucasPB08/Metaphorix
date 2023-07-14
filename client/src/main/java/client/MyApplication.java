package client;

import client.modules.ControllerModule;
import client.modules.MessageHandlerModule;
import client.scenes.*;
import client.utils.ServerUtils;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;



public class MyApplication extends Application {
    private static final Injector INJECTOR =
            Guice.createInjector(new ControllerModule(), new MessageHandlerModule());


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
        FXMLBuilder fxmlBuilder = new FXMLBuilder(INJECTOR);

        Pair<SignInCtrl, Scene> signInPair = fxmlBuilder.buildPair("scenes/sign-in.fxml");
        Pair<SignUpCtrl, Scene> signUpPair = fxmlBuilder.buildPair("scenes/sign-up.fxml");
        Pair<ChatOverviewController,
                Scene> chatOverviewPair = fxmlBuilder.buildPair("scenes/chat-overview.fxml");
        Pair<UserOverviewController, Scene> userOverviewPair =
                fxmlBuilder.buildPair("scenes/user-overview.fxml");

        MainCtrl mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        mainCtrl.init(stage, signInPair, signUpPair, chatOverviewPair, userOverviewPair);
    }

    /**
     * Main function
     * @param args run time arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static Injector getInjector(){
        return INJECTOR;
    }
}