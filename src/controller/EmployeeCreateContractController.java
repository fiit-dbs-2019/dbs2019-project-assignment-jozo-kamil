package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import persistancemanagers.AllTablesManager;
import persistancemanagers.CreateContractManager;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeCreateContractController implements Initializable {
    @FXML private DatePicker datePickerFrom;
    @FXML private DatePicker datePickerTo;
    @FXML private AnchorPane rootPane;
    @FXML private TextField textFieldOp;
    @FXML private TextField textFieldVin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnCreateContractPushed(ActionEvent actionEvent) throws SQLException {
        CreateContractManager ccm = new CreateContractManager();

        int result = ccm.createNewContract(getTextFieldVin(),getTextFieldOp(),getDateFrom(),getDateTo());

        if (result == 0) {
            Alert alertError = new Alert(Alert.AlertType.ERROR,"Zadané VIN číslo: "+getTextFieldVin()+ " sa v databáze nevyskytuje.",ButtonType.CLOSE);
            alertError.initStyle(StageStyle.TRANSPARENT);
            alertError.setHeaderText("Chyba!");
            alertError.showAndWait();
        } else if (result ==1) {
            Alert alertError = new Alert(Alert.AlertType.ERROR,"Zadané OP číslo žiadateľa: "+getTextFieldOp()+ " sa v databáze nevyskytuje.",ButtonType.CLOSE);
            alertError.initStyle(StageStyle.TRANSPARENT);
            alertError.setHeaderText("Chyba!");
            alertError.showAndWait();
        } else if (result ==2){
            Alert alertError = new Alert(Alert.AlertType.ERROR,"Zadané VIN číslo: "+getTextFieldVin()+" a OP číslo žiadateľa: "+getTextFieldOp()+ " sa v databáze nevyskytuje.",ButtonType.CLOSE);
            alertError.initStyle(StageStyle.TRANSPARENT);
            alertError.setHeaderText("Chyba!");
            alertError.showAndWait();
        } else if (result ==3){
            if (getDateTo() == null || getDateFrom() == null || getDateTo().before(getDateFrom())) {
                Alert alertError = new Alert(Alert.AlertType.ERROR,"Zadajte správne dobu trvania."+getTextFieldOp()+ " sa v databáze nevyskytuje.",ButtonType.CLOSE);
                alertError.initStyle(StageStyle.TRANSPARENT);
                alertError.setHeaderText("Chyba!");
                alertError.showAndWait();
            }
            else {
                Alert alertOKInformation = new Alert(Alert.AlertType.INFORMATION,"Vytvorenie zmluvy prebehlo úspešne.", ButtonType.CLOSE);
                alertOKInformation.initStyle(StageStyle.TRANSPARENT);
                alertOKInformation.setHeaderText("Info!");
                alertOKInformation.showAndWait();
            }
        }
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public String getTextFieldOp() {
        return textFieldOp.getText();
    }

    public String getTextFieldVin() {
        return textFieldVin.getText();
    }

    public Date getDateFrom() {
        if (datePickerFrom.getValue()==null){
            return null;
        }
        Date date = Date.valueOf(datePickerFrom.getValue());

        return date;
    }

    public Date getDateTo() {
        if (datePickerTo.getValue()==null){
            return null;
        }
        Date date = Date.valueOf(datePickerTo.getValue());

        return date;
    }
}
