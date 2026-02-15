package tobtahc.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tobtahc.storage.Storage;
import tobtahc.util.Rng;

/**
 * The GUI app object.
 */
public class GuiApp extends Application {
    private Storage storage;
    private Rng rng;
    private UserContext ctx;

    @Override
    public void init() {
        var params = getParameters().getRaw();
        storage = new Storage(params.get(0), params.get(1));
        rng = new Rng();
    }

    @Override
    public void start(Stage stage) {
        try {
            var result = storage.loadTasks();
            var tasks = result.tasks();
            ctx = new UserContext(tasks, storage, rng);
            if (result.numBadLines() > 0) {
                var alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Save File");
                alert.setHeaderText("There were bad lines in the save file.");
                alert.setContentText(String.format("%d bad lines will be removed.", result.numBadLines()));
                alert.showAndWait();
            }
        } catch (IOException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Loading Error");
            alert.setHeaderText("The save file could not be loaded");
            alert.setContentText("The app encountered a problem and will now close.");
            alert.showAndWait();
            Platform.exit();
        }

        try {
            var fxmlLoader = new FXMLLoader(GuiApp.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setUserContext(ctx);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Loading Error");
            alert.setHeaderText("View could not be loaded.");
            alert.setContentText("The app encountered a problem and will now close.");
            alert.showAndWait();
            Platform.exit();
        }
    }
}
