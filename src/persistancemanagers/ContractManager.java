package persistancemanagers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contract;
import model.Employee;
import model.Harm;
import model.NaturalPerson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ContractManager {

    public boolean deleteContract(Contract contract) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "DELETE FROM contract " +
                    "WHERE contract_id = ?;"
            );
            st.setInt(1,contract.getContract_id());

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

    public boolean checkIfHarmHasRepair(int harmID){
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();


            // test if login name already exists in database
            st = conn.prepareStatement(
                    "SELECT exists (" +
                            "SELECT r.repair_id " +
                            "FROM repair r " +
                            "JOIN harm h ON r.repair_id = h.repair_id " +
                            "WHERE harm_id = ?) " +
                    "AS exists;"
            );
            st.setInt(1,harmID);

            ResultSet rs = st.executeQuery();

            rs.next();

            if(!rs.getBoolean("exists")) {
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

    public void addNewHarm(Contract contract, String typeOfHarm, String description){
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            Date date = Date.valueOf(
                    new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

            st = conn.prepareStatement(
                    "INSERT INTO harm(type,date,description) " +
                    "VALUES (?::type_of_harm,?,?) returning harm_id;"
            );
            st.setString(1,typeOfHarm);
            st.setDate(2,date);
            st.setString(3,description);

            ResultSet rs = st.executeQuery();

            rs.next();

            int harm_id = rs.getInt("harm_id");


            st = conn.prepareStatement(
                    "UPDATE contract SET harm_id = ? " +
                    "WHERE contract_id = ?;"
            );
            st.setInt(1,harm_id);
            st.setInt(2,contract.getContract_id());

            st.executeUpdate();

            contract.setHarm_id(harm_id);
            contract.setHarm(new Harm(harm_id,typeOfHarm,description));

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

    public void setHarmToContract(Contract contract){
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "SELECT type, description " +
                    "FROM harm " +
                    "WHERE harm_id = ?;"

            );
            st.setInt(1,contract.getHarm_id());

            ResultSet rs = st.executeQuery();

           rs.next();

           String type = rs.getString("type");
           String description = rs.getString("description");

           contract.setHarm(new Harm(contract.getHarm_id(),type,description));

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

    public ObservableList<Contract> getContractForSpecificEmployee(Employee employee, Integer offSet) {
        ObservableList<Contract> listOfContract = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT * " +
                    "FROM contract " +
                    "WHERE employee_id = ? " +
                    "ORDER BY contract_id " +
                    "LIMIT 500 " +
                    "OFFSET ?;"
            );
            st.setInt(1,employee.getEmployeeID());
            st.setInt(2,offSet);

            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                int contract_id = rs.getInt("contract_id");
                String car_vin = rs.getString("car_vin");
                String customer_id = rs.getString("customer_id");
                int employee_id = rs.getInt("employee_id");
                Date date_from = rs.getDate("date_from");
                Date date_to = rs.getDate("date_to");
                float price = rs.getFloat("price");
                Date date_of_creating = rs.getDate("date_of_creating");
                int harm_id = rs.getInt("harm_id");

                if (date_of_creating == null) date_of_creating = date_from;

                listOfContract.add(new Contract(contract_id,car_vin,customer_id,employee_id,date_from,date_to,price,date_of_creating,harm_id));
            }

            return listOfContract;

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

    public ObservableList<Contract> getContract(Integer offSet){
        ObservableList<Contract> listOfContract = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "SELECT * " +
                    "FROM contract " +
                    "ORDER BY contract_id " +
                    "LIMIT 500 OFFSET ?;"
            );
            st.setInt(1,offSet);

            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                int contract_id = rs.getInt("contract_id");
                String car_vin = rs.getString("car_vin");
                String customer_id = rs.getString("customer_id");
                int employee_id = rs.getInt("employee_id");
                Date date_from = rs.getDate("date_from");
                Date date_to = rs.getDate("date_to");
                float price = rs.getFloat("price");
                Date date_of_creating = rs.getDate("date_of_creating");
                int harm_id = rs.getInt("harm_id");

                if (date_of_creating == null) date_of_creating = date_from;

                listOfContract.add(new Contract(contract_id,car_vin,customer_id,employee_id,date_from,date_to,price,date_of_creating,harm_id));
            }

            return listOfContract;

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

    public ObservableList<Contract> getContractByAtributes(String pattern, Integer offSet) {
        ObservableList<Contract> listOfContract = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT * " +
                    "FROM contract " +
                    "WHERE (car_vin ILIKE ?) " +
                    "OR customer_id ILIKE ? " +
                    "ORDER BY contract_id " +
                    "LIMIT 500" +
                    "OFFSET ?;"
            );
            st.setString(1,pattern + "%");
            st.setString(2,pattern + "%");
            st.setInt(3,offSet);

            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                int contract_id = rs.getInt("contract_id");
                String car_vin = rs.getString("car_vin");
                String customer_id = rs.getString("customer_id");
                int employee_id = rs.getInt("employee_id");
                Date date_from = rs.getDate("date_from");
                Date date_to = rs.getDate("date_to");
                float price = rs.getFloat("price");
                Date date_of_creating = rs.getDate("date_of_creating");
                int harm_id = rs.getInt("harm_id");

                if (date_of_creating == null) date_of_creating = date_from;

                listOfContract.add(new Contract(contract_id,car_vin,customer_id,employee_id,date_from,date_to,price,date_of_creating,harm_id));
            }

            return listOfContract;

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

    public boolean createNewContract(String carVIN, String customerID,
                                     Date dateFrom, Date dateTo, Integer employeeID) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();
            ResultSet rs;

            int price_per_day;
            int sum;
            long diff = dateTo.getTime()-dateFrom.getTime();
            Date date = Date.valueOf(
                    new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

            st = conn.prepareStatement(
                    "SELECT price_per_day " +
                    "FROM car_info " +
                    "WHERE car_info_id = (" +
                        "SELECT car_info_id " +
                        "FROM car " +
                        "WHERE car_vin = ?);"
            );
            st.setString(1,carVIN);

            rs = st.executeQuery();
            rs.next();

            price_per_day=rs.getInt("price_per_day");

            //calculate days between 2 days
            //source: https://stackoverflow.com/questions/20165564/calculating-days-between-two-dates-with-java
            sum = price_per_day*(int)(TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS));

            st = conn.prepareStatement(
                    "INSERT INTO contract (car_vin,customer_id,employee_id," +
                            "date_from,date_to,price,date_of_creating) " +
                    "VALUES (?,?,?,?,?,?,?)"
            );
            st.setString(1, carVIN);
            st.setString(2, customerID);
            st.setInt(3,employeeID);
            st.setDate(4,dateFrom);
            st.setDate(5,dateTo);
            st.setInt(6,sum);
            st.setDate(7,date);

            st.executeUpdate();

            return true;
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

    public int checkInfo(String carVIN, String customerID, Date date_from, Date date_to) {

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "SELECT EXISTS (" +
                            "SELECT car_vin " +
                            "FROM car " +
                            "WHERE car_vin = ?) " +
                    "AS exist;"
            );
            st.setString(1,carVIN);

            ResultSet rs = st.executeQuery();
            rs.next();

            st = conn.prepareStatement(
                    "SELECT EXISTS (" +
                            "SELECT customer_id " +
                            "FROM customer " +
                            "WHERE customer_id = ?) " +
                    "AS exist;"
            );
            st.setString(1,customerID);

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

        return 3;
    }
}
