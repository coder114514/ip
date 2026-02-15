package tobtahc.gui;

import java.io.IOException;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tobtahc.command.CommandContext;
import tobtahc.command.CommandParseError;
import tobtahc.command.CommandParser;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private UserContext userCtx;
    private CommandContext cmdCtx;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setUserContext(UserContext ctx) {
        userCtx = ctx;
        cmdCtx = new CommandContext(ctx.tasks(), ctx.rng());
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        userInput.clear();
        addDialogs(DialogBox.getUserDialog(input));

        try {
            var cmd = CommandParser.parse(input);
            var result = cmd.execute(cmdCtx);
            var lines = result.messageLines();

            if (!lines.isEmpty()) {
                addDialogs(DialogBox.getBotDialog(concat(lines)));
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
            addDialogs(DialogBox.getBotDialog(concat(e.getLines())));
        }
    }

    private void addDialogs(DialogBox... boxes) {
        dialogContainer.getChildren().addAll(boxes);
    }

    private String concat(List<String> lines) {
        var sb = new StringBuilder();
        boolean first = true;
        for (var line : lines) {
            if (first) {
                first = false;
            } else {
                sb.append('\n');
            }
            sb.append(line);
        }
        return sb.toString();
    }

    private String concat(String... lines) {
        var sb = new StringBuilder();
        boolean first = true;
        for (var line : lines) {
            if (first) {
                first = false;
            } else {
                sb.append('\n');
            }
            sb.append(line);
        }
        return sb.toString();
    }
}
