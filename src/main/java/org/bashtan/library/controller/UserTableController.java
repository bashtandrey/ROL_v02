package org.bashtan.library.controller;

import org.bashtan.library.application.StartLibrary;
import org.bashtan.library.application.*;
import org.bashtan.library.constants.PeopleConstants;
import org.bashtan.library.constants.UserConstants;
import javafx.beans.property.SimpleStringProperty;
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
import org.bashtan.library.constructor.Level;
import org.bashtan.library.table.People;
import org.bashtan.library.table.User;
import org.hibernate.HibernateException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UserTableController {
    private List<User> userList;
    private List<People> peopleList;
    private People selectPeople;
    private User selectUser;
    private ToggleGroup actionGroup = new ToggleGroup();
    private LocalDate birthdayLocalDate;
    private boolean addFlag;


    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<People> peopleTableView = new TableView<>();
    @FXML
    private TableColumn<People,String> lastNamePeopleTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People,String> firstNamePeopleTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People, LocalDate> birthdayPeopleTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<People, Gender> genderPeopleTableColumn = new TableColumn<>();

    @FXML
    private TableView<User> usersTableView = new TableView<>();

    @FXML
    private TableColumn<User,String> loginTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<User, Level> levelTableColumn = new TableColumn<>();

    @FXML
    private TableColumn<User, People> peopleTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<User,String> lastNameTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<User,String> firstNameTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<User, String> birthdayTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<User,String> emailTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<User,String> phoneTableColumn = new TableColumn<>();
    @FXML
    private TableColumn<User, String> genderTableColumn = new TableColumn<>();

    @FXML
    ComboBox<Gender> genderComboBox;
    @FXML
    ComboBox<Level> levelComboBox;

    @FXML
    private RadioButton addRadioButton;
    @FXML
    private RadioButton deleteRadioButton;
    @FXML
    private RadioButton editRadioButton;

    @FXML
    private Button actionButton;
    @FXML
    private Button cancelButton;

    @FXML
    private TextField loginTextField;
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
    private PasswordField passwordField;

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
    void onActionAddRadioButton(ActionEvent event) {
        Alert customAlert = Message.createCustomAlert(UserConstants.TEXT_ADD,UserConstants.TEXT_CUSTOMER_ALERT);
        Optional<ButtonType> result = customAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            reset();
            disable(false);
            loginTextField.requestFocus();
            actionButton.setText(UserConstants.TEXT_ADD);
            addFlag=true;
        } else if (result.isPresent() && result.get() == ButtonType.NO) {
            usersTableView.setVisible(false);
            peopleTableView.setVisible(true);
            reset();
            disable(true);
            editRadioButton.setDisable(true);
            deleteRadioButton.setDisable(true);
            addRadioButton.setDisable(true);
            actionButton.setText(UserConstants.TEXT_ADD);
            peopleRestartTableView();
            addFlag=false;
        } else actionGroupFalse();
    }

    @FXML
    void onActionEditRadioButton(ActionEvent event) {
        if (!usersTableView.getSelectionModel().isEmpty()) {
            disable(false);
            actionButton.setText(UserConstants.TEXT_EDIT);
            loginTextField.requestFocus();
        } else {
            reset();
            actionGroupFalse();
            disable(true);
            Message.alertError(UserConstants.TEXT_EDIT, UserConstants.TEXT_ID_IS_EMPTY);
        }
    }
    @FXML
    void onActionDeleteRadioButton(ActionEvent event) {
        if (!usersTableView.getSelectionModel().isEmpty()) {
            actionButton.setText(UserConstants.TEXT_DELETE);
            actionButton.setDisable(false);
            disable(true);
        } else Message.alertError(UserConstants.TEXT_DELETE, UserConstants.TEXT_ID_IS_EMPTY);
    }
    @FXML
    void onActionLoginTextField(ActionEvent event) {
        Check.check(loginTextField,passwordField,UserConstants.Login_LABEL,UserConstants.TEXT_IS_EMPTY);}
    @FXML
    void onActionPasswordField(ActionEvent event) {
        Check.check(passwordField,levelComboBox,UserConstants.PASSWORD_LABEL,UserConstants.TEXT_IS_EMPTY);
    }
    @FXML
    void onActionLevelComboBox(ActionEvent event) {
        if(addFlag) {
            if (levelComboBox.getValue() != null) {
                lastNameTextField.setDisable(false);
                lastNameTextField.requestFocus();
            }
        } else {
            actionButton.setDisable(false);
            actionButton.requestFocus();
        }
    }
    @FXML
    void onActionLastNameTextField(ActionEvent event) {
        Check.check(lastNameTextField,firstNameTextField,UserConstants.LAST_NAME_LABEL,UserConstants.TEXT_IS_EMPTY);
    }
    @FXML
    void onActionFirstNameTextField(ActionEvent event) {
        Check.check(firstNameTextField,genderComboBox,UserConstants.FIRST_NAME_LABEL,UserConstants.TEXT_IS_EMPTY);
    }
    @FXML
    void onActionGenderComboBox(ActionEvent event) {
        if (genderComboBox.getValue()!=null){
            birthdayTextField.setDisable(false);
            birthdayTextField.requestFocus();
        }
    }
    @FXML
    void onActionBirthdayTextField(ActionEvent event) {
        emailTextField.setDisable(false);
        emailTextField.requestFocus();
    }
    @FXML
    void onActionEmailTextField(ActionEvent event) {
        phoneTextField.setDisable(false);
        phoneTextField.requestFocus();
    }
    @FXML
    void onActionPhoneTextField(ActionEvent event) {
        actionButton.setDisable(false);
        actionButton.requestFocus();
    }
    @FXML
    void onKeyPressedLevelComboBox(KeyEvent event) {
        if(addFlag) {
            if (levelComboBox.getValue() != null) {
                lastNameTextField.setDisable(false);
                lastNameTextField.requestFocus();
            }
        } else {
            actionButton.setDisable(false);
            actionButton.requestFocus();
        }
    }
    @FXML
    void onKeyPressedGenderComboBox(KeyEvent event) {
        if (genderComboBox.getValue()!=null){
            birthdayTextField.setDisable(false);
            birthdayTextField.requestFocus();
        }
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
    @FXML
    void onActionButton(ActionEvent event) {
        String hashPassword = new Coder(passwordField.getText()).getHash();
        Object command = actionGroup.getSelectedToggle();
        birthdayLocalDate = (birthdayTextField.getText()==null || birthdayTextField.getText().isEmpty()) ? null :LocalDate.parse(birthdayTextField.getText());
        if (command == addRadioButton) {
            if(addFlag) {
                People people = People.builder()
                        .lastName(lastNameTextField.getText())
                        .firstName(firstNameTextField.getText())
                        .gender(genderComboBox.getValue())
                        .birthday(birthdayLocalDate)
                        .email(emailTextField.getText())
                        .phone(phoneTextField.getText())
                        .build();
                StartLibrary.runLibrary.hibernateRun.persist(people);
                StartLibrary.runLibrary.hibernateRun.persist(User.builder()
                        .login(loginTextField.getText())
                        .password(hashPassword)
                        .level(levelComboBox.getValue())
                        .people(people)
                        .build());
            }  else {
                StartLibrary.runLibrary.hibernateRun.persist(User.builder()
                        .login(loginTextField.getText())
                        .password(hashPassword)
                        .level(levelComboBox.getValue())
                        .people(selectPeople)
                        .build());
            }
            userRestartTableView();
            reset();
        } else if (command == editRadioButton) {
            selectUser.setLogin(loginTextField.getText());
            selectUser.setPassword(hashPassword);
            selectUser.setLevel(levelComboBox.getValue());
            StartLibrary.runLibrary.hibernateRun.merge(selectUser);
            userRestartTableView();
            reset();
            actionButton.setDisable(true);
            actionGroupFalse();
        } else if (command == deleteRadioButton) {
            try {
                StartLibrary.runLibrary.hibernateRun.remove(usersTableView.getSelectionModel().getSelectedItem());
                userRestartTableView();
                reset();
            } catch (HibernateException exception)
            {
                Message.alertError(PeopleConstants.TEXT_DELETE, PeopleConstants.TEXT_ERROR_DELETE);
            }
            reset();
            userRestartTableView();
        }
    }

    @FXML
    void onActionCancelButton(ActionEvent event) {
        new Back().back(anchorPane, "/org/bashtan/library/controller/main.fxml");
    }
    @FXML
    void userTableViewOnMouseClicked(MouseEvent event) {userTableSelect();}
    @FXML
    void userTableViewOnKeyPressed(KeyEvent event) {userTableSelect();}
    @FXML
    void peopleTableViewOnMouseClicked(MouseEvent event) {peopleTableSelect();}
    @FXML
    void peopleTableViewOnKeyTyped(KeyEvent event) {peopleTableSelect();}
    private void userTableSelect() {
        if (!usersTableView.getSelectionModel().isEmpty()) {
            selectUser = usersTableView.getSelectionModel().getSelectedItem();
            setData(selectUser);
            disable(true);
            actionGroupFalse();
            Check.checkLength(lastNameTextField, lastNameLengthLabel, PeopleConstants.LENGTH_LAST_NAME, PeopleConstants.LAST_NAME_LABEL, PeopleConstants.TEXT_LENGTH_LAST_NAME);
            Check.checkLength(firstNameTextField, firstNameLengthLabel, PeopleConstants.LENGTH_FIRST_NAME, PeopleConstants.FIRST_NAME_LABEL, PeopleConstants.TEXT_LENGTH_FIRST_NAME);
            Check.checkLength(emailTextField, emailLengthLabel, PeopleConstants.LENGTH_EMAIL, PeopleConstants.EMAIL_LABEL, PeopleConstants.TEXT_LENGTH_EMAIL);
            Check.checkLength(phoneTextField, phoneLengthLabel, PeopleConstants.LENGTH_PHONE, PeopleConstants.PHONE_LABEL, PeopleConstants.TEXT_LENGTH_PHONE);
        } else Message.alertInformation(PeopleConstants.TEXT_PEOPLE_LABEL, PeopleConstants.PEOPLE_NOT_SELECTED_TEXT);
    }
    private void peopleTableSelect() {
        if (!peopleTableView.getSelectionModel().isEmpty()) {
            selectPeople = peopleTableView.getSelectionModel().getSelectedItem();
            setData(selectPeople);
            disable(true);
            Check.checkLength(lastNameTextField, lastNameLengthLabel, PeopleConstants.LENGTH_LAST_NAME, PeopleConstants.LAST_NAME_LABEL, PeopleConstants.TEXT_LENGTH_LAST_NAME);
            Check.checkLength(firstNameTextField, firstNameLengthLabel, PeopleConstants.LENGTH_FIRST_NAME, PeopleConstants.FIRST_NAME_LABEL, PeopleConstants.TEXT_LENGTH_FIRST_NAME);
            Check.checkLength(emailTextField, emailLengthLabel, PeopleConstants.LENGTH_EMAIL, PeopleConstants.EMAIL_LABEL, PeopleConstants.TEXT_LENGTH_EMAIL);
            Check.checkLength(phoneTextField, phoneLengthLabel, PeopleConstants.LENGTH_PHONE, PeopleConstants.PHONE_LABEL, PeopleConstants.TEXT_LENGTH_PHONE);
            loginTextField.setDisable(false);
            loginTextField.requestFocus();
        } else Message.alertInformation(PeopleConstants.TEXT_PEOPLE_LABEL, PeopleConstants.PEOPLE_NOT_SELECTED_TEXT);
    }
    private void setData(Object clas) {
        if (clas.getClass() == User.class) {
                User user = (User) clas;
                loginTextField.setText(user.getLogin());
                passwordField.setText(user.getPassword());
                levelComboBox.setValue(user.getLevel());

                lastNameTextField.setText(user.getPeople().getLastName());
                firstNameTextField.setText(user.getPeople().getFirstName());
                emailTextField.setText(user.getPeople().getEmail());
                phoneTextField.setText(user.getPeople().getPhone());
                genderComboBox.setValue(user.getPeople().getGender());
                if (user.getPeople().getBirthday()== null){
                    birthdayTextField.setText(null);
                } else birthdayTextField.setText(String.valueOf(user.getPeople().getBirthday()));
        } else if (clas.getClass() == People.class){
            People people = (People) clas;
            lastNameTextField.setText(people.getLastName());
            firstNameTextField.setText(people.getFirstName());
            emailTextField.setText(people.getEmail());
            phoneTextField.setText(people.getPhone());
            genderComboBox.setValue(people.getGender());
            if (people.getBirthday()== null){
                birthdayTextField.setText(null);
            } else birthdayTextField.setText(String.valueOf(people.getBirthday()));
        }
    }
    private void userRestartTableView() {
        peopleTableView.setVisible(false);
        actionGroupFalse();
        disable(true);
        userList = StartLibrary.runLibrary.hibernateRun.load(User.class);
        usersTableView.setItems(FXCollections.observableArrayList(userList));
        userTableViewNull(!userList.isEmpty());

    }
    private void peopleRestartTableView() {
        String queryString = "SELECT p FROM People p WHERE p.id NOT IN (SELECT u.people.id FROM User u WHERE u.people IS NOT NULL)";
        peopleList = StartLibrary.runLibrary.hibernateRun.sessionFactory.openSession()
                .createQuery(queryString, People.class)
                .getResultList();
        peopleTableView.setItems(FXCollections.observableArrayList(peopleList));
    }
    private void actionGroupFalse() {
        if (!(actionGroup.getSelectedToggle() == null)) actionGroup.getSelectedToggle().setSelected(false);
    }
    private void disable(boolean bool) {
        loginTextField.setDisable(bool);
        passwordField.setDisable(true);
        lastNameTextField.setDisable(true);
        firstNameTextField.setDisable(true);
        birthdayTextField.setDisable(true);
        emailTextField.setDisable(true);
        phoneTextField.setDisable(true);
        genderComboBox.setDisable(true);
        levelComboBox.setDisable(true);
        addRadioButton.setDisable(false);
        editRadioButton.setDisable(false);
        deleteRadioButton.setDisable(false);

    }
    private void userTableViewNull(boolean bol) {
        usersTableView.setVisible(bol);
        editRadioButton.setVisible(bol);
        deleteRadioButton.setVisible(bol);
        tableViewNullLabel.setVisible(!bol);
        tableViewNullLabel.setText(UserConstants.TABLE_VIEW_NULL);
    }

    private void reset() {
        loginTextField.setText(null);
        passwordField.setText(null);
        levelComboBox.setValue(null);
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
        @FXML
        void initialize() {
            userRestartTableView();
            levelComboBox.setItems(FXCollections.observableArrayList(Level.values()));
            genderComboBox.setItems(FXCollections.observableArrayList(Gender.values()));

            addRadioButton.setToggleGroup(actionGroup);
            editRadioButton.setToggleGroup(actionGroup);
            deleteRadioButton.setToggleGroup(actionGroup);

            loginTableColumn.setCellValueFactory(new PropertyValueFactory<>(UserConstants.LOGIN));
            levelTableColumn.setCellValueFactory(new PropertyValueFactory<>(UserConstants.LEVEL));
            lastNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeople().getLastName()));
            firstNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeople().getFirstName()));
            emailTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeople().getEmail()));
            phoneTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeople().getPhone()));
            birthdayTableColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getPeople().getBirthday() != null ?
                            cellData.getValue().getPeople().getBirthday().toString() : ""));
            genderTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeople().getGender().toString()));

            lastNamePeopleTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.LAST_NAME));
            firstNamePeopleTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.FIRST_NAME));
            birthdayPeopleTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.BIRTHDAY));
            genderPeopleTableColumn.setCellValueFactory(new PropertyValueFactory<>(PeopleTC.GENDER));

            loginTextField.setPromptText(UserConstants.Login_LABEL);
            passwordField.setPromptText(UserConstants.PASSWORD_LABEL);
            levelComboBox.setPromptText(UserConstants.LEVEL_LABEL);

            lastNameTextField.setPromptText(UserConstants.LAST_NAME_LABEL);
            firstNameTextField.setPromptText(UserConstants.FIRST_NAME_LABEL);
            genderComboBox.setPromptText(UserConstants.GENDER_LABEL);
            birthdayTextField.setPromptText(UserConstants.BIRTHDAY_LABEL);
            emailTextField.setPromptText(UserConstants.EMAIL_LABEL);
            phoneTextField.setPromptText(UserConstants.PHONE_LABEL);

            loginTableColumn.setText(UserConstants.Login_LABEL);
            levelTableColumn.setText(UserConstants.LEVEL_LABEL);
            peopleTableColumn.setText(UserConstants.People_LABEL);

            lastNameTableColumn.setText(UserConstants.LAST_NAME_LABEL);
            firstNameTableColumn.setText(UserConstants.FIRST_NAME_LABEL);
            genderTableColumn.setText(UserConstants.GENDER_LABEL);
            birthdayTableColumn.setText(UserConstants.BIRTHDAY_LABEL);
            emailTableColumn.setText(UserConstants.EMAIL_LABEL);
            phoneTableColumn.setText(UserConstants.PHONE_LABEL);

            addRadioButton.setText(UserConstants.TEXT_ADD);
            editRadioButton.setText(UserConstants.TEXT_EDIT);
            deleteRadioButton.setText(UserConstants.TEXT_DELETE);

            actionButton.setText(UserConstants.TEXT_OK);
            cancelButton.setText(UserConstants.TEXT_CANCEL);
        }
    }
