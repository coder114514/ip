package tobtahc.gui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tobtahc.command.CommandContext;
import tobtahc.command.CommandParseError;
import tobtahc.command.CommandParser;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    private static final String USER_IMAGE_PATH = "/images/user.png";
    private static final String BOT_IMAGE_PATH = "/images/bot.png";

    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private UserContext userCtx;
    private CommandContext cmdCtx;
    private Image userImage;
    private Image botImage;

    /**
     * Initializes the controller after the FXML has been loaded.
     * This method is called automatically by the FXML loader.
     */
    @FXML
    public void initialize() {
        userImage = new Image(getClass().getResourceAsStream(USER_IMAGE_PATH));
        botImage = new Image(getClass().getResourceAsStream(BOT_IMAGE_PATH));

        addDialogs(DialogBox.getBotDialog("Hello! I'm TobTahc. Tob tahc a ma I.\n"
                + "What can I do for you?", botImage));
    }

    /**
     * Injects the user context into the controller.
     *
     * @param ctx the user context to inject
     */
    public void setUserContext(UserContext ctx) {
        userCtx = ctx;
        cmdCtx = new CommandContext(ctx.tasks(), ctx.rng());
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();

        if (input.isEmpty()) {
            return;
        }

        userInput.clear();
        addDialogs(DialogBox.getUserDialog(input, userImage));

        try {
            var cmd = CommandParser.parse(input);
            var result = cmd.execute(cmdCtx);
            var lines = result.messageLines();

            if (!lines.isEmpty()) {
                addDialogs(DialogBox.getBotDialog(String.join("\n", lines), botImage,
                        cmd.getType()));
            }

            if (result.needSave()) {
                try {
                    userCtx.storage().saveTasks(userCtx.tasks());
                } catch (IOException e) {
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Save File");
                    alert.setHeaderText("Could not save the tasks.");
                    alert.setContentText("IO Error: " + e.getMessage() + ".");
                    alert.showAndWait();
                }
            }

            if (result.isExit()) {
                Platform.exit();
            }
        } catch (CommandParseError e) {
            addDialogs(DialogBox.getBotDialogForSyntaxError(String.join("\n", e.getLines()), botImage));
        }
        // autoscroll downwards after the new message bubbles have been added
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    private void addDialogs(DialogBox... boxes) {
        dialogContainer.getChildren().addAll(boxes);
    }
}
