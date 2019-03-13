package persistancemanagers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class CreateContractManager {

    public int createNewContract(String car_vin, String customer_id, Date date_from, Date date_to) throws SQLException {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        Properties prop = new Properties();
        InputStream input = null;
        int id;

        try {
            input = new FileInputStream("src/properties");

            prop.load(input);

            id = Integer.parseInt(prop.getProperty("loggedID"));

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT EXISTS (SELECT car_vin FROM car WHERE car_vin = '" + car_vin + "') AS exist;");
            ResultSet rs = st.executeQuery();
            rs.next();

            st = conn.prepareStatement("SELECT EXISTS (SELECT customer_id FROM customer WHERE customer_id = '" + customer_id + "') AS exist;");
            ResultSet rs2 = st.executeQuery();
            rs2.next();

            if(!rs.getBoolean("exist") && !rs2.getBoolean("exist")) {
                return 2;
            }

            if(!rs.getBoolean("exist")) {
                return 0;
            }

            if(!rs2.getBoolean("exist")) {
                return 1;
            }


            st = conn.prepareStatement("INSERT INTO contract (car_vin,customer_id,employee_id,date_from,date_to,price) VALUES (?,?,?,?,?,?)");
            st.setString(1, car_vin);
            st.setString(2, customer_id);
            st.setInt(3,id);
            st.setDate(4,date_from);
            st.setDate(5,date_to);
            st.setInt(6,1);
            st.executeUpdate();

            return 3;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            st.close();
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }
}
