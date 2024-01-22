package org.bashtan.library.controller;

import org.bashtan.library.application.Message;
import org.bashtan.library.application.RunLibrary;
import org.bashtan.library.application.StartLibrary;
import org.bashtan.library.constants.APPConstants;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.SessionFactory;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/org/bashtan/library/controller/authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(APPConstants.TITLE_LABEL);
        stage.setScene(scene);
        stage.getIcons().add(new Image(APPConstants.FILE_ICON));
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                if (Message.alertClose(APPConstants.TITLE_EXIT, APPConstants.TEXT_EXIT)) {
                    closeAPP();
                }
            }
        });
    }

    public static void closeAPP() {
        if(RunLibrary.flagFileSecretKey &&
                RunLibrary.flagFileHibernateProperties &&
                RunLibrary.flagSessionFactory) {
            java.util.Optional.ofNullable(StartLibrary.runLibrary.hibernateRun.sessionFactory).ifPresent(SessionFactory::close);
        }
        System.exit(0);

    }

    public static void main(String[] args) {
        launch();
    }
}