package persistancemanagers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    "WHERE employee_id = ?;"
            );
            st.setInt(1,employee.getEmployeeID());

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

    public void updateEmployee(Employee employee) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "UPDATE employee " +
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



            st = conn.prepareStatement("SELECT * " +
                    "FROM employee " +
                    "WHERE ((first_name || ' ' || last_name) ILIKE ?) OR ((last_name || ' ' || first_name) ILIKE ?) " +
                    "OR login ILIKE ? OR phone ILIKE ? " +
                    "ORDER BY last_name,first_name " +
                    "LIMIT 500 " +
                    "OFFSET ?;"
            );
            st.setString(1,pattern + "%");
            st.setString(2,pattern + "%");
            st.setString(3,pattern + "%");
            st.setString(4,pattern + "%");
            st.setInt(5,offSet);

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

    public ObservableList<Employee> getEmployee(Integer offSet) {
        ObservableList<Employee> listOfEmployee = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "SELECT * " +
                    "FROM employee " +
                    "ORDER BY last_name,first_name " +
                    "LIMIT 500 " +
                    "OFFSET ?;"
            );
            st.setInt(1,offSet);

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

    public Employee getEmployeeFromDatabase(int employeeID) {
        Employee employee = null;

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "SELECT first_name,last_name,phone " +
                    "FROM employee " +
                    "WHERE employee_id = ?;"
            );
            st.setInt(1,employeeID);

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

    public ObservableList<Employee> getEmployeeForStatistic(Integer offSet) {
        ObservableList<Employee> listOfEmployee = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();



            st = conn.prepareStatement("SELECT e.first_name, e.last_name, sum(c.price), max(c.price)" +
                    "FROM contract c " +
                    "JOIN employee e on c.employee_id = e.employee_id " +
                    "JOIN customer c2 on c.customer_id = c2.customer_id " +
                    "WHERE customer_type = 'Fyzická osoba' " +
                    "GROUP BY e.employee_id " +
                    "HAVING SUM(c.price) > 25000 " +
                    "ORDER BY sum(c.price) DESC " +
                    "LIMIT 500 " +
                    "OFFSET ?;"
            );
            st.setInt(1,offSet);

            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                Integer sum = rs.getInt("sum");
                Integer max = rs.getInt("max");

                listOfEmployee.add(new Employee(firstName,lastName,sum,max));
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

    public boolean addNewEmployee(String firstName, String lastName,
                                  String login, String password, String phone, String type) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();


            // test if login name already exists in database
            st = conn.prepareStatement(
                    "SELECT EXISTS (SELECT login FROM employee WHERE login = ?) " +
                    "AS exist;"
            );
            st.setString(1,login);

            ResultSet rs = st.executeQuery();

            rs.next();

            if(!rs.getBoolean("exist")) {
                st = conn.prepareStatement(
                        "INSERT INTO employee(first_name,last_name,login,password,phone,type) " +
                        "VALUES (?,?,?,?,?,?::employee_type);"
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

    public Employee loginEngine(String login, String password) {
        Employee employee = null;

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();


            // test if login name already exists in database
            st = conn.prepareStatement(
                    "SELECT employee_id,first_name,last_name,phone,type " +
                    "FROM employee " +
                    "WHERE login = (SELECT login FROM employee WHERE login = ? AND password = ?)" +
                        "AND password = (SELECT password FROM employee WHERE login = ? AND password = ?);"
            );
            st.setString(1,login);
            st.setString(2,password);
            st.setString(3,login);
            st.setString(4,password);

            ResultSet rs = st.executeQuery();

            // doesnt exists
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
