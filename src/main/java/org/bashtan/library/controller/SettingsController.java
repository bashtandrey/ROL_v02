package org.bashtan.library.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.bashtan.library.application.*;
import org.bashtan.library.constants.SettingsConstants;
import org.bashtan.library.constructor.Level;
import org.bashtan.library.constructor.SettingProperties;
import org.bashtan.library.table.User;

public class SettingsController {
    private SettingProperties settingProperties;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label titleLabel;

    @FXML
    private Label hostLabel;
    @FXML
    private TextField hostTextField;

    @FXML
    private Label loginBDLabel;
    @FXML
    private TextField loginBDTextField;

    @FXML
    private Label passwordBDLabel;
    @FXML
    private PasswordField passwordBDPasswordField;

    @FXML
    private Label adminLoginLabel;
    @FXML
    private TextField adminLoginTextField;

    @FXML
    private Label adminPasswordLabel;
    @FXML
    private PasswordField adminPasswordPasswordField;

    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label titleLabel1;

    @FXML
    void okButtonOnAction(ActionEvent event) {
        if (!RunLibrary.flagFileSecretKey){
            if (StartLibrary.runLibrary.settingFile.createFileSecretKey(hostTextField.getText())){
                RunLibrary.flagFileSecretKey = true;
                new Back().back(anchorPane, "/org/bashtan/library/controller/authorization.fxml");
            } else Message.alertError(SettingsConstants.TITLE,SettingsConstants.TEXT_SECRET_KEY);
        }else if (!RunLibrary.flagFileHibernateProperties || !RunLibrary.flagSessionFactory) {
            settingProperties = SettingProperties
                    .builder()
                    .url(hostTextField.getText())
                    .username(loginBDTextField.getText())
                    .password(passwordBDPasswordField.getText())
                    .build();
            if(StartLibrary.runLibrary.settingFile.creatFileHibernateProperties(settingProperties)) {
                StartLibrary.runLibrary.hibernateRun();
                RunLibrary.flagFileHibernateProperties = true;
                new Back().back(anchorPane, "/org/bashtan/library/controller/authorization.fxml");
            } else Message.alertError(SettingsConstants.TITLE,SettingsConstants.TEXT_HIBERNATE_PROPERTIES);
        } else if (RunLibrary.flagUserEmpty) {
            String hashPassword = new Coder(adminPasswordPasswordField.getText()).getHash();
            User admin = User.builder()
                    .login(adminLoginTextField.getText())
                    .password(hashPassword)
                    .level(Level.ADMIN)
                    .people(DefaultObject.PEOPLE_ADMIN)
                    .build();
            StartLibrary.runLibrary.hibernateRun.persist(DefaultObject.PEOPLE_ADMIN);
            StartLibrary.runLibrary.hibernateRun.persist(admin);
            StartLibrary.runLibrary.hibernateRun();
            new Back().back(anchorPane, "/org/bashtan/library/controller/authorization.fxml");

        }

    }
    @FXML
    void cancelButtonOnActon(ActionEvent event) {
        new Back().back(anchorPane, "/org/bashtan/library/controller/authorization.fxml");
    }

    @FXML
    void onActionHostTextField(ActionEvent event) {
        if (!RunLibrary.flagFileSecretKey)
        {
            if (hostTextField.getText().length()!=16)
                Message.alertError(SettingsConstants.LABEL_SECRET_KEY,SettingsConstants.ERROR_SECRET_KEY);
            else {
                okButton.setDisable(false);
                okButton.requestFocus();
            }

        }else Check.check(hostTextField, loginBDTextField, SettingsConstants.HostLabel, SettingsConstants.TEXT_EMPTY);
    }

    @FXML
    void onActionLoginBDTextField(ActionEvent event) {
        Check.check(loginBDTextField, passwordBDPasswordField, SettingsConstants.LoginBDLabel, SettingsConstants.TEXT_EMPTY);
    }


    @FXML
    void onActionPasswordBDPasswordField(ActionEvent event) {
            Check.check(passwordBDPasswordField, okButton, SettingsConstants.PasswordBDLabel, SettingsConstants.TEXT_EMPTY);
    }

    @FXML
    void onActionAdminLoginTextField(ActionEvent event) {
        Check.check(adminLoginTextField, adminPasswordPasswordField, SettingsConstants.AdminLoginLabel, SettingsConstants.TEXT_EMPTY);
    }

    @FXML
    void onActionAdminPasswordBDPasswordField(ActionEvent event) {
        Check.check(adminPasswordPasswordField, okButton, SettingsConstants.AdminPasswordLabel, SettingsConstants.TEXT_EMPTY);
    }


private void setTextField(){
    settingProperties = (SettingProperties) StartLibrary.runLibrary.settingFile.readFileHibernateProperties();
    hostTextField.setText(settingProperties.getUrl());
    loginBDTextField.setText(settingProperties.getUsername());
    passwordBDPasswordField.setText(settingProperties.getPassword());
}

    @FXML
    void initialize() {
        if (!RunLibrary.flagFileSecretKey){
        titleLabel1.setText("The secret key must be no more\n or less than 16 characters");
        titleLabel1.setVisible(true);
        hostLabel.setText("Input Secret key");
            hostTextField.setPromptText("Enter 16 characters");

            hostTextField.setDisable(false);
        loginBDLabel.setVisible(false);
        loginBDTextField.setVisible(false);
        passwordBDLabel.setVisible(false);
        passwordBDPasswordField.setVisible(false);
        }else if (!RunLibrary.flagFileHibernateProperties){
            hostLabel.setText(SettingsConstants.HostLabel);
            hostTextField.setDisable(false);
            hostTextField.setPromptText(SettingsConstants.EXAMPLE_URL);
        }  else if (!RunLibrary.flagSessionFactory) {
            hostLabel.setText(SettingsConstants.HostLabel);
            hostTextField.setDisable(false);
            setTextField();
        }
        else if (RunLibrary.flagUserEmpty) {
            adminPasswordLabel.setVisible(true);
            adminLoginLabel.setVisible(true);
            adminLoginTextField.setVisible(true);
            adminPasswordPasswordField.setVisible(true);
            adminLoginTextField.setDisable(false);
            setTextField();
        }

        okButton.setDisable(true);
        titleLabel.setText(SettingsConstants.TITLE);
        loginBDLabel.setText(SettingsConstants.LoginBDLabel);
        passwordBDLabel.setText(SettingsConstants.PasswordBDLabel);

        adminLoginLabel.setText(SettingsConstants.AdminLoginLabel);
        adminPasswordLabel.setText(SettingsConstants.AdminPasswordLabel);

        okButton.setText(SettingsConstants.OK_BUTTON);
        cancelButton.setText(SettingsConstants.CANCEL_BUTTON);

    }
}
