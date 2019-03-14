package model;

public class LegalPerson extends Person {

    private String ico;
    private String dic;
    private String name;
    private String adress;
    private String bankAccount;
    private String phoneNumber;

    public LegalPerson(){

    }

    public LegalPerson(String ico,String dic,String name,String adress,String bankAccount,String phoneNumber){
        this.ico=ico;
        this.dic=dic;
        this.name=name;
        this.adress=adress;
        this.bankAccount=bankAccount;
        this.phoneNumber=phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getAdress() {
        return adress;
    }

    public String getDic() {
        return dic;
    }

    public String getIco() {
        return ico;
    }

    public String getName() {
        return name;
    }
}
