package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.*;
import org.controlsfx.control.Notifications;
import persistancemanagers.CarManager;
import persistancemanagers.CreateContractManager;
import persistancemanagers.EmployeeManager;
import persistancemanagers.PersonManager;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class EmployeeConfirmContractController implements Initializable {

    @FXML private AnchorPane rootPane;

    // labels for customer
    @FXML private Label labelCustomer1;
    @FXML private Label labelCustomer2;
    @FXML private Label labelCustomer3;
    @FXML private Label fieldCustomer1;
    @FXML private Label fieldCustomer2;
    @FXML private Label fieldCustomer3;
    @FXML private Label labelAdress;
    @FXML private Label labelBankAccount;
    @FXML private Label labelCustomerPhone;

    // labels for car
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

    // labels for employee
    @FXML private Label labelEmployeeFirstName;
    @FXML private Label labelEmployeeLastName;

    // labels for contract info
    @FXML private Label labelDateFromTo;
    @FXML private Label labelDateOfContract;
    @FXML private Label labelTotalPrice;

    // for save the data from database, or previously got from create contract scene [optional]
    private Car car = null;
    private Person customer = null;
    private Date dateFROM = null;
    private Date dateTO = null;

    // logged employee
    private Employee employee;

    // only filled in case of employee didnt search for customer or cas
    private String carVIN;
    private String customerID;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setAllLabels() {
        setEmployeeLabels();
        setAnotherLabels();
    }

    ///////////////////////////////////////////////////////////////////
    // setters for car and person, they are optional

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setCustomer(Person person) {
        this.customer = person;
    }

    public void setCarAndCustomer() {

        // check if car is set, else there is a need to connect to databse to get car info from set VIN
        if(car == null) {
            CarManager cm = new CarManager();

            try {
                car = cm.getCarFromDatabase(carVIN);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // check if customer is set, else there is a need to connect to databse to get customer info from set ID
        if(customer == null) {
            PersonManager pm = new PersonManager();

            try {
                customer = pm.getPersonFromDatabase(customerID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////

    // setters for info from previous scene

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCarVIN() {
        return carVIN;
    }

    public void setCarVIN(String carVIN) {
        this.carVIN = carVIN;
    }

    public Date getDateFROM() {
        return dateFROM;
    }

    public void setDateFROM(Date dateFROM) {
        this.dateFROM = dateFROM;
    }

    public Date getDateTO() {
        return dateTO;
    }

    public void setDateTO(Date dateTO) {
        this.dateTO = dateTO;
    }

    // LABELS set - complex - connection to database and more

    public void setEmployeeLabels() {
        setLabelEmployeeFirstName(employee.getFirstName());
        setLabelEmployeeLastName(employee.getLastName());
    }

    public void setAnotherLabels() {
        setLabelDateOfContract(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

        setLabelDateFromTo("OD: " + dateFROM + " DO: " + dateTO);

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
        long diff = dateTO.getTime() - dateFROM.getTime();
        setLabelTotalPrice(car.getPrice_per_day()*(int)(TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS)));

        if (customer instanceof NaturalPerson) {
            setLabelCustomer1("Meno:");
            setLabelCustomer2("Priezvisko:");
            setLabelCustomer3("Číslo OP:");

            setFieldCustomer1(((NaturalPerson) customer).getFirstName());
            setFieldCustomer2(((NaturalPerson) customer).getLastName());
            setFieldCustomer3(((NaturalPerson) customer).getOp());

            setLabelAdress(((NaturalPerson) customer).getAdress());
            setLabelBankAccount(((NaturalPerson) customer).getBankAccount());
            setLabelCustomerPhone(((NaturalPerson) customer).getPhoneNumber());
        }
        if (customer instanceof LegalPerson){
            setLabelCustomer1("IČO:");
            setLabelCustomer2("DIČ:");
            setLabelCustomer3("Názov firmy:");

            setFieldCustomer1(((LegalPerson) customer).getIco());
            setFieldCustomer2(((LegalPerson) customer).getDic());
            setFieldCustomer3(((LegalPerson) customer).getName());

            setLabelAdress(((LegalPerson) customer).getAdress());
            setLabelBankAccount(((LegalPerson) customer).getBankAccount());
            setLabelCustomerPhone(((LegalPerson) customer).getPhoneNumber());
        }
    }

    // SO MUCH SETTERS FOR LABELS IN GUI

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

    // BUTTONS EVENTS

    public void btnBackPushed(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_create_contract.fxml"));
            parent = (Parent) loaader.load();

            EmployeeCreateContractController employeeCreateContractController = loaader.getController();
            employeeCreateContractController.setEmployee(employee);
            employeeCreateContractController.setCar(car);
            employeeCreateContractController.setCustomer(customer);

            employeeCreateContractController.setCarVin();
            employeeCreateContractController.setCustomerID();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void btnSubmitPushed(ActionEvent actionEvent) {
        CreateContractManager ccm = new CreateContractManager();

        java.sql.Date sqlDateFROM = new java.sql.Date(dateFROM.getTime());
        java.sql.Date sqlDateTO = new java.sql.Date(dateTO.getTime());

        if(customer instanceof NaturalPerson) {
            try {
                if(ccm.createNewContract(car.getCar_vin(),((NaturalPerson) customer).getOp(),sqlDateFROM,sqlDateTO,employee.getEmployeeID())) {

                    Notifications notification = Notifications.create()
                            .title("Zmluva bola úspešne vytvorená!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showConfirm();

                    backToMenu();

                } else {

                    Notifications notification = Notifications.create()
                            .title("Pri zakladaní zmluvy sa vyskytla neočakávaná chyba!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showError();
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(customer instanceof LegalPerson) {
            try {
                if(ccm.createNewContract(car.getCar_vin(),((LegalPerson) customer).getIco(),sqlDateFROM,sqlDateTO,employee.getEmployeeID())) {

                    Notifications notification = Notifications.create()
                            .title("Zmluva bola úspešne vytvorená!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showConfirm();

                    backToMenu();

                } else {

                    Notifications notification = Notifications.create()
                            .title("Pri zakladaní zmluvy sa vyskytla neočakávaná chyba!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showError();
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void backToMenu() {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_menu.fxml"));
            parent = (Parent) loaader.load();

            EmployeeMenuController employeeMenuController = loaader.getController();
            employeeMenuController.setEmployee(employee);

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
