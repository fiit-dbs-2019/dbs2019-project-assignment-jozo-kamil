package persistancemanagers;

import javafx.fxml.FXML;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class PersonManager {

    public boolean addNewLegalPersonToDatabase(String customerId,String dic,String name,String adress,String bankAccount,String phone) throws SQLException{
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT EXISTS (SELECT customer_id FROM customer WHERE customer_id = '" + customerId + "') AS exist;");
            ResultSet rs = st.executeQuery();
            rs.next();

            if(!rs.getBoolean("exist")) {
                st = conn.prepareStatement("INSERT INTO customer (customer_id,customer_type) VALUES (?,?::person_type)");
                st.setString(1, customerId);
                st.setString(2, "Právnická osoba");
                st.executeUpdate();

                st = conn.prepareStatement("INSERT INTO legal_person (legal_person_ico,dic,name_of_organization" +
                        ",adress,bank_account,phone) VALUES (?,?,?,?,?,?)");
                st.setString(1, customerId);
                st.setString(2, dic);
                st.setString(3, name);
                st.setString(4, adress);
                st.setString(5, bankAccount);
                st.setString(6, phone);
                st.executeUpdate();

                return true;
            }
            else return false;
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

    public boolean addNewNaturalPersonToDatabase(String customerId,String firstName,String lastName,String adress,String bankAccount,String phone) throws SQLException{
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT EXISTS (SELECT customer_id FROM customer WHERE customer_id = '" + customerId + "') AS exist;");
            ResultSet rs = st.executeQuery();
            rs.next();

            if(!rs.getBoolean("exist")) {
                st = conn.prepareStatement("INSERT INTO customer (customer_id) VALUES (?)");
                st.setString(1, customerId);
                st.executeUpdate();

                st = conn.prepareStatement("INSERT INTO natural_person (natural_person_id,first_name,last_name,adress" +
                        ",bank_account,phone) VALUES (?,?,?,?,?,?)");
                st.setString(1, customerId);
                st.setString(2, firstName);
                st.setString(3, lastName);
                st.setString(4, adress);
                st.setString(5, bankAccount);
                st.setString(6, phone);
                st.executeUpdate();

                return true;
            }
            else {
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
}
