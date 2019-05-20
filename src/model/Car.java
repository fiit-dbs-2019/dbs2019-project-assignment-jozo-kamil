package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.time.LocalDate;

public class Car {

    private SimpleStringProperty car_vin;
    private Integer carInfoID;
    private SimpleStringProperty brand;
    private SimpleStringProperty model;
    private SimpleStringProperty body_style;
    private Float engine_capacity;
    private Integer engine_power;
    private SimpleStringProperty gear_box;
    private SimpleStringProperty fuel;
    private SimpleStringProperty color;
    private Float price_per_day;
    private Date year_of_production;
    private Integer mileage;
    private SimpleStringProperty spz;
    private ObservableList<ServiceRecord> serviceRecords;

    public Car(String car_vin, Integer carInfoID, Date year_of_production, Integer mileage, String spz) {
        this.car_vin = new SimpleStringProperty(car_vin);
        this.carInfoID = carInfoID;
        this.year_of_production = year_of_production;
        this.mileage = mileage;
        this.spz = new SimpleStringProperty(spz);
    }

    public Car(Date year_of_production, String spz, int mileage, String car_vin) {
        this.year_of_production = year_of_production;
        this.spz = new SimpleStringProperty(spz);
        this.mileage = mileage;
        this.car_vin = new SimpleStringProperty(car_vin);
    }

    public Car(String brand, String model, String body_style, float engine_capacity, int engine_power, String gear_box, String fuel,
               String color, Float price_per_day, Date year_of_production, String spz, int mileage, String car_vin) {
        this.brand = new SimpleStringProperty(brand);
        this.model = new SimpleStringProperty(model);
        this.body_style = new SimpleStringProperty(body_style);
        this.engine_capacity = engine_capacity;
        this.engine_power = engine_power;
        this.gear_box = new SimpleStringProperty(gear_box);
        this.fuel = new SimpleStringProperty(fuel);
        this.color = new SimpleStringProperty(color);
        this.price_per_day = price_per_day;
        this.year_of_production = year_of_production;
        this.spz = new SimpleStringProperty(spz);
        this.mileage = mileage;
        this.car_vin = new SimpleStringProperty(car_vin);
    }

    public Car(String car_vin, String brand, String model, String body_style, float engine_capacity, int engine_power, String gear_box,
               String fuel, String color, Float price_per_day, Date year_of_production, int mileage, String spz) {
        this.car_vin = new SimpleStringProperty(car_vin);
        this.brand = new SimpleStringProperty(brand);
        this.model = new SimpleStringProperty(model);
        this.body_style = new SimpleStringProperty(body_style);
        this.engine_capacity = engine_capacity;
        this.engine_power = engine_power;
        this.gear_box = new SimpleStringProperty(gear_box);
        this.fuel = new SimpleStringProperty(fuel);
        this.color = new SimpleStringProperty(color);
        this.price_per_day = price_per_day;
        this.year_of_production = year_of_production;
        this.mileage = mileage;
        this.spz = new SimpleStringProperty(spz);
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

    public Integer getCarInfoID() {
        return carInfoID;
    }

    public void setCarInfoID(Integer carInfoID) {
        this.carInfoID = carInfoID;
    }

    public String getBrand() {
        return brand.get();
    }

    public SimpleStringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = new SimpleStringProperty(brand);
        //this.brand.set(brand);
    }

    public String getModel() {
        return model.get();
    }

    public SimpleStringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model = new SimpleStringProperty(model);
    }

    public String getBody_style() {
        return body_style.get();
    }

    public SimpleStringProperty body_styleProperty() {
        return body_style;
    }

    public void setBody_style(String body_style) {
        this.body_style = new SimpleStringProperty(body_style);
    }

    public Float getEngine_capacity() {
        return engine_capacity;
    }

    public void setEngine_capacity(Float engine_capacity) {
        this.engine_capacity = engine_capacity;
    }

    public Integer getEngine_power() {
        return engine_power;
    }

    public void setEngine_power(Integer engine_power) {
        this.engine_power = engine_power;
    }

    public String getGear_box() {
        return gear_box.get();
    }

    public SimpleStringProperty gear_boxProperty() {
        return gear_box;
    }

    public void setGear_box(String gear_box) {
        this.gear_box = new SimpleStringProperty(gear_box);
    }

    public String getFuel() {
        return fuel.get();
    }

    public SimpleStringProperty fuelProperty() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = new SimpleStringProperty(fuel);
    }

    public String getColor() {
        return color.get();
    }

    public SimpleStringProperty colorProperty() {
        return color;
    }

    public void setColor(String color) {
        this.color = new SimpleStringProperty(color);
    }

    public Float getPrice_per_day() {
        return price_per_day;
    }

    public void setPrice_per_day(Float price_per_day) {
        this.price_per_day = price_per_day;
    }

    public Date getYear_of_production() {
        return year_of_production;
    }

    public void setYear_of_production(Date year_of_production) {
        this.year_of_production = year_of_production;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getSpz() {
        return spz.get();
    }

    public SimpleStringProperty spzProperty() {
        return spz;
    }

    public void setSpz(String spz) {
        this.spz = new SimpleStringProperty(spz);
    }

    public ObservableList<ServiceRecord> getServiceRecords() {
        return serviceRecords;
    }

    public void setServiceRecords(ObservableList<ServiceRecord> serviceRecords) {
        this.serviceRecords = serviceRecords;
    }

    public void addServiceRecord(ServiceRecord serviceRecord) {
        this.serviceRecords.add(serviceRecord);
    }
}
