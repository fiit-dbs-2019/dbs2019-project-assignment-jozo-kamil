package model;

public class NaturalPerson {

    private String firstName;
    private String lastName;
    private String op;
    private String adress;
    private String bankAccount;
    private String phoneNumber;

    NaturalPerson(){

    }

    NaturalPerson(String firstName, String lastName, String op, String adress, String bankAccount, String phoneNumber){
        this.adress=adress;
        this.bankAccount=bankAccount;
        this.firstName=firstName;
        this.lastName=lastName;
        this.op=op;
        this.phoneNumber=phoneNumber;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAdress() {
        return adress;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOp() {
        return op;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

