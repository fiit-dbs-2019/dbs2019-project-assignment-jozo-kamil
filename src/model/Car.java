package model;

import java.time.LocalDate;

public class Car {

    private String brand;
    private String model;
    private String body_style;
    private float engine_capacity;
    private int engine_power;
    private String gear_box;
    private String fuel;
    private String color;
    private int price_per_day;
    private LocalDate year_of_production;
    private String spz;
    private int mileage;
    private String car_vin;

    Car(String brand,String model,String body_style,float engine_capacity,int engine_power,String gear_box,String fuel,String color,int price_per_day,LocalDate year_of_production,String spz,int mileage,String car_vin) {
        this.brand=brand;
        this.model=model;
        this.body_style=body_style;
        this.engine_capacity=engine_capacity;
        this.engine_power=engine_power;
        this.gear_box=gear_box;
        this.fuel=fuel;
        this.color=color;
        this.price_per_day=price_per_day;
        this.year_of_production=year_of_production;
        this.spz=spz;
        this.mileage=mileage;
        this.car_vin=car_vin;
    }

    public void setBody_style(String body_style) {
        this.body_style = body_style;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setEngine_capacity(float engine_capacity) {
        this.engine_capacity = engine_capacity;
    }

    public void setEngine_power(int engine_power) {
        this.engine_power = engine_power;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public void setGear_box(String gear_box) {
        this.gear_box = gear_box;
    }

    public void setCar_vin(String car_vin) {
        this.car_vin = car_vin;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrice_per_day(int price_per_day) {
        this.price_per_day = price_per_day;
    }

    public void setSpz(String spz) {
        this.spz = spz;
    }

    public void setYear_of_production(LocalDate year_of_production) {
        this.year_of_production = year_of_production;
    }

    public float getEngine_capacity() {
        return engine_capacity;
    }

    public int getEngine_power() {
        return engine_power;
    }

    public int getMileage() {
        return mileage;
    }

    public int getPrice_per_day() {
        return price_per_day;
    }

    public String getBody_style() {
        return body_style;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getCar_vin() {
        return car_vin;
    }

    public String getFuel() {
        return fuel;
    }

    public String getGear_box() {
        return gear_box;
    }

    public String getModel() {
        return model;
    }

    public String getSpz() {
        return spz;
    }

    public LocalDate getYear_of_production() {
        return year_of_production;
    }

}
