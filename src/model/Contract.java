package model;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class Contract {

    private int contract_id;
    private SimpleStringProperty car_vin;
    private SimpleStringProperty customer_id;
    private int employee_id;
    private Date date_from;
    private Date date_to;
    private float price;
    private Date date_of_creating;
    private int harm_id;
    private Harm harm;

    public Contract(int contract_id, String car_vin, String customer_id, int employee_id, Date date_from, Date date_to, float price, Date date_of_creating, int harm_id) {
        this.contract_id = contract_id;
        this.car_vin =  new SimpleStringProperty(car_vin);
        this.customer_id =  new SimpleStringProperty(customer_id);
        this.employee_id = employee_id;
        this.date_from = date_from;
        this.date_to = date_to;
        this.price = price;
        this.date_of_creating = date_of_creating;
        this.harm_id = harm_id;
    }

    public int getContract_id() {
        return contract_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }

    public String getCar_vin() {
        return car_vin.get();
    }

    public SimpleStringProperty car_vinProperty() {
        return car_vin;
    }

    public void setCar_vin(String car_vin) {
        this.car_vin.set(car_vin);
    }

    public String getCustomer_id() {
        return customer_id.get();
    }

    public SimpleStringProperty customer_idProperty() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id.set(customer_id);
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public Date getDate_from() {
        return date_from;
    }

    public void setDate_from(Date date_from) {
        this.date_from = date_from;
    }

    public Date getDate_to() {
        return date_to;
    }

    public void setDate_to(Date date_to) {
        this.date_to = date_to;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDate_of_creating() {
        return date_of_creating;
    }

    public void setDate_of_creating(Date date_of_creating) {
        this.date_of_creating = date_of_creating;
    }

    public int getHarm_id() {
        return harm_id;
    }

    public void setHarm_id(int harm_id) {
        this.harm_id = harm_id;
    }

    public Harm getHarm() {
        return harm;
    }

    public void setHarm(Harm harm) {
        this.harm = harm;
    }
}
