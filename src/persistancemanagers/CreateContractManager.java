package persistancemanagers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class CreateContractManager {

    public boolean createNewContract(String car_vin, String customer_id, Date date_from, Date date_to, Integer id) throws SQLException {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();
            ResultSet rs;

            int price_per_day;
            int sum;
            long diff = date_to.getTime()-date_from.getTime();
            Date date = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

            st = conn.prepareStatement("SELECT price_per_day FROM car_info WHERE car_info_id = (SELECT car_info_id FROM car WHERE car_vin = '" + car_vin + "');");
            rs = st.executeQuery();
            rs.next();

            price_per_day=rs.getInt("price_per_day");

            //calculate days between 2 days
            //source: https://stackoverflow.com/questions/20165564/calculating-days-between-two-dates-with-java
            sum = price_per_day*(int)(TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS));

            st = conn.prepareStatement("INSERT INTO contract (car_vin,customer_id,employee_id,date_from,date_to,price,date_of_creating) VALUES (?,?,?,?,?,?,?)");
            st.setString(1, car_vin);
            st.setString(2, customer_id);
            st.setInt(3,id);
            st.setDate(4,date_from);
            st.setDate(5,date_to);
            st.setInt(6,sum);
            st.setDate(7,date);

            st.executeUpdate();

            return true;
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

    public int checkInfo(String car_vin, String customer_id, Date date_from, Date date_to) throws SQLException{

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        Properties prop = new Properties();
        InputStream input = null;

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

        return 3;
    }
}
