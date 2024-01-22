package org.bashtan.library.controller;

import org.bashtan.library.application.Back;
import org.bashtan.library.application.Check;
import org.bashtan.library.application.Message;
import org.bashtan.library.application.StartLibrary;
import org.bashtan.library.constants.BookConstants;
import org.bashtan.library.constants.PeopleConstants;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.bashtan.library.constants.BookTC;
import org.bashtan.library.table.Book;
import org.hibernate.HibernateException;

import java.time.Year;
import java.util.List;

public class BookTableController {
    private List<Book> books;
    private Book selectBook;
    private final ToggleGroup actionGroup = new ToggleGroup();
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<Book> bookTableView = new TableView<>();
    @FXML
    private TableColumn<Book,String> serialTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<Book,String> nameBookTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<Book,Year> publishingYearTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<Book,String> publishingHouseTableColumn = new TableColumn<>();

    @FXML
    private TextField nameBookTextField;
    @FXML
    private TextField publishingYearTextField;
    @FXML
    private TextField publishingHouseTextField;
    @FXML
    private TextField serialTextField;
    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label nameBookLengthLabel;
    @FXML
    private Label publishingYearLengthLabel;
    @FXML
    private Label publishingHouseLengthLabel;
    @FXML
    private Label serialLengthLabel;
    @FXML
    private Label descriptionLengthLabel;
    @FXML
    private Label tableViewNullLabel;

    @FXML
    private RadioButton addRadioButton;
    @FXML
    private RadioButton editRadioButton;
    @FXML
    private RadioButton deleteRadioButton;

    @FXML
    private Button actionButton;
    @FXML
    private Button cancelButton;

    @FXML
    void onActionCancelButton(ActionEvent event) {
        new Back().back(anchorPane, "/org/bashtan/library/controller/main.fxml");
    }

    @FXML
    void onActionButton(ActionEvent event) {
        Object command = actionGroup.getSelectedToggle();
        if (command == addRadioButton) {
            StartLibrary.runLibrary.hibernateRun.persist(Book.builder()
                    .nameBook(nameBookTextField.getText())
                    .publishingYear(publishingYearTextField.getText())
                    .publishingHouse(publishingHouseTextField.getText())
                    .serial(serialTextField.getText())
                    .description(descriptionTextArea.getText())
                    .build());
            restartTableView();
            disable(true);
            reset();
            actionGroupFalse();
            actionButton.setDisable(true);
        } else if (command == editRadioButton) {
            selectBook.setNameBook(nameBookTextField.getText());
            selectBook.setPublishingYear(publishingYearTextField.getText());
            selectBook.setPublishingHouse(publishingHouseTextField.getText());
            selectBook.setDescription(descriptionTextArea.getText());
            StartLibrary.runLibrary.hibernateRun.merge(selectBook);
            restartTableView();
            disable(true);
            reset();
            actionGroupFalse();
            actionButton.setDisable(true);
        } else if (command == deleteRadioButton) {
            try {
                if(selectBook.getPeople()!=null)
                {
                    Message.alertError(PeopleConstants.TEXT_DELETE, PeopleConstants.TEXT_ERROR_DELETE);
                } else{
                    StartLibrary.runLibrary.hibernateRun.remove(selectBook);
                    restartTableView();
                    reset();
                    actionGroupFalse();
                    disable(true);
                    actionButton.setDisable(true);
                }

            } catch (HibernateException exception)
            {
                Message.alertError(PeopleConstants.TEXT_DELETE, PeopleConstants.TEXT_ERROR_DELETE);
            }
            restartTableView();
            reset();
        }
    }

    @FXML
    void onActionAddRadioButton(ActionEvent event) {
        reset();
        disable(false);
        actionButton.setText(BookConstants.TEXT_ADD);
        nameBookTextField.requestFocus();
    }

    @FXML
    void onActionEditRadioButton(ActionEvent event) {
        if (!bookTableView.getSelectionModel().isEmpty()) {
            disable(false);
            actionButton.setText(BookConstants.TEXT_EDIT);
            nameBookTextField.requestFocus();
        } else {
            reset();
            actionGroupFalse();
            disable(false);
            Message.alertError(BookConstants.TEXT_EDIT, BookConstants.TEXT_ID_IS_EMPTY);
        }
    }

    @FXML
    void onActionDeleteRadioButton(ActionEvent event) {
        if (!bookTableView.getSelectionModel().isEmpty()) {
            actionButton.setText(BookConstants.TEXT_DELETE);
            actionButton.setDisable(false);
            disable(true);
        } else Message.alertError(BookConstants.TEXT_DELETE, BookConstants.TEXT_ID_IS_EMPTY);
    }
    @FXML
    void tableViewOnMouseClicked(MouseEvent event) {
        select();
    }
    @FXML
    void tableViewOnKeyPressed(KeyEvent event) {
        select();
    }

    @FXML
    void onActonNameBookTextField(ActionEvent event) {
        Check.check(nameBookTextField, publishingYearTextField, BookConstants.NAME_BOOK_LABEL, BookConstants.FIELD_IS_NOT_EMPTY);
    }

    @FXML
    void onActionPublishingYearTextField(ActionEvent event) {
        Check.check(publishingYearTextField,publishingHouseTextField,BookConstants.PUBLISHING_YEAR_LABEL,BookConstants.FIELD_IS_NOT_EMPTY);
    }

    @FXML
    void onActionPublishingHouseTextField(ActionEvent event) {
        Object command = actionGroup.getSelectedToggle();
        if (command==editRadioButton){
            descriptionTextArea.setDisable(false);
            actionButton.setDisable(false);
            descriptionTextArea.requestFocus();
        } else {
            serialTextField.setDisable(false);
            serialTextField.requestFocus();
        }

    }

    @FXML
    void onActionSerialTextField(ActionEvent event) {
        if (serialTextField.getText() == null || serialTextField.getText().isEmpty())
            Message.alertError(BookConstants.SERIAL_LABEL, BookConstants.FIELD_IS_NOT_EMPTY);
        else if (books.stream().anyMatch(book -> book.getSerial().equals(serialTextField.getText()))){
            Message.alertError(BookConstants.SERIAL_LABEL,BookConstants.TEXT_SERIAL_FALSE);
        } else {
            descriptionTextArea.setDisable(false);
            actionButton.setDisable(false);
            descriptionTextArea.requestFocus();
        }
    }

    @FXML
    void onKeyReleasedNameBookTextField(KeyEvent event) {
        Check.checkLength(nameBookTextField, nameBookLengthLabel, BookConstants.LENGTH_NAME_BOOK, BookConstants.NAME_BOOK_LABEL, BookConstants.TEXT_LENGTH_NAME_BOOK);
    }

    @FXML
    void onKeyReleasedPublishingYearTextField(KeyEvent event) {
        Check.checkLength(publishingYearTextField, publishingYearLengthLabel, BookConstants.LENGTH_PUBLISHING_YEAR, BookConstants.PUBLISHING_YEAR_LABEL, BookConstants.TEXT_LENGTH_PUBLISHING_YEAR);
    }

    @FXML
    void onKeyReleasedPublishingHouseTextField(KeyEvent event) {
        Check.checkLength(publishingHouseTextField, publishingHouseLengthLabel, BookConstants.LENGTH_PUBLISHING_HOUSE, BookConstants.PUBLISHING_HOUSE_LABEL, BookConstants.TEXT_LENGTH_PUBLISHING_HOUSE);
    }

    @FXML
    void onKeyReleasedSerialTextField(KeyEvent event) {
        Check.checkLength(serialTextField, serialLengthLabel, BookConstants.LENGTH_SERIAL, BookConstants.SERIAL_LABEL, BookConstants.TEXT_LENGTH_SERIAL);
    }

    @FXML
    void onKeyReleasedDescriptionTextArea(KeyEvent event) {
        Check.checkLength(descriptionTextArea, descriptionLengthLabel, BookConstants.LENGTH_DESCRIPTION, BookConstants.DESCRIPTION_LABEL, BookConstants.TEXT_LENGTH_DESCRIPTION);
    }
    private void select() {
        if (!bookTableView.getSelectionModel().isEmpty()) {
        selectBook = bookTableView.getSelectionModel().getSelectedItem();
        setData(selectBook);
        disable(true);
        actionGroupFalse();
        Check.checkLength(nameBookTextField, nameBookLengthLabel, BookConstants.LENGTH_NAME_BOOK, BookConstants.NAME_BOOK_LABEL, BookConstants.TEXT_LENGTH_NAME_BOOK);
        Check.checkLength(publishingYearTextField, publishingYearLengthLabel, BookConstants.LENGTH_PUBLISHING_YEAR, BookConstants.PUBLISHING_YEAR_LABEL, BookConstants.TEXT_LENGTH_PUBLISHING_YEAR);
        Check.checkLength(publishingHouseTextField, publishingHouseLengthLabel, BookConstants.LENGTH_PUBLISHING_HOUSE, BookConstants.PUBLISHING_HOUSE_LABEL, BookConstants.TEXT_LENGTH_PUBLISHING_HOUSE);
        Check.checkLength(serialTextField, serialLengthLabel, BookConstants.LENGTH_SERIAL, BookConstants.SERIAL_LABEL, BookConstants.TEXT_LENGTH_SERIAL);
        Check.checkLength(descriptionTextArea, descriptionLengthLabel, BookConstants.LENGTH_DESCRIPTION, BookConstants.DESCRIPTION_LABEL, BookConstants.TEXT_LENGTH_DESCRIPTION);
        } else Message.alertInformation(BookConstants.TEXT_BOOK_LABEL, BookConstants.BOOK_NOT_SELECTED_TEXT);

    }
    private void disable(boolean bool) {
        nameBookTextField.setDisable(bool);
        publishingHouseTextField.setDisable(true);
        publishingYearTextField.setDisable(true);
        serialTextField.setDisable(true);
        descriptionTextArea.setDisable(true);
    }
    private void tableViewNull(boolean bol) {
        bookTableView.setVisible(bol);
        editRadioButton.setVisible(bol);
        deleteRadioButton.setVisible(bol);
        tableViewNullLabel.setVisible(!bol);
        tableViewNullLabel.setText(BookConstants.TableViewNull);
    }
    private void restartTableView() {
        actionGroupFalse();
        disable(true);
        books = StartLibrary.runLibrary.hibernateRun.load(Book.class);
        tableViewNull(!books.isEmpty());
        bookTableView.setItems(FXCollections.observableArrayList(books));
    }
    private void setData(Book book) {
        nameBookTextField.setText(book.getNameBook());
        publishingHouseTextField.setText(book.getPublishingHouse());
        publishingYearTextField.setText(book.getPublishingYear());
        serialTextField.setText(book.getSerial());
        descriptionTextArea.setText(book.getDescription());
    }
    private void reset() {
        nameBookTextField.setText(null);
        publishingHouseTextField.setText(null);
        publishingYearTextField.setText(null);
        serialTextField.setText(null);
        descriptionTextArea.setText(null);

        nameBookLengthLabel.setText(null);
        publishingYearLengthLabel.setText(null);
        publishingHouseLengthLabel.setText(null);
        serialLengthLabel.setText(null);
        descriptionLengthLabel.setText(null);
    }
    private void actionGroupFalse() {
        if (!(actionGroup.getSelectedToggle() == null)) actionGroup.getSelectedToggle().setSelected(false);
    }

    @FXML
    void initialize() {
        addRadioButton.setToggleGroup(actionGroup);
        editRadioButton.setToggleGroup(actionGroup);
        deleteRadioButton.setToggleGroup(actionGroup);

        addRadioButton.setText(BookConstants.TEXT_ADD);
        editRadioButton.setText(BookConstants.TEXT_EDIT);
        deleteRadioButton.setText(BookConstants.TEXT_DELETE);

        cancelButton.setText(BookConstants.TEXT_CANCEL);
        actionButton.setText(BookConstants.TEXT_OK);

        nameBookTextField.setPromptText(BookConstants.NAME_BOOK_LABEL);
        publishingYearTextField.setPromptText(BookConstants.PUBLISHING_YEAR_LABEL);
        publishingHouseTextField.setPromptText(BookConstants.PUBLISHING_HOUSE_LABEL);
        serialTextField.setPromptText(BookConstants.SERIAL_LABEL);
        descriptionTextArea.setPromptText(BookConstants.DESCRIPTION_LABEL);

        serialTableColumn.setText(BookConstants.SERIAL_LABEL);
        nameBookTableColumn.setText(BookConstants.NAME_BOOK_LABEL);
        publishingYearTableColumn.setText(BookConstants.PUBLISHING_YEAR_LABEL);
        publishingHouseTableColumn.setText(BookConstants.PUBLISHING_HOUSE_LABEL);

        publishingHouseTableColumn.setCellValueFactory(new PropertyValueFactory<>(BookTC.PUBLISHING_HOUSE));
        serialTableColumn.setCellValueFactory(new PropertyValueFactory<>(BookTC.SERIAL));
        nameBookTableColumn.setCellValueFactory(new PropertyValueFactory<>(BookTC.NAME_BOOK));
        publishingYearTableColumn.setCellValueFactory(new PropertyValueFactory<>(BookTC.PUBLISHING_YEAR));
        restartTableView();
    }
}