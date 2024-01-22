package org.bashtan.library.application;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Optional;

import static javafx.scene.control.ButtonType.*;

public class Message {

    public static void alertError(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(null);
        alert.showAndWait();
    }

    public static void alertInformation(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(null);
        alert.showAndWait();
    }

    public static boolean alertClose(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(null);
        if (alert.showAndWait().filter(t -> t == OK).isPresent()) {
            return true;
        } else return false;
    }

    public static boolean alertConfirmation(String title, String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.OK) return true;
        else return false;
    }
    public static Alert createCustomAlert(String title, String headerText) {
        Alert customAlert = new Alert(Alert.AlertType.NONE);
        customAlert.setTitle(title);
        customAlert.setHeaderText(headerText);
        ButtonType yesButton = new ButtonType("New user");
        ButtonType noButton = new ButtonType("Existing user");
        DialogPane dialogPane = customAlert.getDialogPane();
        dialogPane.getButtonTypes().add(0,yesButton);
        dialogPane.getButtonTypes().add(1,noButton);
        dialogPane.getButtonTypes().add(2,CANCEL);

        customAlert.setResultConverter(dialogButton -> {
            if (dialogButton == yesButton) {
                return OK;
            } else if (dialogButton == noButton) {
                return NO;
            } else {
                return CANCEL;
            }
        });
        return customAlert;
    }

}
