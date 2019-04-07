package persistancemanagers;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumManager {

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

    public void employeeTypeEnum(ComboBox comboBox, String whichEnum) throws SQLException {
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

    public void setModelsForSpecificBrand(String brand, ComboBox comboBox) throws SQLException {
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
