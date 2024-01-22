package org.bashtan.library.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Back {

    public void back(AnchorPane anchorPane, String resource) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Scene scene = stage.getScene();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        Parent parent = null;
        try {
            parent = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene = new Scene(parent);
        stage.setScene(scene);
    }
}
