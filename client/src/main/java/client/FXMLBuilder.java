package client;

import client.scenes.AddChatsCtrl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.util.Pair;

import java.io.IOException;

public class FXMLBuilder {

    public Pair<AddChatsCtrl, Dialog<ButtonType>> buildDialogPane(String resource){
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

    public <T> Pair<T, Scene> buildPair(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Scene scene = new Scene(loader.load());
        T controller = loader.getController();
        return new Pair<>(controller, scene);
    }
}
