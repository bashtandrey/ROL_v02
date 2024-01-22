package org.bashtan.library.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.bashtan.library.application.StartLibrary;
import org.bashtan.library.application.*;
import org.bashtan.library.constants.AuthorizationConstants;
import org.bashtan.library.table.User;

import java.util.Objects;


public class AuthorizationController {
    public static User user;

    @FXML
    private Button CancelButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button authorizationButton;

    @FXML
    private Label connectionLabel;

    @FXML
    private Label loginLabel;

    @FXML
    private TextField loginTextField;

    @FXML
    private Label passwordLabel;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button settingsButton;

    @FXML
    private Label titleLabel;

    @FXML
    void authorizationButtonOnAction(ActionEvent event) {
        String hashPassword = new Coder(passwordTextField.getText()).getHash();
             user = StartLibrary.runLibrary.hibernateRun.sessionFactory.openSession()
                    .createQuery("FROM User WHERE login = :login", User.class)
                    .setParameter("login", loginTextField.getText())
                    .uniqueResult();
            if (user != null && Objects.equals(hashPassword, user.getPassword())) {
                    new Back().back(anchorPane, "/org/bashtan/library/controller/main.fxml");
            } else {
                Message.alertError(AuthorizationConstants.LOG_IN, AuthorizationConstants.NOT_CORRECT);
            }
    }
    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        StartApplication.closeAPP();
    }

    @FXML
    void settingsButtonOnAction(ActionEvent event) {
        new Back().back(anchorPane, "/org/bashtan/library/controller/settings.fxml");
    }

    public void onActionLoginTextField(ActionEvent event) {
        Check.check(loginTextField, passwordTextField, AuthorizationConstants.LOGIN_LABEL, AuthorizationConstants.TEXT_EMPTY);
    }

    public void onActionPasswordTextField(ActionEvent event) {

        Check.check(passwordTextField,authorizationButton,AuthorizationConstants.LOGIN_LABEL, AuthorizationConstants.TEXT_EMPTY);
    }


    @FXML
    void initialize() {
        authorizationButton.setDisable(true);
        loginTextField.setDisable(true);
        passwordTextField.setDisable(true);
        titleLabel.setText(AuthorizationConstants.TITLE_LABEL);
        loginLabel.setText(AuthorizationConstants.LOGIN_LABEL);
        loginTextField.setPromptText(AuthorizationConstants.LOGIN_LABEL);
        passwordLabel.setText(AuthorizationConstants.PASSWORD_LABEL);
        passwordTextField.setPromptText(AuthorizationConstants.PASSWORD_LABEL);
        settingsButton.setText(AuthorizationConstants.SETTINGS_BUTTON);
        CancelButton.setText(AuthorizationConstants.CANCEL_BUTTON);
        authorizationButton.setText(AuthorizationConstants.AUTHORIZATION_BUTTON);

        if(!RunLibrary.flagFileSecretKey){
            connectionLabel.setText(AuthorizationConstants.FILE_FILE_SECRET_KEY_ERROR);
        } else if (!RunLibrary.flagFileHibernateProperties) {
            connectionLabel.setText(AuthorizationConstants.FILE_HIBERNATE_PROPERTIES_ERROR);
        } else if (!RunLibrary.flagSessionFactory)
            connectionLabel.setText(AuthorizationConstants.CONNECTION_FALSE);
            else if (RunLibrary.flagUserEmpty)
                connectionLabel.setText(AuthorizationConstants.USER_EMPTY);
            else {
                connectionLabel.setText(AuthorizationConstants.CONNECTION_TRUE);
                settingsButton.setVisible(false);
                loginTextField.setDisable(false);
            }

    }
}
