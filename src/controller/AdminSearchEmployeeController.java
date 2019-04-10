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
import model.Employee;
import org.controlsfx.control.Notifications;
import persistancemanagers.EmployeeManager;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AdminSearchEmployeeController implements Initializable {

    @FXML private AnchorPane rootPane;

    // header labels
    @FXML private Label labelFirstName;
    @FXML private Label labelLastName;
    @FXML private Label labelDate;

    // table view and collumns
    @FXML private TableView<Employee> tableView;
    @FXML private TableColumn<Employee,String> collumnFirstName;
    @FXML private TableColumn<Employee,String> collumnLastName;
    @FXML private TableColumn<Employee,String> collumnLogin;
    @FXML private TableColumn<Employee,String> collumnPhone;

    // textfields for search
    @FXML private TextField textFieldSearchInTables;
    @FXML private TextField textFieldSearchInDatabase;

    private ObservableList<Employee> observableList;

    private FilteredList filer;

    @FXML private Button buttonNextData;
    @FXML private Button buttonPreviousData;
    @FXML private Label labelOffset;

    @FXML private JFXProgressBar progressBar;

    private Employee admin;

    private Integer offSet = 0;

    private Boolean isbuttonSearchInDatabasePushed = false;

    public void setHeader () {
        labelFirstName.setText(admin.getFirstName());
        labelLastName.setText(admin.getLastName());

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        collumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        collumnLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        collumnPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // enable funcionality of ordering the collumns by click on it
        collumnFirstName.setSortType(TableColumn.SortType.ASCENDING);
        collumnLastName.setSortType(TableColumn.SortType.ASCENDING);
        collumnLogin.setSortType(TableColumn.SortType.ASCENDING);
        collumnPhone.setSortType(TableColumn.SortType.ASCENDING);

        tableView.getSortOrder().add(collumnFirstName);
        tableView.getSortOrder().add(collumnLastName);
        tableView.getSortOrder().add(collumnLogin);
        tableView.getSortOrder().add(collumnPhone);

        buttonNextData.setDisable(true);
        buttonPreviousData.setDisable(true);

        progressBar.setVisible(false);
    }

    public String getTextFieldSearchInDatabase() {
        return textFieldSearchInDatabase.getText();
    }

    public void setLabelOffset(String labelOffset) {
        this.labelOffset.setText(labelOffset);
    }

    public void setAdmin(Employee admin) {
        this.admin = admin;
        setHeader();
    }

    public void setNewRangeOfDisplayedData() {
        int range = offSet + observableList.size();
        setLabelOffset(((offSet + 1) + " - " + range));
    }

    public void addItemsToList() {
        EmployeeManager employeeManager = new EmployeeManager();

        observableList = employeeManager.getEmployee(offSet);
    }

    public void addItemsToTable() {
        tableView.setItems(observableList);

        filer = new FilteredList(observableList,e->true);

        if(observableList.size() == 500) {
            buttonNextData.setDisable(false);
        }
    }

    public void btnBackPushed(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/admin_menu.fxml"));
            parent = (Parent) loaader.load();

            AdminMenuController adminMenuController = loaader.getController();
            adminMenuController.setAdmin(admin);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void loadNext(ActionEvent actionEvent) {
        offSet += 500;

        if(offSet > 0) {
            buttonPreviousData.setDisable(false);
        }

        Task loadNext = new Task() {
            @Override
            protected ObservableList<Employee> call() throws Exception {
                progressBar.setVisible(true);

                EmployeeManager employeeManager = new EmployeeManager();

                if(isbuttonSearchInDatabasePushed) {
                    observableList = employeeManager.getEmployeeByFullName(getTextFieldSearchInDatabase(),offSet);
                } else {
                    observableList = employeeManager.getEmployee(offSet);
                }

                return observableList;
            }
        };

        loadNext.setOnSucceeded(event -> {
            tableView.setItems(observableList);

            progressBar.setVisible(false);

            if(observableList.size() < 500) {
                buttonNextData.setDisable(true);
            }

            filer = new FilteredList(observableList,e->true);

            textFieldSearchInTables.setText("");

            setNewRangeOfDisplayedData();
        });

        Thread thread = new Thread(loadNext);
        thread.setDaemon(true);
        thread.start();
    }

    public void loadPrevious(ActionEvent actionEvent) {

        if(offSet > 0) {
            buttonNextData.setDisable(false);
        }

        offSet -= 500;

        if(offSet == 0) {
            buttonPreviousData.setDisable(true);
        }

        Task loadPrevious = new Task() {
            @Override
            protected ObservableList<Employee> call() throws Exception {

                progressBar.setVisible(true);

                EmployeeManager employeeManager = new EmployeeManager();

                if(isbuttonSearchInDatabasePushed) {
                    observableList = employeeManager.getEmployeeByFullName(getTextFieldSearchInDatabase(),offSet);
                } else {
                    observableList = employeeManager.getEmployee(offSet);
                }

                return observableList;
            }
        };

        loadPrevious.setOnSucceeded(event -> {
            tableView.setItems(observableList);

            progressBar.setVisible(false);

            filer = new FilteredList(observableList,e->true);

            textFieldSearchInTables.setText("");

            setNewRangeOfDisplayedData();
        });

        Thread thread = new Thread(loadPrevious);
        thread.setDaemon(true);
        thread.start();
    }

    public void searchInTable(KeyEvent keyEvent) {

        if(observableList == null) {
            return;
        }

        textFieldSearchInTables.textProperty().addListener((observable, oldValue, newValue) -> {
            filer.setPredicate((Predicate<? super Employee>) employee ->{

                if(newValue == null ||  newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFiler = newValue.toLowerCase();
                if(employee.getFirstName().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                } else if(employee.getLastName().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                } else if(employee.getLogin().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                } else if(employee.getPhoneNumber().toLowerCase().contains(lowerCaseFiler)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Employee> sortedList = new SortedList<>(filer);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        setNewRangeOfDisplayedData();
    }

    public void buttonSearchInDatabasePushed(ActionEvent actionEvent) {

        buttonPreviousData.setDisable(true);
        isbuttonSearchInDatabasePushed = true;
        offSet = 0;

        Task addDataFromDatabase = new Task() {
            @Override
            protected ObservableList<Employee> call() {

                progressBar.setVisible(true);

                EmployeeManager employeeManager = new EmployeeManager();
                observableList = employeeManager.getEmployeeByFullName(getTextFieldSearchInDatabase(),offSet);
                return observableList;
            }
        };

        addDataFromDatabase.setOnSucceeded(event -> {
            tableView.setItems(observableList);

            progressBar.setVisible(false);

            if(observableList.size() == 500) {
                Notifications notification = Notifications.create()
                        .title("Počet nájdených záznamov je väčší ako " + observableList.size() + ".")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showInformation();

                buttonNextData.setDisable(false);
            } else {
                buttonNextData.setDisable(true);
            }

            filer = new FilteredList(observableList,e->true);

            textFieldSearchInTables.setText("");

            setNewRangeOfDisplayedData();
        });

        Thread thread = new Thread(addDataFromDatabase);
        thread.setDaemon(true);
        thread.start();
    }

    public void detailMenuSelected(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/admin_employee_detail.fxml"));

        AdminEmployeeDetailController adminEmployeeDetailController;

        try {
            Parent detailWindow = (Parent) loader.load();

            adminEmployeeDetailController = loader.getController();
            Employee employeeForDetail = tableView.getSelectionModel().getSelectedItem();
            adminEmployeeDetailController.setEmployee(employeeForDetail);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Detail");
            stage.setScene(new Scene(detailWindow));

            // on hiding, there is a funcionality to add changes into database, if they were set
            stage.setOnHiding(event -> {
                Task updateInfo = new Task() {
                    @Override
                    protected Object call() {

                        if(adminEmployeeDetailController.getDataChanged()) {
                            EmployeeManager employeeManager = new EmployeeManager();
                            employeeManager.updateEmployee(employeeForDetail);
                        }
                        return null;
                    }
                };

                updateInfo.setOnSucceeded(event1 -> {
                    if(adminEmployeeDetailController.getDataChanged()) {
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

    public void buttonDisplayDataPushed(ActionEvent actionEvent) {
        isbuttonSearchInDatabasePushed = false;

        offSet = 0;

        buttonPreviousData.setDisable(true);

        Task displayData = new Task() {
            @Override
            protected Object call() {
                progressBar.setVisible(true);

                addItemsToList();
                return null;
            }
        };

        displayData.setOnSucceeded(event -> {
            addItemsToTable();

            filer = new FilteredList(observableList,e->true);

            progressBar.setVisible(false);

            textFieldSearchInTables.setText("");

            if(observableList.size() == 500) {
                buttonNextData.setDisable(false);
            }

            setNewRangeOfDisplayedData();
        });

        Thread thread = new Thread(displayData);
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteMenuSelected(ActionEvent actionEvent) {
        Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();

        EmployeeManager employeeManager = new EmployeeManager();

        if(employeeManager.deleteEmployee(selectedEmployee)) {

            Notifications notification = Notifications.create()
                    .title("Záznam bol úspešne odstránený!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showConfirm();

            tableView.getItems().remove(selectedEmployee);
        } else {
            Notifications notification = Notifications.create()
                    .title("Záznam sa nepodarilo odstrániť!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
        }
    }
}
