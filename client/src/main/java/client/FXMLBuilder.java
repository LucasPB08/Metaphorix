package client;

import client.modules.ControllerModule;
import client.modules.MessageHandlerModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.util.Pair;

import java.io.IOException;

public class FXMLBuilder {

    private Injector injector = Guice.createInjector(new ControllerModule(), new MessageHandlerModule());

    /**
     *  Creates a dialog pane.
     * @param resource path to fxml file of the dialog.
     * @param <T> Controller class.
     * @return Pair with dialog and its controller.
     */
    public <T> Pair<T, Dialog<ButtonType>> buildDialogPane(String resource){
        try {
            FXMLLoader loader = new FXMLLoader(MyApplication.class.
                    getResource("scenes/add-user-dialog.fxml"));

            DialogPane pane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);

            return new Pair<>(loader.getController(), dialog);
        } catch(Exception e){
            System.err.println("Something went wrong");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a pair of a scene and its corresponding controller.
     * @param resource Path to the fxml file of the scene
     * @return Pair of scene and controller
     * @param <T> Controller class
     * @throws IOException If the path is incorrect.
     */
    public <T> Pair<T, Scene> buildPair(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));

        loader.setControllerFactory(injector::getInstance);

        Scene scene = new Scene(loader.load());
        T controller = loader.getController();
        return new Pair<>(controller, scene);
    }
}
