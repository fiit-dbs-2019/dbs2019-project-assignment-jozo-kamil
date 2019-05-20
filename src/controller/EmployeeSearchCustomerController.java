package controller;

import com.jfoenix.controls.JFXProgressBar;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import org.controlsfx.control.Notifications;
import persistancemanagers.EmployeeManager;
import persistancemanagers.PersonManager;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class EmployeeSearchCustomerController implements Initializable {

    @FXML private AnchorPane rootPane;

    @FXML private Label labelFirstName;
    @FXML private Label labelDate;
    @FXML private Label labelLastName;


    @FXML private TableView<NaturalPerson> tableViewNatural;
    @FXML private TableColumn<NaturalPerson, String> collumnFirstName;
    @FXML private TableColumn<NaturalPerson, String> collumnLastName;
    @FXML private TableColumn<NaturalPerson, String> collumnID;
    @FXML private TableColumn<NaturalPerson, String> collumnPhone;

    @FXML private Button buttonNextDataNatural;
    @FXML private Button buttonPreviousDataNatural;
    @FXML private TextField textFieldSearchInTablesNatural;
    @FXML private TextField textFieldSearchInDatabaseNatural;
    @FXML private Label labelOffsetNatural;
    @FXML private JFXProgressBar progressBarNatural;


    @FXML private TableView<LegalPerson> tableViewLegal;
    @FXML private TableColumn<LegalPerson, String> collumnIco;
    @FXML private TableColumn<LegalPerson, String> collumnDic;
    @FXML private TableColumn<LegalPerson, String> collumnName;
    @FXML private TableColumn<LegalPerson, String> collumnPhoneNumber;

    @FXML private TextField textFieldSearchInTablesLegal;
    @FXML private TextField textFieldSearchInDatabaseLegal;
    @FXML private Button buttonPreviousDataLegal;
    @FXML private Button buttonNextDataLegal;
    @FXML private Label labelOffsetLegal;
    @FXML private JFXProgressBar progressBarLegal;

    private ObservableList<NaturalPerson> observableListNatural;
    private ObservableList<LegalPerson> observableListLegal;

    private FilteredList filer;
    private FilteredList filerLegal;

    private Employee employee;

    private Car car = null;
    private String carVIN = null;

    private Integer offSet = 0;
    private Integer offSetLegal = 0;

    private Boolean isbuttonSearchInDatabasePushedNatural = false;
    private Boolean isbuttonSearchInDatabasePushedLegal = false;

    private Boolean openedFromContractScene = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        collumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        collumnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        collumnPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // enable funcionality of ordering the collumns by click on it
        collumnFirstName.setSortType(TableColumn.SortType.ASCENDING);
        collumnLastName.setSortType(TableColumn.SortType.ASCENDING);
        collumnID.setSortType(TableColumn.SortType.ASCENDING);
        collumnPhone.setSortType(TableColumn.SortType.ASCENDING);

        tableViewNatural.getSortOrder().add(collumnFirstName);
        tableViewNatural.getSortOrder().add(collumnLastName);
        tableViewNatural.getSortOrder().add(collumnID);
        tableViewNatural.getSortOrder().add(collumnPhone);

        buttonNextDataNatural.setDisable(true);
        buttonPreviousDataNatural.setDisable(true);

        progressBarNatural.setVisible(false);

        //legal person
        collumnIco.setCellValueFactory(new PropertyValueFactory<>("ID"));
        collumnDic.setCellValueFactory(new PropertyValueFactory<>("dic"));
        collumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        collumnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // enable funcionality of ordering the collumns by click on it
        collumnIco.setSortType(TableColumn.SortType.ASCENDING);
        collumnDic.setSortType(TableColumn.SortType.ASCENDING);
        collumnName.setSortType(TableColumn.SortType.ASCENDING);
        collumnPhoneNumber.setSortType(TableColumn.SortType.ASCENDING);

        tableViewLegal.getSortOrder().add(collumnIco);
        tableViewLegal.getSortOrder().add(collumnDic);
        tableViewLegal.getSortOrder().add(collumnName);
        tableViewLegal.getSortOrder().add(collumnPhoneNumber);

        buttonNextDataLegal.setDisable(true);
        buttonPreviousDataLegal.setDisable(true);

        progressBarLegal.setVisible(false);
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setCarVIN(String carVIN) {
        this.carVIN = carVIN;
    }

    public void setOpenedFromContractScene(Boolean openedFromContractScene) {
        this.openedFromContractScene = openedFromContractScene;
    }

    public void detailForNaturalPersonSelected(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/employee_natural_person_detail.fxml"));

        EmployeeNaturalPersonDetailController employeeNaturalPersonDetailController;

        try {
            Parent detailWindow = (Parent) loader.load();

            employeeNaturalPersonDetailController = loader.getController();
            NaturalPerson naturalPersonDetail = tableViewNatural.getSelectionModel().getSelectedItem();
            employeeNaturalPersonDetailController.setNaturalPerson(naturalPersonDetail);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Detail");
            stage.setScene(new Scene(detailWindow));

            // on hiding, there is a funcionality to add changes into database, if they were set
            stage.setOnHiding(event -> {
                Task updateInfo = new Task() {
                    @Override
                    protected Object call() {

                        if(employeeNaturalPersonDetailController.getDataChanged()) {
                            PersonManager personManager = new PersonManager();
                            personManager.updateNaturalPersonInfo(naturalPersonDetail);
                        }
                        return null;
                    }
                };

                updateInfo.setOnSucceeded(event1 -> {
                    if(employeeNaturalPersonDetailController.getDataChanged()) {
                        Notifications notification = Notifications.create()
                                .title("Informácie boli úspešne aktualizované!")
                                .hideAfter(Duration.seconds(4))
                                .hideCloseButton();
                        notification.showConfirm();
                    }
                });

                Thread thread = new Thread(updateInfo);
                thread.setDaemon(true);
                thread.start();
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getTextFieldSearchInDatabaseNatural() {
        return textFieldSearchInDatabaseNatural.getText();
    }

    public String getTextFieldSearchInDatabaseLegal() {
        return textFieldSearchInDatabaseLegal.getText();
    }

    public void setLabelOffsetNatural(String labelOffsetNatural) {
        this.labelOffsetNatural.setText(labelOffsetNatural);
    }

    public void setLabelOffsetLegal(String labelOffsetLegal) {
        this.labelOffsetLegal.setText(labelOffsetLegal);
    }

    public void setNewRangeOfDisplayedDataNatural() {
        int range = offSet + observableListNatural.size();
        setLabelOffsetNatural(((offSet +1)+ " - " + range));
    }

    public void setNewRangeOfDisplayedDataLegal() {
        int range = offSetLegal + observableListLegal.size();
        setLabelOffsetLegal(((offSetLegal +1)+ " - " + range));
    }

    public void addItemsToListNatural() {
        PersonManager naturalPerson = new PersonManager();

        observableListNatural = naturalPerson.getNaturalPerson(offSet);

    }

    public void addItemsToListLegal() {
        PersonManager legalPerson = new PersonManager();

        observableListLegal = legalPerson.getLegalPerson(offSetLegal);

    }

    public void addItemsToTableNatural() {
        tableViewNatural.setItems(observableListNatural);

        filer = new FilteredList(observableListNatural,e->true);

        if(observableListNatural.size() == 500) {
            buttonNextDataNatural.setDisable(false);
        }
    }

    public void addItemsToTableLegal() {
        tableViewLegal.setItems(observableListLegal);

        filerLegal = new FilteredList(observableListLegal,e->true);

        if(observableListLegal.size() == 500) {
            buttonNextDataLegal.setDisable(false);
        }
    }

    public void loadNextNatural(ActionEvent actionEvent) {
        offSet += 500;

        if(offSet > 0) {
            buttonPreviousDataNatural.setDisable(false);
        }

        Task loadNext = new Task() {
            @Override
            protected ObservableList<NaturalPerson> call() throws Exception {
                progressBarNatural.setVisible(true);

                PersonManager personManager = new PersonManager();

                if(isbuttonSearchInDatabasePushedNatural) {
                    observableListNatural = personManager.getNaturalPersonByFullName(getTextFieldSearchInDatabaseNatural(),offSet);
                } else {
                    observableListNatural = personManager.getNaturalPerson(offSet);
                }

                return observableListNatural;
            }
        };

        loadNext.setOnSucceeded(event -> {
            tableViewNatural.setItems(observableListNatural);

            progressBarNatural.setVisible(false);

            if(observableListNatural.size() < 500) {
                buttonNextDataNatural.setDisable(true);
            }

            filer = new FilteredList(observableListNatural,e->true);

            textFieldSearchInTablesNatural.setText("");

            setNewRangeOfDisplayedDataNatural();
        });

        Thread thread = new Thread(loadNext);
        thread.setDaemon(true);
        thread.start();
    }

    public void loadNextLegalPerson(ActionEvent actionEvent) {
        offSetLegal += 500;

        if(offSetLegal > 0) {
            buttonPreviousDataLegal.setDisable(false);
        }

        Task loadNext = new Task() {
            @Override
            protected ObservableList<LegalPerson> call() throws Exception {
                progressBarLegal.setVisible(true);

                PersonManager personManager = new PersonManager();

                if(isbuttonSearchInDatabasePushedLegal) {
                    observableListLegal = personManager.getLegalPersonByFullName(getTextFieldSearchInDatabaseLegal(),offSetLegal);
                } else {
                    observableListLegal = personManager.getLegalPerson(offSetLegal);
                }

                return observableListLegal;
            }
        };

        loadNext.setOnSucceeded(event -> {
            tableViewLegal.setItems(observableListLegal);

            progressBarLegal.setVisible(false);

            if(observableListLegal.size() < 500) {
                buttonNextDataLegal.setDisable(true);
            }

            filerLegal = new FilteredList(observableListLegal,e->true);

            textFieldSearchInTablesLegal.setText("");

            setNewRangeOfDisplayedDataLegal();
        });

        Thread thread = new Thread(loadNext);
        thread.setDaemon(true);
        thread.start();
    }

    public void loadPreviousNatural(ActionEvent actionEvent) {

        if(offSet > 0) {
            buttonNextDataNatural.setDisable(false);
        }

        offSet -= 500;

        if(offSet == 0) {
            buttonPreviousDataNatural.setDisable(true);
        }

        Task loadPrevious = new Task() {
            @Override
            protected ObservableList<NaturalPerson> call() throws Exception {

                progressBarNatural.setVisible(true);

                PersonManager personManager = new PersonManager();

                if(isbuttonSearchInDatabasePushedNatural) {
                    observableListNatural = personManager.getNaturalPersonByFullName(getTextFieldSearchInDatabaseNatural(),offSet);
                } else {
                    observableListNatural = personManager.getNaturalPerson(offSet);
                }

                return observableListNatural;
            }
        };

        loadPrevious.setOnSucceeded(event -> {
            tableViewNatural.setItems(observableListNatural);

            progressBarNatural.setVisible(false);

            filer = new FilteredList(observableListNatural,e->true);

            textFieldSearchInTablesNatural.setText("");

            setNewRangeOfDisplayedDataNatural();
        });

        Thread thread = new Thread(loadPrevious);
        thread.setDaemon(true);
        thread.start();
    }

    public void loadPreviousLegalPerson(ActionEvent actionEvent) {

        if(offSetLegal > 0) {
            buttonNextDataLegal.setDisable(false);
        }

        offSetLegal -= 500;

        if(offSetLegal == 0) {
            buttonPreviousDataLegal.setDisable(true);
        }

        Task loadPrevious = new Task() {
            @Override
            protected ObservableList<LegalPerson> call() throws Exception {

                progressBarLegal.setVisible(true);

                PersonManager personManager = new PersonManager();

                if(isbuttonSearchInDatabasePushedLegal) {
                    observableListLegal = personManager.getLegalPersonByFullName(getTextFieldSearchInDatabaseLegal(),offSetLegal);
                } else {
                    observableListLegal = personManager.getLegalPerson(offSetLegal);
                }

                return observableListLegal;
            }
        };

        loadPrevious.setOnSucceeded(event -> {
            tableViewLegal.setItems(observableListLegal);

            progressBarLegal.setVisible(false);

            filerLegal = new FilteredList(observableListLegal,e->true);

            textFieldSearchInTablesLegal.setText("");

            setNewRangeOfDisplayedDataLegal();
        });

        Thread thread = new Thread(loadPrevious);
        thread.setDaemon(true);
        thread.start();
    }

    public void searchInTableNatural(KeyEvent keyEvent) {

        if(observableListNatural == null) {
            return;
        }

        textFieldSearchInTablesNatural.textProperty().addListener((observable, oldValue, newValue) -> {
            filer.setPredicate((Predicate<? super NaturalPerson>) naturalPerson ->{

                if(newValue == null ||  newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFiler = newValue.toLowerCase();
                if(naturalPerson.getFirstName().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                } else if(naturalPerson.getLastName().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                } else if(naturalPerson.getID().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                } else if(naturalPerson.getPhoneNumber().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<NaturalPerson> sortedList = new SortedList<>(filer);
        sortedList.comparatorProperty().bind(tableViewNatural.comparatorProperty());
        tableViewNatural.setItems(sortedList);

        setNewRangeOfDisplayedDataNatural();
    }

    public void searchInTableLegal(KeyEvent keyEvent) {

        if(observableListLegal == null) {
            return;
        }

        textFieldSearchInTablesLegal.textProperty().addListener((observable, oldValue, newValue) -> {
            filerLegal.setPredicate((Predicate<? super LegalPerson>) legalPerson ->{

                if(newValue == null ||  newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFiler = newValue.toLowerCase();
                if(legalPerson.getID().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                } else if(legalPerson.getDic().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                } else if(legalPerson.getName().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                } else if(legalPerson.getPhoneNumber().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<LegalPerson> sortedList = new SortedList<>(filerLegal);
        sortedList.comparatorProperty().bind(tableViewLegal.comparatorProperty());
        tableViewLegal.setItems(sortedList);

        setNewRangeOfDisplayedDataLegal();
    }

    public void buttonSearchInDatabaseNaturalPushed(ActionEvent actionEvent) {

        buttonPreviousDataNatural.setDisable(true);
        isbuttonSearchInDatabasePushedNatural = true;
        offSet = 0;

        Task addDataFromDatabase = new Task() {
            @Override
            protected ObservableList<NaturalPerson> call() {

                progressBarNatural.setVisible(true);

                PersonManager personManager = new PersonManager();
                observableListNatural = personManager.getNaturalPersonByFullName(getTextFieldSearchInDatabaseNatural(),offSet);
                return observableListNatural;
            }
        };

        addDataFromDatabase.setOnSucceeded(event -> {
            tableViewNatural.setItems(observableListNatural);

            progressBarNatural.setVisible(false);

            if(observableListNatural.size() == 500) {
                Notifications notification = Notifications.create()
                        .title("Počet nájdených záznamov je väčší ako " + observableListNatural.size() + ".")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showInformation();

                buttonNextDataNatural.setDisable(false);
            } else {
                buttonNextDataNatural.setDisable(true);
            }

            filer = new FilteredList(observableListNatural,e->true);

            textFieldSearchInTablesNatural.setText("");

            setNewRangeOfDisplayedDataNatural();
        });

        final Thread thread = new Thread(addDataFromDatabase);
        thread.setDaemon(true);
        thread.start();
    }

    public void buttonSearchInDatabaseLegalPushed(ActionEvent actionEvent) {

        buttonPreviousDataLegal.setDisable(true);
        isbuttonSearchInDatabasePushedLegal = true;
        offSetLegal = 0;

        Task addDataFromDatabase = new Task() {
            @Override
            protected ObservableList<LegalPerson> call() {

                progressBarLegal.setVisible(true);

                PersonManager personManager = new PersonManager();
                observableListLegal = personManager.getLegalPersonByFullName(getTextFieldSearchInDatabaseLegal(),offSetLegal);
                return observableListLegal;
            }
        };

        addDataFromDatabase.setOnSucceeded(event -> {
            tableViewLegal.setItems(observableListLegal);

            progressBarLegal.setVisible(false);

            if(observableListLegal.size() == 500) {
                Notifications notification = Notifications.create()
                        .title("Počet nájdených záznamov je väčší ako " + observableListLegal.size() + ".")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showInformation();

                buttonNextDataLegal.setDisable(false);
            } else {
                buttonNextDataLegal.setDisable(true);
            }

            filerLegal = new FilteredList(observableListLegal,e->true);

            textFieldSearchInTablesLegal.setText("");

            setNewRangeOfDisplayedDataLegal();
        });

        final Thread thread = new Thread(addDataFromDatabase);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void btnBackPushed(ActionEvent actionEvent) {
        if(openedFromContractScene) {
            backToCreateContract(false,false);
            return;
        }

        backToMenu();
    }

    public void setHeader () {
        labelFirstName.setText(employee.getFirstName());
        labelLastName.setText(employee.getLastName());

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        setHeader();
    }

    public void backToMenu() {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_search_menu.fxml"));
            parent = (Parent) loaader.load();

            EmployeeSearchMenuController employeeSearchMenuController = loaader.getController();
            employeeSearchMenuController.setEmployee(employee);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void backToCreateContract(Boolean isNaturalPerson, Boolean isPersonSelected) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_create_contract.fxml"));
            parent = (Parent) loaader.load();

            EmployeeCreateContractController employeeCreateContractController= loaader.getController();
            employeeCreateContractController.setEmployee(employee);

            if(isPersonSelected) {
                if(isNaturalPerson) {
                    employeeCreateContractController.setCustomer(tableViewNatural.getSelectionModel().getSelectedItem());
                } else {
                    employeeCreateContractController.setCustomer(tableViewLegal.getSelectionModel().getSelectedItem());
                }
            }

            if(car != null) {
                employeeCreateContractController.setCar(car);
                employeeCreateContractController.setCarVin();
            } else if(carVIN != null) {
                employeeCreateContractController.setSelectedCarVIN(carVIN);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    @FXML
    public void buttonDisplayDataNaturalPushed(ActionEvent actionEvent) {
        isbuttonSearchInDatabasePushedNatural = false;

        offSet = 0;

        buttonPreviousDataNatural.setDisable(true);

        Task displayData = new Task() {
            @Override
            protected Object call() {
                progressBarNatural.setVisible(true);

                addItemsToListNatural();
                return null;
            }
        };

        displayData.setOnSucceeded(event -> {
            addItemsToTableNatural();

            filer = new FilteredList(observableListNatural,e->true);

            progressBarNatural.setVisible(false);

            textFieldSearchInTablesNatural.setText("");

            if(observableListNatural.size() == 500) {
                buttonNextDataNatural.setDisable(false);
            }

            setNewRangeOfDisplayedDataNatural();
        });

        Thread thread = new Thread(displayData);
        thread.setDaemon(true);
        thread.start();
    }

    public void buttonDisplayDataLegalPushed(ActionEvent actionEvent) {
        isbuttonSearchInDatabasePushedLegal = false;

        offSet = 0;

        buttonPreviousDataLegal.setDisable(true);

        Task displayData = new Task() {
            @Override
            protected Object call() {
                progressBarLegal.setVisible(true);

                addItemsToListLegal();
                return null;
            }
        };

        displayData.setOnSucceeded(event -> {
            addItemsToTableLegal();

            filer = new FilteredList(observableListLegal,e->true);

            progressBarLegal.setVisible(false);

            textFieldSearchInTablesLegal.setText("");

            if(observableListLegal.size() == 500) {
                buttonNextDataLegal.setDisable(false);
            }

            setNewRangeOfDisplayedDataLegal();
        });

        Thread thread = new Thread(displayData);
        thread.setDaemon(true);
        thread.start();
    }

    public void detailLegalPersonSelected(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/employee_legal_person_detail.fxml"));

        EmployeeLegalPersonDetailController employeeLegalPersonDetailController;

        try {
            Parent detailWindow = (Parent) loader.load();

            employeeLegalPersonDetailController = loader.getController();
            LegalPerson legalPersonDetail = tableViewLegal.getSelectionModel().getSelectedItem();
            employeeLegalPersonDetailController.setLegalPerson(legalPersonDetail);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Detail");
            stage.setScene(new Scene(detailWindow));

            // on hiding, there is a funcionality to add changes into database, if they were set
            stage.setOnHiding(event -> {
                Task updateInfo = new Task() {
                    @Override
                    protected Object call() {

                        if(employeeLegalPersonDetailController.getDataChanged()) {
                            PersonManager personManager = new PersonManager();
                            personManager.updateLegalPersonInfo(legalPersonDetail);
                        }
                        return null;
                    }
                };

                updateInfo.setOnSucceeded(event1 -> {
                    if(employeeLegalPersonDetailController.getDataChanged()) {
                        Notifications notification = Notifications.create()
                                .title("Informácie boli úspešne aktualizované!")
                                .hideAfter(Duration.seconds(4))
                                .hideCloseButton();
                        notification.showConfirm();
                    }
                });

                Thread thread = new Thread(updateInfo);
                thread.setDaemon(true);
                thread.start();
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createContractNaturalPersonSelected(ActionEvent actionEvent) {
       //displayCreateContractScene(true);
        backToCreateContract(true,true);
    }

    public void createContractLegalPersonSelected(ActionEvent actionEvent) {
        //displayCreateContractScene(false);
        backToCreateContract(false,true);
    }

    public void displayCreateContractScene(Boolean naturalPersonSelected) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_create_contract.fxml"));
            parent = (Parent) loaader.load();

            EmployeeCreateContractController employeeCreateContractController= loaader.getController();
            employeeCreateContractController.setEmployee(employee);

            if(naturalPersonSelected) {
                employeeCreateContractController.setCustomer(tableViewNatural.getSelectionModel().getSelectedItem());
            } else {
                employeeCreateContractController.setCustomer(tableViewLegal.getSelectionModel().getSelectedItem());
            }

            if(car != null) {
                employeeCreateContractController.setCar(car);
                employeeCreateContractController.setCarVin();
            } else if(carVIN != null) {
                employeeCreateContractController.setSelectedCarVIN(carVIN);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }
}
