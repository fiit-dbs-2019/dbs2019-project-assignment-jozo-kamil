package persistancemanagers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
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
import java.util.Observable;
import java.util.Properties;

public class EmployeeManager {

    public boolean deleteEmployee(Employee employee) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "DELETE FROM employee " +
                            "WHERE employee_id = " + employee.getEmployeeID() + ";"
            );
            int numberOfRows = st.executeUpdate();

            if(numberOfRows != 0) {
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

    public void updateEmployeeInfo(Employee employee) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("UPDATE employee " +
                    "SET first_name = ?, last_name = ?, password = ?, phone = ? " +
                    "WHERE employee_id = ?;"
            );
            st.setString(1,employee.getFirstName());
            st.setString(2,employee.getLastName());
            st.setString(3,employee.getPassword());
            st.setString(4,employee.getPhoneNumber());
            st.setInt(5,employee.getEmployeeID());

            st.executeUpdate();

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

    public ObservableList<Employee> getEmployeeByFullName(String pattern, Integer offSet) {
        ObservableList<Employee> listOfEmployee = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();



            st = conn.prepareStatement("select * " +
                    "from employee " +
                    "where ((first_name || ' ' || last_name) ILIKE '" + pattern + "%') OR ((last_name || ' ' || first_name) ILIKE '" + pattern + "%')" +
                    "OR login ILIKE '" + pattern + "%' OR phone ILIKE '" + pattern + "%'" +
                    "ORDER BY last_name,first_name " +
                    "LIMIT 500" +
                    "OFFSET " + offSet + ";"
            );
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                Integer employeeID = rs.getInt("employee_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String login = rs.getString("login");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                String type = rs.getString("type");

                listOfEmployee.add(new Employee(employeeID,firstName,lastName,login,password,phone,type));
            }

            return listOfEmployee;

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

    public ObservableList<Employee> getEmployee(Integer offset) {
        ObservableList<Employee> listOfEmployee = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT * FROM employee ORDER BY last_name,first_name LIMIT 500 OFFSET " + offset + ";");
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                Integer employeeID = rs.getInt("employee_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String login = rs.getString("login");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                String type = rs.getString("type");

                listOfEmployee.add(new Employee(employeeID,firstName,lastName,login,password,phone,type));
            }

            return listOfEmployee;

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

    public Employee getEmployeeFromDatabase(int id) throws SQLException {
        Employee employee = null;

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT first_name,last_name,phone FROM employee " +
                    "WHERE employee_id = '" + id + "';"
            );
            ResultSet rs = st.executeQuery();

            rs.next();

            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String phone = rs.getString("phone");

            employee = new Employee(firstName,lastName,phone);

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
