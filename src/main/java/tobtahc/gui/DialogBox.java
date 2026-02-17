package tobtahc.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the profile image
 * and a label containing text from the speaker.
 */
class DialogBox extends HBox {
    private static final Image userImage =
            new Image(DialogBox.class.getResourceAsStream("/images/user.png"));
    private static final Image botImage =
            new Image(DialogBox.class.getResourceAsStream("/images/bot.png"));

    @FXML private Label dialog;
    @FXML private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            var fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
            dialog.setText(text);
            displayPicture.setImage(img);
        } catch (IOException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Loading Error");
            alert.setHeaderText("View could not be loaded.");
            alert.setContentText("The app encountered a problem and cannot display the dialog box.");
            alert.showAndWait();
        }
    }

    /**
     * Creates a user's dialog box.
     *
     * @param text the dialog text
     */
    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text, userImage);
    }

    /**
     * Creates a bot's dialog box.
     *
     * @param text the dialog text
     */
    public static DialogBox getBotDialog(String text) {
        var db = new DialogBox(text, botImage);
        db.flip();
        return db;
    }

    private void flip() {
        var tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }
}
