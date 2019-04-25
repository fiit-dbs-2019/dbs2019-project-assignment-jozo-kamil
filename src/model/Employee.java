package model;

import javafx.beans.property.SimpleStringProperty;

public class Employee {

    private Integer employeeID;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty login;
    private SimpleStringProperty password;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty type;

    //variables for statistics
    private Integer sum;
    private Integer max;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String phoneNumber) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }

    public Employee(String firstName, String lastName, Integer sum, Integer max) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.sum = sum;
        this.max = max;
    }

    public Employee(Integer employeeID, String firstName, String lastName, String login, String password, String phoneNumber, String type) {
        this.employeeID = employeeID;
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.type = new SimpleStringProperty(type);
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public Integer getSum() {
        return sum;
    }

    public Integer getMax() {
        return max;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public void setType(String type) {
        this.type.set(type);
    }
}
