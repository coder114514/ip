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
     * Creates a dialog box representing the user.
     *
     * @param text the user's input text
     * @param img the user's profile image
     * @return a {@code DialogBox} containing the user's message and image
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Creates a dialog box representing the chatbot.
     *
     * @param text the chatbot's response text
     * @param img the chatbot's profile image
     * @return a {@code DialogBox} containing the chatbot's response and image
     */
    public static DialogBox getBotDialog(String text, Image img) {
        return new DialogBox(text, img).flip();
    }

    private DialogBox flip() {
        var tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
        return this;
    }
}
