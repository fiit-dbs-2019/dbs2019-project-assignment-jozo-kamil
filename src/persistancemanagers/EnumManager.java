package persistancemanagers;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumManager {

    public void employeeTypeEnum(ComboBox comboBox, String whichEnum) throws SQLException {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT unnest(enum_range(NULL::" + whichEnum + "))::text AS type ORDER BY type");
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                comboBox.getItems().add(rs.getString("type"));
            }

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
