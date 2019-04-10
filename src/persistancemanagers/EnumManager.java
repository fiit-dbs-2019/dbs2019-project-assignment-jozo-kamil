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

    public ObservableList<String> getServiceNamesAndLocations() {
        ObservableList<String> allServices = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "SELECT servis_name, servis_location " +
                    "FROM servis;"
            );
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String nameAndLocation = rs.getString("servis_name") +
                        ", " + rs.getString("servis_location");
                allServices.add(nameAndLocation);
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

    public ObservableList<String> getTypeOfHarm(){
        ObservableList<String> typesOfHarm = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try{
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "SELECT unnest(enum_range(NULL::type_of_harm))::text " +
                    "AS type " +
                    "ORDER BY type");
            ResultSet rs = st.executeQuery();

            while(rs.next()){
               String type = rs.getString("type");
               typesOfHarm.add(type);
            }

            return typesOfHarm;

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

    public boolean addToEnum(String newValue, String operator) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            String whichEnumFirstPart = "car_";
            String whichEnum = whichEnumFirstPart + operator;


            st = conn.prepareStatement(
                    "SELECT '" + newValue.toLowerCase() + "' ILIKE any(enum_range(NULL::" +
                            whichEnum + ")::name[]) " +
                    "AS already_exist;");

            ResultSet rs = st.executeQuery();

            rs.next();

            if(!rs.getBoolean("already_exist")) {
                st = conn.prepareStatement(
                        "ALTER TYPE " + whichEnum + " " +
                        "ADD VALUE '" + newValue + "';"
                );

                st.executeUpdate();

                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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

    public void employeeTypeEnum(JFXComboBox comboBox, String whichEnum) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "SELECT unnest(enum_range(NULL::" + whichEnum + "))::text " +
                    "AS type " +
                    "ORDER BY type");
            ResultSet rs = st.executeQuery();

            comboBox.getItems().clear();

            while(rs.next()){
                comboBox.getItems().add(rs.getString("type"));
            }

            comboBox.setVisibleRowCount(comboBox.getItems().size() -1);

        } catch (SQLException e) {
            e.printStackTrace();
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

    public void setModelsForSpecificBrand(String brand, JFXComboBox comboBox) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "SELECT DISTINCT model " +
                    "FROM car_info " +
                    "WHERE brand = '" + brand + "';"
            );
            ResultSet rs = st.executeQuery();

            comboBox.getItems().clear();

            while(rs.next()){
                comboBox.getItems().add(rs.getString("model"));
            }

            comboBox.setVisibleRowCount(comboBox.getItems().size() -1);

        } catch (SQLException e) {
            e.printStackTrace();
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
}
