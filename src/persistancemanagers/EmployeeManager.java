package persistancemanagers;

import javafx.scene.control.Label;
import model.Employee;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class EmployeeManager {

    public void setHeader(Label labelFirstName, Label labelLastName) throws SQLException {
        Employee employee = null;

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            Properties prop = new Properties();
            InputStream input = new FileInputStream("src/properties");

            prop.load(input);


            Integer employee_id = Integer.valueOf(prop.getProperty("loggedID"));                       // get employee_id from properties file

            input.close();

            st = conn.prepareStatement("SELECT first_name,last_name FROM employee " +
                    "WHERE employee_id = '" + employee_id + "';");
            ResultSet rs = st.executeQuery();

            rs.next();

            labelFirstName.setText(rs.getString("first_name"));
            labelLastName.setText(rs.getString("last_name"));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            st.close();
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public boolean AddNewEmployeeToDatabase(String firstName, String lastName, String login, String password, String phone, String type) throws SQLException {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();


            // test if login name already exists in database
            st = conn.prepareStatement(
                    "SELECT EXISTS (SELECT login FROM employee WHERE login = '" + login + "') AS exist;"
            );
            ResultSet rs = st.executeQuery();

            rs.next();

            if(!rs.getBoolean("exist")) {
                st = conn.prepareStatement(
                        "INSERT INTO employee(first_name,last_name,login,password,phone,type) VALUES (?,?,?,?,?,?::employee_type);"
                );
                st.setString(1,firstName);
                st.setString(2,lastName);
                st.setString(3,login);
                st.setString(4,password);
                st.setString(5,phone);
                st.setString(6,type);

                st.executeUpdate();

                return true;
            } else {
                // user with login already exist
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

    public Employee LoginEngine(String login, String password) throws SQLException {
        Employee employee = null;

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();


            // test if login name already exists in database
            st = conn.prepareStatement("SELECT employee_id,first_name,last_name,phone,type FROM employee " +
                    "WHERE login = (SELECT login FROM employee WHERE login = '" + login + "' AND password = '" + password + "')" +
                    "AND password = (SELECT password FROM employee WHERE login = '" + login + "' AND password = '" + password + "');");
            ResultSet rs = st.executeQuery();

            if(!rs.next()){
                return null;
            }

            Integer employeeID = rs.getInt("employee_id");
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            String phone = rs.getString("phone");
            String type = rs.getString("type");

            employee = new Employee(employeeID,first_name,last_name,login,password,phone,type);

            return employee;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            st.close();
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }
}
