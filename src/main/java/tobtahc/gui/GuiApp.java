package tobtahc.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The GUI app object.
 */
public class GuiApp extends Application {
    @Override
    public void start(Stage stage) {
        try {
            var fxmlLoader = new FXMLLoader(GuiApp.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Loading Error");
            alert.setHeaderText("View could not be loaded.");
            alert.setContentText("The app encountered a problem and will now close.");
            alert.showAndWait();
        }
    }
}
