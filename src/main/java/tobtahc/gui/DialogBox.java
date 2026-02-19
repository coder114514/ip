package tobtahc.gui;

import java.io.IOException;

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
     *         with the default style
     */
    public static DialogBox getBotDialog(String text, Image img) {
        return new DialogBox(text, img).flip();
    }

    /**
     * Creates a dialog box representing the chatbot.
     *
     * @param text the chatbot's response text
     * @param img the chatbot's profile image
     * @param commandType the type of ther user's command
     * @return a {@code DialogBox} containing the chatbot's response and image,
     *         styled based on {@code CommandType}
     */
    public static DialogBox getBotDialog(String text, Image img, String commandType) {
        return new DialogBox(text, img).flip().changeStyle(commandType);
    }

    private DialogBox flip() {
        var tmp = FXCollections.observableArrayList(getChildren());
        FXCollections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
        return this;
    }

    private DialogBox changeStyle(String commandType) {
        switch (commandType) {
        case "AddCommand":
            dialog.getStyleClass().add("add-label");
            break;
        case "MarkCommand":
            dialog.getStyleClass().add("marked-label");
            break;
        case "UnmarkCommand":
            dialog.getStyleClass().add("unmarked-label");
            break;
        case "DeleteCommand":
            dialog.getStyleClass().add("delete-label");
            break;
        default:
            throw new AssertionError("unreachable");
        }
        return this;
    }
}
