package org.bashtan.library.application;

import org.bashtan.library.constants.BookConstants;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.function.UnaryOperator;

public class Check {

    public static void checkLength(TextArea textArea, Label label, int lengthLabel, String title, String textLength) {
        int length = textArea.getLength();
        if (length <= lengthLabel) {
            label.setText(String.valueOf(length));
            label.setTextFill(Color.BLACK);
        } else {
            label.setText(String.valueOf(length));
            label.setTextFill(Color.RED);
            Message.alertInformation(title, textLength);
        }
    }

    public static void checkLength(TextField textField, Label label, int lengthLabel, String title, String textLength) {
        int length = textField.getLength();
        if (length <= lengthLabel) {
            label.setText(String.valueOf(length));
            label.setTextFill(Color.BLACK);
        } else {
            label.setText(String.valueOf(length));
            label.setTextFill(Color.RED);
            Message.alertInformation(title, textLength);
        }
    }

    public static void check(TextField textFieldFirst, TextArea textArea, Button button, String title, String textMessage) {
        if (textFieldFirst.getText() == null || textFieldFirst.getText().isEmpty())
            Message.alertInformation(title, textMessage);
        else {
            textArea.setDisable(false);
            textArea.requestFocus();
            button.setDisable(false);
        }
    }

    public static void check(TextField textFieldFirst, TextField textFieldSecond, String title, String textMessage) {
        if (textFieldFirst.getText() == null || textFieldFirst.getText().isEmpty())
            Message.alertInformation(title, textMessage);
        else {
            textFieldSecond.setDisable(false);
            textFieldSecond.requestFocus();
        }
    }
    public static void check(TextField textFieldFirst, TextField textFieldSecond,int length, String title, String textMessage) {
        if (textFieldFirst.getText() == null || textFieldFirst.getText().isEmpty() || !textFieldSecond.getText().matches("\\d{"+ length + "}")) {
            Message.alertError(BookConstants.PUBLISHING_YEAR_LABEL, BookConstants.TEXT_LENGTH_PUBLISHING_YEAR);
        } else {
            textFieldSecond.setDisable(false);
            textFieldSecond.requestFocus();
        }
    }

    public static void check(TextField textFieldFirst, Button textFieldSecond, String title, String textMessage) {
        if (textFieldFirst.getText() == null || textFieldFirst.getText().isEmpty())
            Message.alertInformation(title, textMessage);
        else {
            textFieldSecond.setDisable(false);
            textFieldSecond.requestFocus();
        }
    }

    public static void check(TextField textFieldFirst, ComboBox<?> comboBox, String title, String textMessage) {
        if (textFieldFirst.getText() == null || textFieldFirst.getText().isEmpty())
            Message.alertInformation(title, textMessage);
        else {
            comboBox.setDisable(false);
            comboBox.requestFocus();
        }
    }
    public static void check(ComboBox<?> comboBox, TextField textField, String title, String textMessage) {
        if (comboBox.getValue() == null)
            Message.alertInformation(title, textMessage);
        else {
            textField.setDisable(false);
            textField.requestFocus();
        }
    }

    public static void checkUser(TextField textFieldFirst, TextField textFieldSecond, String title, String textMessage) {
        if (textFieldFirst.getText() == null || textFieldFirst.getText().isEmpty())
            Message.alertInformation(title, textMessage);
        else {
            textFieldSecond.setVisible(true);
            textFieldSecond.setDisable(false);
            textFieldSecond.requestFocus();
        }
    }

    public static void checkUser(TextField textFieldFirst, GridPane gridPane, String title, String textMessage) {
        if (textFieldFirst.getText() == null || textFieldFirst.getText().isEmpty())
            Message.alertInformation(title, textMessage);
        else {
            gridPane.setDisable(false);
        }
    }

    public static void checkCharacter(TextField textField, UnaryOperator unaryOperator) {
        textField.setTextFormatter(new TextFormatter<>(unaryOperator));
    }

}
