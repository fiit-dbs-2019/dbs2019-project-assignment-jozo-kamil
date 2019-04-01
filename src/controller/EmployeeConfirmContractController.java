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

    // for save the data from database
    private Car car = null;
    private Person person = null;
    private Date dateFROM = null;
    private Date dateTO = null;

    private Employee employee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setEmployeeLabels();
        setAnotherLabels();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setEmployeeLabels() {
        EmployeeManager em = new EmployeeManager();
        Employee employee = null;

        try {
            employee = em.getEmployeeFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setLabelEmployeeFirstName(employee.getFirstName());
        setLabelEmployeeLastName(employee.getLastName());
    }

    public void setAnotherLabels() {
        setLabelDateOfContract(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

        Properties prop = new Properties();
        String car_vin;
        String customer_id;

        // input for contract_info
        FileInputStream input = null;

        try {
            input = new FileInputStream("src/contract_info");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            try {
                String dateFromProperty = prop.getProperty("dateFROM");
                dateFROM = new SimpleDateFormat("yyyy-MM-dd").parse(dateFromProperty);

                String dateToProperty = prop.getProperty("dateTO");
                dateTO = new SimpleDateFormat("yyyy-MM-dd").parse(dateToProperty);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            setLabelDateFromTo("OD: " + prop.getProperty("dateFROM") + " - DO: " + prop.getProperty("dateTO"));

            car_vin = prop.getProperty("carVIN");
            customer_id = prop.getProperty("customerID");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        CarManager cm = new CarManager();

        try {
            car = cm.getCarFromDatabase(car_vin);
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        PersonManager pm = new PersonManager();

        try {
            person = pm.getPersonFromDatabase(customer_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (person instanceof NaturalPerson) {
            setLabelCustomer1("Meno:");
            setLabelCustomer2("Priezvisko:");
            setLabelCustomer3("Číslo OP:");

            setFieldCustomer1(((NaturalPerson) person).getFirstName());
            setFieldCustomer2(((NaturalPerson) person).getLastName());
            setFieldCustomer3(((NaturalPerson) person).getOp());

            setLabelAdress(((NaturalPerson) person).getAdress());
            setLabelBankAccount(((NaturalPerson) person).getBankAccount());
            setLabelCustomerPhone(((NaturalPerson) person).getPhoneNumber());
        }
        if (person instanceof LegalPerson){
            setLabelCustomer1("IČO:");
            setLabelCustomer2("DIČ:");
            setLabelCustomer3("Názov firmy:");

            setFieldCustomer1(((LegalPerson) person).getIco());
            setFieldCustomer2(((LegalPerson) person).getDic());
            setFieldCustomer3(((LegalPerson) person).getName());

            setLabelAdress(((LegalPerson) person).getAdress());
            setLabelBankAccount(((LegalPerson) person).getBankAccount());
            setLabelCustomerPhone(((LegalPerson) person).getPhoneNumber());
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

    public void btnBackPushed(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_create_contract.fxml"));
            parent = (Parent) loaader.load();

            EmployeeCreateContractController employeeCreateContractController = loaader.getController();
            employeeCreateContractController.setEmployee(employee);

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

        if(person instanceof NaturalPerson) {
            try {
                if(ccm.createNewContract(car.getCar_vin(),((NaturalPerson) person).getOp(),sqlDateFROM,sqlDateTO)) {

//                    Alert alertInfo = new Alert(Alert.AlertType.INFORMATION,"Zmluva bola úspešne vytvorená!", ButtonType.CLOSE);
//                    alertInfo.initStyle(StageStyle.TRANSPARENT);
//                    alertInfo.setHeaderText("Info!");
//                    alertInfo.showAndWait();
                    Notifications notification = Notifications.create()
                            .title("Zmluva bola úspešne vytvorená!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showConfirm();

//                    AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
//                    rootPane.getChildren().setAll(pane);
                    backToMenu();

                } else {
//                    Alert alertError = new Alert(Alert.AlertType.ERROR,"Pri zakladaní zmluvy sa vyskytla neočakávaná chyba!", ButtonType.CLOSE);
//                    alertError.initStyle(StageStyle.TRANSPARENT);
//                    alertError.setHeaderText("Chyba!");
//                    alertError.showAndWait();
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

        if(person instanceof LegalPerson) {
            try {
                if(ccm.createNewContract(car.getCar_vin(),((LegalPerson) person).getIco(),sqlDateFROM,sqlDateTO)) {

//                    Alert alertInfo = new Alert(Alert.AlertType.INFORMATION,"Zmluva bola úspešne vytvorená!", ButtonType.CLOSE);
//                    alertInfo.initStyle(StageStyle.TRANSPARENT);
//                    alertInfo.setHeaderText("Info!");
//                    alertInfo.showAndWait();
                    Notifications notification = Notifications.create()
                            .title("Zmluva bola úspešne vytvorená!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showConfirm();

//                    AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
//                    rootPane.getChildren().setAll(pane);
                    backToMenu();

                } else {
//                    Alert alertError = new Alert(Alert.AlertType.ERROR,"Pri zakladaní zmluvy sa vyskytla neočakávaná chyba!", ButtonType.CLOSE);
//                    alertError.initStyle(StageStyle.TRANSPARENT);
//                    alertError.setHeaderText("Chyba!");
//                    alertError.showAndWait();
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
