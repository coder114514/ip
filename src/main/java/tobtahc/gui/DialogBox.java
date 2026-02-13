package tobtahc.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * A dialog box GUI element.
 */
class DialogBox extends HBox {
    private static final Image defaultImage =
            new Image(DialogBox.class.getResourceAsStream("/images/default-profile-image.png"));

    private Label text;
    private ImageView displayPicture;

    /**
     * Initializes the dialog box with the dialog text and default profile image
     *
     * @param s the dialog text
     */
    public DialogBox(String s) {
        this(s, defaultImage);
    }

    /**
     * Initializes the dialog box with the dialog text and the profile image
     *
     * @param s the dialog text
     * @param i the profile image
     */
    public DialogBox(String s, Image i) {
        text = new Label(s);
        displayPicture = new ImageView(i);

        // Styling the dialog box
        text.setWrapText(true);
        displayPicture.setFitWidth(100.0);
        displayPicture.setFitHeight(100.0);
        this.setAlignment(Pos.TOP_RIGHT);

        this.getChildren().addAll(text, displayPicture);
    }

    /**
     * Creates a user's dialog box.
     *
     * @param s the dialog text
     */
    public static DialogBox getUserDialog(String s) {
        return getUserDialog(s, defaultImage);
    }

    /**
     * Creates a user's dialog box.
     *
     * @param s the dialog text
     * @param i the profile image
     */
    public static DialogBox getUserDialog(String s, Image i) {
        return new DialogBox(s, i);
    }

    /**
     * Creates a bot's dialog box.
     *
     * @param s the dialog text
     */
    public static DialogBox getBotDialog(String s) {
        return getBotDialog(s, defaultImage);
    }

    /**
     * Creates a bot's dialog box.
     *
     * @param s the dialog text
     * @param i the profile image
     */
    public static DialogBox getBotDialog(String s, Image i) {
        var db = new DialogBox(s, i);
        db.flip();
        return db;
    }

    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }
}
