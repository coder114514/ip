package tobtahc.gui;

import javafx.geometry.Pos;
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
     * Initializes the dialog box with the dialog text and user's profile image
     *
     * @param s the dialog text
     * @param i user's profile image
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
}
