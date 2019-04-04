package persistancemanagers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Employee;
import model.LegalPerson;
import model.NaturalPerson;
import model.Person;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class PersonManager {

    public ObservableList<NaturalPerson> getNaturalPerson(Integer offset){
        ObservableList<NaturalPerson> listOfNaturalPerson = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();



            st = conn.prepareStatement("SELECT * FROM natural_person ORDER BY last_name,first_name LIMIT 500 OFFSET " + offset + ";");
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                String op = rs.getString("natural_person_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String adress = rs.getString("adress");
                String bankAccount = rs.getString("bank_account");
                String phone = rs.getString("phone");
                String type = rs.getString("customer_type");

                listOfNaturalPerson.add(new NaturalPerson(op,firstName,lastName,adress,bankAccount,phone));
            }

            return listOfNaturalPerson;

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

    public ObservableList<NaturalPerson> getNaturalPersonByFullName(String pattern, Integer offSet) {
        ObservableList<NaturalPerson> listOfNaturalPerson = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();



            st = conn.prepareStatement("select * " +
                    "from natural_person " +
                    "where ((first_name || ' ' || last_name) ILIKE '" + pattern + "%') OR ((last_name || ' ' || first_name) ILIKE '" + pattern + "%')" +
                    "OR natural_person_id ILIKE '" + pattern + "%' OR phone ILIKE '" + pattern + "%' OR adress ILIKE '" + pattern + "%'" +
                    "ORDER BY last_name,first_name " +
                    "LIMIT 500" +
                    "OFFSET " + offSet + ";"
            );
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String op = rs.getString("natural_person_id");
                String adress = rs.getString("adress");
                String bankAccount = rs.getString("bank_account");
                String phone = rs.getString("phone");
                String type = rs.getString("customer_type");

                listOfNaturalPerson.add(new NaturalPerson(op,firstName,lastName,adress,bankAccount,phone));
            }

            return listOfNaturalPerson;

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

    public void updateNaturalPersonInfo(NaturalPerson naturalPerson) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("UPDATE natural_person " +
                    "SET first_name = ?, last_name = ?, adress = ?, bank_account = ?, phone = ? " +
                    "WHERE natural_person_id = ?;"
            );
            st.setString(1,naturalPerson.getFirstName());
            st.setString(2,naturalPerson.getLastName());
            st.setString(3,naturalPerson.getAdress());
            st.setString(4,naturalPerson.getBankAccount());
            st.setString(5,naturalPerson.getPhoneNumber());
            st.setString(6,naturalPerson.getID());

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

    public ObservableList<LegalPerson> getLegalPerson(Integer offset){
        ObservableList<LegalPerson> listOfLegalPerson = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();



            st = conn.prepareStatement("SELECT * FROM legal_person ORDER BY dic,legal_person_ico LIMIT 500 OFFSET " + offset + ";");
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                String ico = rs.getString("legal_person_ico");
                String dic = rs.getString("dic");
                String name = rs.getString("name_of_organization");
                String adress = rs.getString("adress");
                String bankAccount = rs.getString("bank_account");
                String phone = rs.getString("phone");
                String type = rs.getString("customer_type");

                listOfLegalPerson.add(new LegalPerson(ico,dic,name,adress,bankAccount,phone));
            }

            return listOfLegalPerson;

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

    public ObservableList<LegalPerson> getLegalPersonByFullName(String pattern, Integer offSet) {
        ObservableList<LegalPerson> listOfLegalPerson = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();



            st = conn.prepareStatement("select * " +
                    "from legal_person " +
                    "where ((legal_person_ico || ' ' || dic) ILIKE '" + pattern + "%') OR ((dic || ' ' || legal_person_ico) ILIKE '" + pattern + "%')" +
                    "OR name_of_organization ILIKE '" + pattern + "%' OR phone ILIKE '" + pattern + "%' OR adress ILIKE '" + pattern + "%'" +
                    "ORDER BY legal_person_ico,dic " +
                    "LIMIT 500" +
                    "OFFSET " + offSet + ";"
            );
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                String ico = rs.getString("legal_person_ico");
                String dic = rs.getString("dic");
                String name = rs.getString("name_of_organization");
                String adress = rs.getString("adress");
                String bankAccount = rs.getString("bank_account");
                String phone = rs.getString("phone");
                String type = rs.getString("customer_type");

                listOfLegalPerson.add(new LegalPerson(ico,dic,name,adress,bankAccount,phone));
            }

            return listOfLegalPerson;

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

    public void updateLegalPersonInfo(LegalPerson legalPerson) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("UPDATE legal_person " +
                    "SET name_of_organization = ?, adress = ?, bank_account = ?, phone = ? " +
                    "WHERE legal_person_ico = ?;"
            );
            st.setString(1,legalPerson.getName());
            st.setString(2,legalPerson.getAdress());
            st.setString(3,legalPerson.getBankAccount());
            st.setString(4,legalPerson.getPhoneNumber());
            st.setString(5,legalPerson.getID());

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

    public Person getPersonFromDatabase(String personID) throws SQLException {
        Person person = null;

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();


            st = conn.prepareStatement("SELECT customer_type FROM customer " +
                    "WHERE customer_id ILIKE '" + personID + "';"
            );
            ResultSet rs = st.executeQuery();

            rs.next();

            String customer_type = rs.getString("customer_type");

            if(customer_type.equals("Fyzická osoba")) {
                st = conn.prepareStatement("SELECT first_name,last_name,adress,bank_account,phone FROM natural_person " +
                        "WHERE natural_person_id ILIKE '" + personID + "';"
                );
                rs = st.executeQuery();

                rs.next();

                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String adress = rs.getString("adress");
                String bank_account = rs.getString("bank_account");
                String phone = rs.getString("phone");

                person = new NaturalPerson(personID,first_name,last_name,adress,bank_account,phone);

            } else {
                st = conn.prepareStatement("SELECT dic,name_of_organization,adress,bank_account,phone FROM legal_person " +
                        "WHERE legal_person_ico ILIKE '" + personID + "';"
                );
                rs = st.executeQuery();

                rs.next();

                String dic = rs.getString("dic");
                String name_of_organization = rs.getString("name_of_organization");
                String adress = rs.getString("adress");
                String bank_account = rs.getString("bank_account");
                String phone = rs.getString("phone");

                person = new LegalPerson(personID,dic,name_of_organization,adress,bank_account,phone);
            }

            return person;
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

    public int addNewLegalPersonToDatabase(String customerId,String dic,String name,String adress,String bankAccount,String phone) throws SQLException{
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT EXISTS (SELECT customer_id FROM customer WHERE customer_id ILIKE '" + customerId + "') AS exist;");
            ResultSet rs = st.executeQuery();
            rs.next();

            st = conn.prepareStatement("SELECT EXISTS (SELECT dic FROM legal_person WHERE dic ILIKE '" + dic + "') AS exist;");
            ResultSet rs2 = st.executeQuery();
            rs2.next();

            if(!rs.getBoolean("exist") && !rs2.getBoolean("exist")) {
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

                return 3;
            }

            if(rs.getBoolean("exist") && rs2.getBoolean("exist")){
                return 2;
            }

            if (rs.getBoolean("exist")){
                return 0;
            }

            if (rs2.getBoolean("exist")) {
                return 1;
            }

            return 0;
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

    public boolean addNewNaturalPersonToDatabase(String customerId,String firstName,String lastName,String adress,String bankAccount,String phone) throws SQLException{
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT EXISTS (SELECT customer_id FROM customer WHERE customer_id ILIKE '" + customerId + "') AS exist;");
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
