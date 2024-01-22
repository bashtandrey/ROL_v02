package org.bashtan.library.controller;

import org.bashtan.library.application.Back;
import org.bashtan.library.application.Message;
import org.bashtan.library.application.StartLibrary;
import org.bashtan.library.constants.BookConstants;
import org.bashtan.library.constants.GetBookTableConstants;
import org.bashtan.library.constants.PeopleConstants;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.bashtan.library.constants.BookTC;
import org.bashtan.library.constants.PeopleTC;
import org.bashtan.library.constructor.Gender;
import org.bashtan.library.table.Book;
import org.bashtan.library.table.People;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

public class GetBookTableController {
    private List<Book> books;
    private List<People> peoples;
    private Book setSelectBook;
    private People setSelectPeople;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<People> peopleTableView = new TableView<>();
    @FXML
    private TableColumn<People,String> lastNameTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People,String> firstNameTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People, LocalDate> birthdayTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People, Gender> genderTableColumn = new TableColumn<>();

    @FXML
    private TableView<Book> bookTableView = new TableView<>();
    @FXML
    private TableColumn<Book,String> serialTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<Book,String> nameBookTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<Book, Year> publishingYearTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<Book,String> publishingHouseTableColumn = new TableColumn<>();

    @FXML
    private Label peopleTableViewNullLabel;
    @FXML
    private Label bookTableViewNullLabel;
    @FXML
    private Label titleLabel;

    @FXML
    private Text peopleText;
    @FXML
    private Text bookText;

    @FXML
    private Button peopleButton;
    @FXML
    private Button bookButton;

    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    @FXML
    void humanTableViewOnKeyPressed(KeyEvent event) {selectPeople();}

    @FXML
    void humanTableViewOnMouseClicked(MouseEvent event) {
        selectPeople();
    }

    @FXML
    void bookTableViewOnKeyPressed(KeyEvent event) {
        selectBook();
    }

    @FXML
    void bookTableViewOnMouseClicked(MouseEvent event) {
        selectBook();
    }

    @FXML
    void onActionOkButton(ActionEvent event) {
        setSelectBook.setPeople(null);
        try {
            StartLibrary.runLibrary.hibernateRun.merge(setSelectBook);
            Message.alertInformation(GetBookTableConstants.TITLE, GetBookTableConstants.GET_BOOK_OK);
            new Back().back(anchorPane, "/org/bashtan/library/controller/main.fxml");
        } catch (Exception ex){
            Message.alertError(GetBookTableConstants.TITLE, ex.getMessage());
        }
    }

    @FXML
    void onActionCancelButton(ActionEvent event) {
        new Back().back(anchorPane, "/org/bashtan/library/controller/main.fxml");
    }

    @FXML
    void onActionPeopleButton(ActionEvent event) {
        setSelectPeople = null;
        bookTableView.setVisible(false);
        peopleTableView.setVisible(true);
        peopleText.setText(null);
        peopleText.setVisible(false);
        peopleButton.setVisible(false);

    }
    @FXML
    void onActionBookButton(ActionEvent event) {
        okButton.setDisable(true);
        bookTableView.setDisable(false);
        setSelectBook = null;
        bookText.setText(null);
        bookText.setVisible(false);
        bookButton.setVisible(false);
        peopleButton.setVisible(true);
    }
    private void selectPeople() {
        if (!peopleTableView.getSelectionModel().isEmpty()) {
            setSelectPeople = peopleTableView.getSelectionModel().getSelectedItem();
            if (Message.alertConfirmation(GetBookTableConstants.PEOPLE_TITLE, setSelectPeople.toString(), GetBookTableConstants.OK_PEOPLE_BUTTON)) {
                peopleText.setText(setSelectPeople.toString());
                peopleText.setVisible(true);
                peopleButton.setVisible(true);
                peopleTableView.setVisible(false);
                bookTableView.setVisible(true);
                restartBookTableView();
            }
        } else Message.alertInformation(GetBookTableConstants.PEOPLE_TITLE, GetBookTableConstants.PEOPLE_NOT_SELECTED_TEXT);
    }
    private void selectBook() {
        if (!bookTableView.getSelectionModel().isEmpty()) {
            setSelectBook = bookTableView.getSelectionModel().getSelectedItem();
            if (Message.alertConfirmation(GetBookTableConstants.BOOK_TITLE, setSelectBook.toString(), GetBookTableConstants.OK_BOOK_BUTTON)) {
                bookText.setText(setSelectBook.toString());
                bookText.setVisible(true);
                bookButton.setVisible(true);
                peopleButton.setVisible(false);
                bookTableView.setDisable(true);
                okButton.setDisable(false);

            }
        } else Message.alertInformation(GetBookTableConstants.BOOK_TITLE, GetBookTableConstants.BOOK_NOT_SELECTED_TEXT);
    }
    private void restartBookTableView() {
        books = setSelectPeople.getBooks();
        bookTableView.setItems(FXCollections.observableArrayList(books));
    }
    private void peopleTableViewNull(boolean bol) {
        peopleTableView.setVisible(!bol);
        peopleTableViewNullLabel.setVisible(bol);
    }

    private void restartPeopleTableView() {
        peoples = StartLibrary.runLibrary.hibernateRun
                .load(People.class)
                .stream()
                .filter(people -> !people.getBooks().isEmpty())
                .collect(Collectors.toList());
        peopleTableView.setItems(FXCollections.observableArrayList(peoples));
        peopleTableViewNull(peoples.isEmpty());
    }
    private void run() {
        allVisibleFalse();
        okButton.setDisable(true);
        restartPeopleTableView();

    }
    private void allVisibleFalse() {
        peopleTableView.setVisible(false);
        peopleTableViewNullLabel.setVisible(false);
        peopleText.setVisible(false);
        peopleButton.setVisible(false);
        bookTableView.setVisible(false);
        bookTableViewNullLabel.setVisible(false);
        bookText.setVisible(false);
        bookButton.setVisible(false);

    }
    @FXML
    void initialize() {
        run();

        lastNameTableColumn.setText(PeopleConstants.LAST_NAME_LABEL);
        firstNameTableColumn.setText(PeopleConstants.FIRST_NAME_LABEL);
        birthdayTableColumn.setText(PeopleConstants.BIRTHDAY_LABEL);
        genderTableColumn.setText(PeopleConstants.GENDER_LABEL);

        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.LAST_NAME));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.FIRST_NAME));
        birthdayTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.BIRTHDAY));
        genderTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.GENDER));

        serialTableColumn.setText(BookConstants.SERIAL_LABEL);
        nameBookTableColumn.setText(BookConstants.NAME_BOOK_LABEL);
        publishingYearTableColumn.setText(BookConstants.PUBLISHING_YEAR_LABEL);
        publishingHouseTableColumn.setText(BookConstants.PUBLISHING_HOUSE_LABEL);

        publishingHouseTableColumn.setCellValueFactory(new PropertyValueFactory<>(BookTC.PUBLISHING_HOUSE));
        serialTableColumn.setCellValueFactory(new PropertyValueFactory<>(BookTC.SERIAL));
        nameBookTableColumn.setCellValueFactory(new PropertyValueFactory<>(BookTC.NAME_BOOK));
        publishingYearTableColumn.setCellValueFactory(new PropertyValueFactory<>(BookTC.PUBLISHING_YEAR));

        titleLabel.setText(GetBookTableConstants.TITLE);
        okButton.setText(GetBookTableConstants.OK_BUTTON);
        cancelButton.setText(GetBookTableConstants.CANCEL_BUTTON);
        bookButton.setText(GetBookTableConstants.BOOK_BUTTON);
        peopleButton.setText(GetBookTableConstants.PEOPLE_BUTTON);
    }
}
