package model;

import javafx.beans.property.SimpleStringProperty;

public class LegalPerson extends Person {

    private SimpleStringProperty dic;
    private SimpleStringProperty name;
    private SimpleStringProperty adress;
    private SimpleStringProperty bankAccount;
    private SimpleStringProperty phoneNumber;

    public LegalPerson(String ID){
        super(ID);

    }

    public LegalPerson(String ico,String dic,String name,String adress,String bankAccount,String phoneNumber){
        super(ico);
        this.dic=new SimpleStringProperty(dic);
        this.name=new SimpleStringProperty(name);
        this.adress=new SimpleStringProperty(adress);
        this.bankAccount=new SimpleStringProperty(bankAccount);
        this.phoneNumber=new SimpleStringProperty(phoneNumber);
    }

    public String getDic() {
        return dic.get();
    }

    public SimpleStringProperty dicProperty() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic.set(dic);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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
