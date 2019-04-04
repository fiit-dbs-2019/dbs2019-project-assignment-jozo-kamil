package model;

import javafx.beans.property.SimpleStringProperty;

public class NaturalPerson extends Person {

    private SimpleStringProperty op;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty adress;
    private SimpleStringProperty bankAccount;
    private SimpleStringProperty phoneNumber;

    public NaturalPerson(){

    }

    public NaturalPerson(String op, String firstName, String lastName, String adress, String bankAccount, String phoneNumber) {
        this.op = new SimpleStringProperty(op);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.adress = new SimpleStringProperty(adress);
        this.bankAccount = new SimpleStringProperty(bankAccount);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }

    public String getOp() {
        return op.get();
    }

    public SimpleStringProperty opProperty() {
        return op;
    }

    public void setOp(String op) {
        this.op.set(op);
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

    public String getAdress() {
        return adress.get();
    }

    public SimpleStringProperty adressProperty() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress.set(adress);
    }

    public String getBankAccount() {
        return bankAccount.get();
    }

    public SimpleStringProperty bankAccountProperty() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount.set(bankAccount);
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
}

