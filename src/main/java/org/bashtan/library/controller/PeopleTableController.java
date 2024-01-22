package org.bashtan.library.controller;

import org.bashtan.library.application.Back;
import org.bashtan.library.application.Check;
import org.bashtan.library.application.Message;
import org.bashtan.library.application.StartLibrary;
import org.bashtan.library.constants.PeopleConstants;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.bashtan.library.constants.PeopleTC;
import org.bashtan.library.constructor.Gender;
import org.bashtan.library.table.People;
import org.hibernate.HibernateException;

import java.time.LocalDate;
import java.util.List;

public class PeopleTableController {

    private  List<People> peoples;
    private  People selectPeople;
    private ToggleGroup actionGroup = new ToggleGroup();
    private LocalDate birthdayLocalDate;
    @FXML
    ComboBox<Gender> genderComboBox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private RadioButton addRadioButton;
    @FXML
    private RadioButton deleteRadioButton;
    @FXML
    private RadioButton editRadioButton;

    @FXML
    private TableView<People> peopleTableView = new TableView<>();
    @FXML
    private TableColumn<People,String> lastNameTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People,String> firstNameTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People,LocalDate> birthdayTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People,String> emailTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People,String> phoneTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People,Gender> genderTableColumn = new TableColumn<>();

    @FXML
    private Button actionButton;
    @FXML
    private Button cancelButton;


    @FXML
    private TextField emailTextField;
    @FXML
    private TextField birthdayTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneTextField;

    @FXML
    private Label lastNameLengthLabel;
    @FXML
    private Label firstNameLengthLabel;
    @FXML
    private Label emailLengthLabel;
    @FXML
    private Label phoneLengthLabel;
    @FXML
    private Label tableViewNullLabel;
    @FXML
    void tableViewOnMouseClicked(MouseEvent event) {
        select();
    }

    @FXML
    void tableViewOnKeyPressed(KeyEvent event) {
        select();
    }

    @FXML
    void onActionCancelButton(ActionEvent event) {
        new Back().back(anchorPane, "/org/bashtan/library/controller/main.fxml");
    }

    @FXML
    void onActionButton(ActionEvent event) {
        Object command = actionGroup.getSelectedToggle();
        if (birthdayTextField.getText()==null || birthdayTextField.getText().isEmpty()){
            birthdayLocalDate = null;
        } else {
            birthdayLocalDate = LocalDate.parse(birthdayTextField.getText());
        }

        if (command == addRadioButton) {
            StartLibrary.runLibrary.hibernateRun.persist(People.builder()
                    .lastName(lastNameTextField.getText())
                    .firstName(firstNameTextField.getText())
                    .gender(genderComboBox.getValue())
                    .birthday(birthdayLocalDate)
                    .email(emailTextField.getText())
                    .phone(phoneTextField.getText())
                    .build());
            restartTableView();
            reset();
        } else if (command == editRadioButton) {
            selectPeople.setLastName(lastNameTextField.getText());
            selectPeople.setFirstName(firstNameTextField.getText());
            selectPeople.setEmail(emailTextField.getText());
            selectPeople.setBirthday(birthdayLocalDate);
            selectPeople.setGender(genderComboBox.getValue());
            selectPeople.setPhone(phoneTextField.getText());
            StartLibrary.runLibrary.hibernateRun.merge(selectPeople);
            restartTableView();
            reset();
            actionButton.setDisable(true);
            actionGroupFalse();
        } else if (command == deleteRadioButton) {
            try {
                StartLibrary.runLibrary.hibernateRun.remove(peopleTableView.getSelectionModel().getSelectedItem());
                restartTableView();
                reset();
            } catch (HibernateException exception)
            {
                Message.alertError(PeopleConstants.TEXT_DELETE, PeopleConstants.TEXT_ERROR_DELETE);
            }
            reset();
            restartTableView();
        }
    }

    @FXML
    void onActionAddRadioButton(ActionEvent event) {
        reset();
        disable(false);
        actionButton.setText(PeopleConstants.TEXT_ADD);
        lastNameTextField.requestFocus();
    }

    @FXML
    void onActionEditRadioButton(ActionEvent event) {
        if (!peopleTableView.getSelectionModel().isEmpty()) {
            disable(false);
            actionButton.setText(PeopleConstants.TEXT_EDIT);
            lastNameTextField.requestFocus();
        } else {
            reset();
            actionGroupFalse();
            disable(false);
            Message.alertError(PeopleConstants.TEXT_EDIT, PeopleConstants.TEXT_ID_IS_EMPTY);
        }
    }

    @FXML
    void onActionDeleteRadioButton(ActionEvent event) {
        if (!peopleTableView.getSelectionModel().isEmpty()) {
            actionButton.setText(PeopleConstants.TEXT_DELETE);
            actionButton.setDisable(false);
            disable(true);
        } else Message.alertError(PeopleConstants.TEXT_DELETE, PeopleConstants.TEXT_ID_IS_EMPTY);
    }


    @FXML
    void onActionLastNameTextField(ActionEvent event) {
        Check.check(lastNameTextField, firstNameTextField, PeopleConstants.LAST_NAME_LABEL, PeopleConstants.TEXT_IS_EMPTY);
    }

    @FXML
    void onActonFirstNameTextField(ActionEvent event) {
        Check.check(firstNameTextField, genderComboBox, PeopleConstants.FIRST_NAME_LABEL, PeopleConstants.TEXT_IS_EMPTY);
    }
    @FXML
    void onActionGenderComboBox(ActionEvent event) {
        birthdayTextField.setDisable(false);
        birthdayTextField.requestFocus();
    }
    @FXML
    void onKeyPressedGenderComboBox(KeyEvent  event) {
        birthdayTextField.setDisable(false);
        birthdayTextField.requestFocus();
    }

    @FXML
    void onActonBirthdayTextField(ActionEvent event) {
     emailTextField.setDisable(false);
     emailTextField.requestFocus();
    }

    @FXML
    void onActionEmailTextField(ActionEvent event) {
        phoneTextField.setDisable(false);
        phoneTextField.requestFocus();
    }

    @FXML
    void onActonPhoneTextField(ActionEvent event) {
        actionButton.setDisable(false);
        actionButton.requestFocus();
    }

    @FXML
    void onKeyPressedLastNameTextField(KeyEvent event) {
        Check.checkLength(lastNameTextField, lastNameLengthLabel, PeopleConstants.LENGTH_LAST_NAME, PeopleConstants.LAST_NAME_LABEL, PeopleConstants.TEXT_LENGTH_LAST_NAME);
    }

    @FXML
    void onKeyPressedFirstNameTextField(KeyEvent event) {
        Check.checkLength(firstNameTextField, firstNameLengthLabel, PeopleConstants.LENGTH_FIRST_NAME, PeopleConstants.FIRST_NAME_LABEL, PeopleConstants.TEXT_LENGTH_FIRST_NAME);
    }

    @FXML
    void onKeyPressedEmailTextField(KeyEvent event) {
        Check.checkLength(emailTextField, emailLengthLabel, PeopleConstants.LENGTH_EMAIL, PeopleConstants.EMAIL_LABEL, PeopleConstants.TEXT_LENGTH_EMAIL);
    }

    @FXML
    void onKeyPressedPhoneTextField(KeyEvent event) {
        Check.checkLength(phoneTextField, phoneLengthLabel, PeopleConstants.LENGTH_PHONE, PeopleConstants.PHONE_LABEL, PeopleConstants.TEXT_LENGTH_PHONE);
    }
    private void select() {
        if (!peopleTableView.getSelectionModel().isEmpty()) {
        selectPeople = peopleTableView.getSelectionModel().getSelectedItem();
        setData(selectPeople);
        disable(true);
        actionGroupFalse();
        Check.checkLength(lastNameTextField, lastNameLengthLabel, PeopleConstants.LENGTH_LAST_NAME, PeopleConstants.LAST_NAME_LABEL, PeopleConstants.TEXT_LENGTH_LAST_NAME);
        Check.checkLength(firstNameTextField, firstNameLengthLabel, PeopleConstants.LENGTH_FIRST_NAME, PeopleConstants.FIRST_NAME_LABEL, PeopleConstants.TEXT_LENGTH_FIRST_NAME);
        Check.checkLength(emailTextField, emailLengthLabel, PeopleConstants.LENGTH_EMAIL, PeopleConstants.EMAIL_LABEL, PeopleConstants.TEXT_LENGTH_EMAIL);
        Check.checkLength(phoneTextField, phoneLengthLabel, PeopleConstants.LENGTH_PHONE, PeopleConstants.PHONE_LABEL, PeopleConstants.TEXT_LENGTH_PHONE);
        } else Message.alertInformation(PeopleConstants.TEXT_PEOPLE_LABEL, PeopleConstants.PEOPLE_NOT_SELECTED_TEXT);
    }
    private void disable(boolean bool) {
        lastNameTextField.setDisable(bool);
        firstNameTextField.setDisable(true);
        birthdayTextField.setDisable(true);
        emailTextField.setDisable(true);
        phoneTextField.setDisable(true);
        genderComboBox.setDisable(true);
    }
    private void tableViewNull(boolean bol) {
        peopleTableView.setVisible(bol);
        editRadioButton.setVisible(bol);
        deleteRadioButton.setVisible(bol);
        tableViewNullLabel.setVisible(!bol);
        tableViewNullLabel.setText(PeopleConstants.TABLE_VIEW_NULL);
    }
    private void restartTableView() {
        actionGroupFalse();
        disable(true);
        peoples = StartLibrary.runLibrary.hibernateRun.load(People.class);
        peopleTableView.setItems(FXCollections.observableArrayList(peoples));
        tableViewNull(!peoples.isEmpty());
    }
    private void setData(People people) {
        lastNameTextField.setText(people.getLastName());
        firstNameTextField.setText(people.getFirstName());
        emailTextField.setText(people.getEmail());
        phoneTextField.setText(people.getPhone());
        genderComboBox.setValue(people.getGender());
        if (people.getBirthday()== null){
            birthdayTextField.setText(null);
        } else birthdayTextField.setText(String.valueOf(people.getBirthday()));
    }
    private void reset() {
        lastNameTextField.setText(null);
        firstNameTextField.setText(null);
        emailTextField.setText(null);
        phoneTextField.setText(null);
        birthdayTextField.setText(null);
        genderComboBox.setValue(null);

        lastNameLengthLabel.setText(null);
        firstNameLengthLabel.setText(null);
        emailLengthLabel.setText(null);
        phoneLengthLabel.setText(null);
    }
    private void actionGroupFalse() {
        if (!(actionGroup.getSelectedToggle() == null)) actionGroup.getSelectedToggle().setSelected(false);
    }

    @FXML
    void initialize() {
        genderComboBox.setItems(FXCollections.observableArrayList(Gender.values()));
        addRadioButton.setToggleGroup(actionGroup);
        editRadioButton.setToggleGroup(actionGroup);
        deleteRadioButton.setToggleGroup(actionGroup);

        cancelButton.setText(PeopleConstants.CANCEL_BUTTON);
        actionButton.setText(PeopleConstants.OK_BUTTON);

        addRadioButton.setText(PeopleConstants.TEXT_ADD);
        editRadioButton.setText(PeopleConstants.TEXT_EDIT);
        deleteRadioButton.setText(PeopleConstants.TEXT_DELETE);

        lastNameTextField.setPromptText(PeopleConstants.LAST_NAME_LABEL);
        firstNameTextField.setPromptText(PeopleConstants.FIRST_NAME_LABEL);
        emailTextField.setPromptText(PeopleConstants.EMAIL_LABEL);
        phoneTextField.setPromptText(PeopleConstants.PHONE_LABEL);
        birthdayTextField.setPromptText(PeopleConstants.BIRTHDAY_FORMAT);

        emailTableColumn.setText(PeopleConstants.EMAIL_LABEL);
        phoneTableColumn.setText(PeopleConstants.PHONE_LABEL);
        lastNameTableColumn.setText(PeopleConstants.LAST_NAME_LABEL);
        firstNameTableColumn.setText(PeopleConstants.FIRST_NAME_LABEL);
        birthdayTableColumn.setText(PeopleConstants.BIRTHDAY_LABEL);
        genderTableColumn.setText(PeopleConstants.GENDER_LABEL);

        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.LAST_NAME));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.FIRST_NAME));
        birthdayTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.BIRTHDAY));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.EMAIL));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.PHONE));
        genderTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.GENDER));

        restartTableView();
    }
}