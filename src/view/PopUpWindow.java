package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import persistancemanagers.CarManager;
import persistancemanagers.EnumManager;

import java.sql.SQLException;

public class PopUpWindow {

    private TextField field;
    private Stage window;

    public void display(String brand, String operation, String text) {

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setWidth(400);
        window.setHeight(200);
        window.setResizable(false);


        Label label = new Label(text);
        field = new TextField();
        Button btnAdd = new Button("Pridaj");
        btnAdd.setOnAction(e -> {
            try {
                do_operation(brand, operation);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,field,btnAdd);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public void do_operation(String brand, String operation) throws SQLException {

        if(getText().trim().isEmpty()) {
            Alert alertEmptyField = new Alert(Alert.AlertType.WARNING,"Zadaj údaje!", ButtonType.CLOSE);
            alertEmptyField.initStyle(StageStyle.TRANSPARENT);
            alertEmptyField.setHeaderText("Varovanie!");
            alertEmptyField.showAndWait();
            return;
        }

        EnumManager em = new EnumManager();

        if(!em.addToEnum(getText(),operation)){
            Alert alertAlreadyExist = new Alert(Alert.AlertType.WARNING,"Zadadný údaj je už uložený!", ButtonType.CLOSE);
            alertAlreadyExist.initStyle(StageStyle.TRANSPARENT);
            alertAlreadyExist.setHeaderText("Varovanie!");
            alertAlreadyExist.showAndWait();
        } else {
            if (brand != null){
                CarManager cm = new CarManager();
                cm.addNewCarInfo(brand,getText());
            }

            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION,"Pridané!", ButtonType.CLOSE);
            alertInfo.initStyle(StageStyle.TRANSPARENT);
            alertInfo.setHeaderText("Info!");
            alertInfo.showAndWait();
            window.close();
        }
    }

    public String getText() {
        return field.getText();
    }
}
