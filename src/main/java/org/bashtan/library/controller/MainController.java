package org.bashtan.library.controller;


import org.bashtan.library.application.Back;
import org.bashtan.library.constants.MainConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import org.bashtan.library.constructor.Level;

public class MainController {
    final private static Level LEVEL = AuthorizationController.user.getLevel();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Menu libraryMenu;

    @FXML
    private Menu helpMenu;

    @FXML
    private Menu exitMenu;

    @FXML
    private Menu userMenu;

    @FXML
    private MenuItem bookMenuItem;
    @FXML
    private MenuItem humanMenuItem;

    @FXML
    private MenuItem giveBookMenuItem;

    @FXML
    private MenuItem getBookMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem userMenuItem;

    @FXML
    void onActionBookMenuItem(ActionEvent event){
        new Back().back(anchorPane, "/org/bashtan/library/controller/bookTable.fxml");
    }

    @FXML
    void onActionHumanMenuItem(ActionEvent event){
        new Back().back(anchorPane, "/org/bashtan/library/controller/peopleTable.fxml");
    }

    @FXML
    void onActionGiveBookMenuItem(ActionEvent event){
        new Back().back(anchorPane, "/org/bashtan/library/controller/giveBookTable.fxml");
    }

    @FXML
    void onActionGetBookMenuItem(ActionEvent event){
        new Back().back(anchorPane, "/org/bashtan/library/controller/getBookTable.fxml");
    }

    @FXML
    void onActionExitMenuItem(ActionEvent event){
        StartApplication.closeAPP();
    }

    @FXML
    void onActionUserMenuItem(ActionEvent event){
        new Back().back(anchorPane, "/org/bashtan/library/controller/userTable.fxml");
    }
private void userLevel(){
        switch (LEVEL){
            case ADMIN:
            {
                userMenu.setDisable(false);
                break;
            }
            case USER:
            {
                userMenu.setDisable(true);
                break;
            }
        }
}

    @FXML
    void initialize() {
        userLevel();
        userMenu.setText(String.valueOf(LEVEL));
        libraryMenu.setText(MainConstants.LIBRARY_MENU);
        helpMenu.setText(MainConstants.HELP_MENU);
        exitMenu.setText(MainConstants.EXIT);
        exitMenuItem.setText(MainConstants.EXIT);
        bookMenuItem.setText(MainConstants.BOOK_MENU_ITEM);
        humanMenuItem.setText(MainConstants.HUMAN_MENU_ITEM);
        userMenuItem.setText(MainConstants.USER_MENU_ITEM);
        giveBookMenuItem.setText(MainConstants.GIVE_BOOK_MENU_ITEM);
        getBookMenuItem.setText(MainConstants.GET_BOOK_MENU_ITEM);
    }
}
