package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;

/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {

    private final HelloModel model = new HelloModel(new NtfyConnectionImpl());
    public ListView<NtfyMessageDto> messageView;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField messageInput;

    @FXML
    private void initialize() {
        if (messageLabel != null) {
            messageLabel.setText(model.getGreeting());
        }
        messageInput.textProperty().bindBidirectional(model.messageToSendProperty());

        messageView.setItems(model.getMessages());

        messageView.setCellFactory(list-> new ListCell<>() {
            @Override
            protected void updateItem(NtfyMessageDto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                }
                else {
                    setText(item.message());
                }
            }
        });
    }

    public void sendMessage(ActionEvent actionEvent) {
        model.sendMessage();
        messageInput.clear();
    }
    @FXML
    public void attachFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to attach");

        File file = fileChooser.showOpenDialog(messageInput.getScene().getWindow());
        if (file != null) {
            try {
                // Läs filens innehåll som text (för små textfiler)
                String content = Files.readString(file.toPath());

                // Skapa ett meddelande som innehåller filnamn + innehåll
                String message = "File: " + file.getName() + "\n" + content;

                model.setMessageToSend(message);
                model.sendMessage();

            } catch (Exception e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        }
    }
}
