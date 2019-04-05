package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import persistancemanagers.CreateContractManager;
import persistancemanagers.EnumManager;
import persistancemanagers.PersonManager;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class EmployeeContractDetailController implements Initializable {
    @FXML private MenuItem menuItem;

    @FXML private ContextMenu contextMenu;

    @FXML private TableView<Harm> tableViewHarm;

    @FXML private TableColumn<Harm, String> collumnType;

    @FXML private TableColumn<Harm, String> collumnDescription;

    @FXML private JFXTextField textFieldTypeOfHarm;

    @FXML private JFXTextField textFieldDescriptionOfHarm;

    @FXML private JFXButton buttonAddHarm;

    @FXML private Label title;

    @FXML private AnchorPane rootPane;

    @FXML private Label labelCustomer1;

    @FXML private Label labelCustomer2;

    @FXML private Label labelCustomer3;

    @FXML private Label fieldCustomer1;

    @FXML private Label fieldCustomer2;

    @FXML private Label fieldCustomer3;

    @FXML private Label labelAdress;

    @FXML private Label labelBankAccount;

    @FXML private Label labelCustomerPhone;

    @FXML private Label labelVIN;

    @FXML private Label labelSPZ;

    @FXML private Label labelBrand;

    @FXML private Label labelModel;

    @FXML private Label labelYear;

    @FXML private Label labelMileage;

    @FXML private Label labelFuel;

    @FXML private Label labelEngineCapacity;

    @FXML private Label labelEnginePower;

    @FXML private Label labelGearBox;

    @FXML private Label labelBodyStyle;

    @FXML private Label labelColor;

    @FXML private Label labelPrice;

    @FXML private Label labelEmployeeFirstName;

    @FXML private Label labelEmployeeLastName;

    @FXML private Label labelDateFromTo;

    @FXML private Label labelDateOfContract;

    @FXML private Label labelTotalPrice;

    private Contract contract;
    private Boolean dataChanged = false;

    private Car car;
    private Employee employee;
    private Person customer;

    private ObservableList<String> typeOfHarm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collumnType.setCellValueFactory(new PropertyValueFactory<>("typeOfHarm"));
        collumnDescription.setCellValueFactory(new PropertyValueFactory<>("harmDescription"));
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Boolean getDataChanged() {
        return dataChanged;
    }

    public void setRootPane(AnchorPane rootPane) {
        this.rootPane = rootPane;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        setLabels();

        if (contract.getHarm_id() != 0){

            textFieldTypeOfHarm.setVisible(false);
            textFieldDescriptionOfHarm.setVisible(false);
            buttonAddHarm.setVisible(false);

            CreateContractManager contractManager = new CreateContractManager();
            contractManager.setHarmToContract(contract);

            if (!contractManager.checkIfHarmHasRepair(contract.getHarm_id())){
                menuItem.setVisible(false);
            }

            tableViewHarm.setItems(FXCollections.observableArrayList(contract.getHarm()));

        } else {

            EnumManager enumManager = new EnumManager();
            typeOfHarm = enumManager.getTypeOfHarm();

            menuItem.setVisible(false);

            TextFields.bindAutoCompletion(textFieldTypeOfHarm,typeOfHarm);

        }
    }

    public String getTypeOfHarm() {
        return textFieldTypeOfHarm.getText();
    }

    public String getDescriptionOfHarm() {
        return textFieldDescriptionOfHarm.getText();
    }

    public boolean checkFields(){
        if (getTypeOfHarm().trim().isEmpty() || getDescriptionOfHarm().trim().isEmpty() || !typeOfHarm.contains(getTypeOfHarm())){
            return true;
        }

        return false;
    }

    public void setPerson(Person person) {
        this.customer = person;
    }


    public void setLabels(){
        setTitle("Zmluva o výpožičke motorového vozidla č."+ contract.getContract_id());

        setLabelEmployeeFirstName(employee.getFirstName());
        setLabelEmployeeLastName(employee.getLastName());

        setLabelDateOfContract(contract.getDate_of_creating().toString());

        setLabelDateFromTo("OD: " + contract.getDate_from() + " DO: " + contract.getDate_to());

        setLabelVIN(car.getCar_vin());
        setLabelSPZ(car.getSpz());
        setLabelBrand(car.getBrand());
        setLabelModel(car.getModel());
        setLabelYear(car.getYear_of_production().toString());
        setLabelMileage(String.valueOf(car.getMileage()));
        setLabelFuel(car.getFuel());
        setLabelEngineCapacity(String.valueOf(car.getEngine_capacity()));
        setLabelEnginePower(String.valueOf(car.getEngine_power()));
        setLabelGearBox(car.getGear_box());
        setLabelBodyStyle(car.getBody_style());
        setLabelColor(car.getColor());
        setLabelPrice(car.getPrice_per_day() + "€");

        // add total price
        long diff = contract.getDate_to().getTime() - contract.getDate_from().getTime();
        setLabelTotalPrice(car.getPrice_per_day()*(int)(TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS)));

        if (customer instanceof NaturalPerson) {
            setLabelCustomer1("Meno:");
            setLabelCustomer2("Priezvisko:");
            setLabelCustomer3("Číslo OP:");

            setFieldCustomer1(((NaturalPerson) customer).getFirstName());
            setFieldCustomer2(((NaturalPerson) customer).getLastName());
            setFieldCustomer3(((NaturalPerson) customer).getID());

            setLabelAdress(((NaturalPerson) customer).getAdress());
            setLabelBankAccount(((NaturalPerson) customer).getBankAccount());
            setLabelCustomerPhone(((NaturalPerson) customer).getPhoneNumber());
        }
        if (customer instanceof LegalPerson){
            setLabelCustomer1("IČO:");
            setLabelCustomer2("DIČ:");
            setLabelCustomer3("Názov firmy:");

            setFieldCustomer1(((LegalPerson) customer).getID());
            setFieldCustomer2(((LegalPerson) customer).getDic());
            setFieldCustomer3(((LegalPerson) customer).getName());

            setLabelAdress(((LegalPerson) customer).getAdress());
            setLabelBankAccount(((LegalPerson) customer).getBankAccount());
            setLabelCustomerPhone(((LegalPerson) customer).getPhoneNumber());
        }
    }

    public void setLabelCustomer1(String labelCustomer1) {
        this.labelCustomer1.setText(labelCustomer1);
    }

    public void setLabelCustomer2(String labelCustomer2) {
        this.labelCustomer2.setText(labelCustomer2);
    }

    public void setLabelCustomer3(String labelCustomer3) {
        this.labelCustomer3.setText(labelCustomer3);
    }

    public void setFieldCustomer1(String fieldCustomer1) {
        this.fieldCustomer1.setText(fieldCustomer1);
    }

    public void setFieldCustomer2(String fieldCustomer2) {
        this.fieldCustomer2.setText(fieldCustomer2);
    }

    public void setFieldCustomer3(String fieldCustomer3) {
        this.fieldCustomer3.setText(fieldCustomer3);
    }

    public void setLabelAdress(String labelAdress) {
        this.labelAdress.setText(labelAdress);
    }

    public void setLabelBankAccount(String labelBankAccount) {
        this.labelBankAccount.setText(labelBankAccount);
    }

    public void setLabelCustomerPhone(String labelCustomerPhone) {
        this.labelCustomerPhone.setText(labelCustomerPhone);
    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    public void setLabelVIN(String labelVIN) {
        this.labelVIN.setText(labelVIN);
    }

    public void setLabelSPZ(String labelSPZ) {
        this.labelSPZ.setText(labelSPZ);
    }

    public void setLabelBrand(String labelBrand) {
        this.labelBrand.setText(labelBrand);
    }

    public void setLabelModel(String labelModel) {
        this.labelModel.setText(labelModel);
    }

    public void setLabelYear(String labelYear) {
        this.labelYear.setText(labelYear);
    }

    public void setLabelMileage(String labelMileage) {
        this.labelMileage.setText(labelMileage);
    }

    public void setLabelFuel(String labelFuel) {
        this.labelFuel.setText(labelFuel);
    }

    public void setLabelEngineCapacity(String labelEngineCapacity) {
        this.labelEngineCapacity.setText(labelEngineCapacity);
    }

    public void setLabelEnginePower(String labelEnginePower) {
        this.labelEnginePower.setText(labelEnginePower);
    }

    public void setLabelGearBox(String labelGearBox) {
        this.labelGearBox.setText(labelGearBox);
    }

    public void setLabelBodyStyle(String labelBodyStyle) {
        this.labelBodyStyle.setText(labelBodyStyle);
    }

    public void setLabelColor(String labelColor) {
        this.labelColor.setText(labelColor);
    }

    public void setLabelPrice(String labelPrice) {
        this.labelPrice.setText(labelPrice);
    }

    public void setLabelEmployeeFirstName(String labelEmployeeFirstName) {
        this.labelEmployeeFirstName.setText(labelEmployeeFirstName);
    }

    public void setLabelEmployeeLastName(String labelEmployeeLastName) {
        this.labelEmployeeLastName.setText(labelEmployeeLastName);
    }

    public void setLabelDateFromTo(String labelDateFromTo) {
        this.labelDateFromTo.setText(labelDateFromTo);
    }

    public void setLabelDateOfContract(String labelDateOfContract) {
        this.labelDateOfContract.setText(labelDateOfContract);
    }

    public void setLabelTotalPrice(Float labelTotalPrice) {
        this.labelTotalPrice.setText(String.valueOf(labelTotalPrice) + "€");
    }

    public void btnAddHarmPushed(ActionEvent actionEvent) {
        if (checkFields()){
            Notifications notification = Notifications.create()
                    .title("Zadaj správne údaje")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
        }
        else {
            CreateContractManager contractManager = new CreateContractManager();
            contractManager.addNewHarm(contract,getTypeOfHarm(),getDescriptionOfHarm());

            Notifications notification = Notifications.create()
                    .title("Poškodenie bolo úspešne pridané.")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showConfirm();

            textFieldDescriptionOfHarm.setVisible(false);
            textFieldTypeOfHarm.setVisible(false);
            buttonAddHarm.setVisible(false);


            if (contractManager.checkIfHarmHasRepair(contract.getHarm_id())){
                menuItem.setVisible(true);
            }

            tableViewHarm.setItems(FXCollections.observableArrayList(contract.getHarm()));
        }
    }

    public void itemAddRepairSelected(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/employee_add_harm_repair.fxml"));

        EmployeeAddHarmRepairController employeeAddHarmRepairController;

        try {
            Parent detailWindow = (Parent) loader.load();

            employeeAddHarmRepairController = loader.getController();

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Pridanie opravy");
            stage.setScene(new Scene(detailWindow));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
