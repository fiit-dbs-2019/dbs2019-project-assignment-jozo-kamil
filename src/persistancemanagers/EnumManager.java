package persistancemanagers;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import model.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumManager {

    public ObservableList<String> getServiceNamesAndRecords() {
        ObservableList<String> allServices = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("select servis_name, servis_location from servis;");
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                String nameAndLocation = rs.getString("servis_name") + ", " + rs.getString("servis_location");
                allServices.add(nameAndLocation);

//                Integer servisID = rs.getInt("servis_id");
//                String servisName = rs.getString("servis_name");
//                String servisLocation = rs.getString("servis_location");
//
//                allServices.add(new Service(servisID,servisName,servisLocation));
            }

            return allServices;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public boolean addToEnum(String newValue, String operator) throws SQLException {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            String which_enum_first_part = "car_";
            String which_enum = which_enum_first_part + operator;


            st = conn.prepareStatement("SELECT '" + newValue.toLowerCase() + "' ILIKE any(enum_range(NULL::" + which_enum + ")::name[]) AS already_exist;");
            ResultSet rs = st.executeQuery();

            rs.next();

            if(!rs.getBoolean("already_exist")) {
                st = conn.prepareStatement("ALTER TYPE " + which_enum + " ADD VALUE '" + newValue + "';");
                st.executeUpdate();

                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            st.close();
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public void employeeTypeEnum(JFXComboBox comboBox, String whichEnum) throws SQLException {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT unnest(enum_range(NULL::" + whichEnum + "))::text AS type ORDER BY type");
            ResultSet rs = st.executeQuery();

            comboBox.getItems().clear();

            while(rs.next()){
                comboBox.getItems().add(rs.getString("type"));
            }

            comboBox.setVisibleRowCount(comboBox.getItems().size() -1);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            st.close();
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public void setModelsForSpecificBrand(String brand, JFXComboBox comboBox) throws SQLException {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT DISTINCT model FROM car_info WHERE brand = '" + brand + "';");
            ResultSet rs = st.executeQuery();

            comboBox.getItems().clear();

            while(rs.next()){
                comboBox.getItems().add(rs.getString("model"));
            }

            comboBox.setVisibleRowCount(comboBox.getItems().size() -1);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            st.close();
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }
}
