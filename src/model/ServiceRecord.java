package model;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class ServiceRecord {

    private SimpleStringProperty serviceName;
    private SimpleStringProperty serviceLocation;
    private SimpleStringProperty typeOfRepair;
    private Date dateOfService;
    private Float servicePrice;

    public ServiceRecord(String serviceName, String serviceLocation, String typeOfRepair, Date dateOfService, Float servicePrice) {
        this.serviceName = new SimpleStringProperty(serviceName);
        this.serviceLocation = new SimpleStringProperty(serviceLocation);
        this.typeOfRepair = new SimpleStringProperty(typeOfRepair);
        this.dateOfService = dateOfService;
        this.servicePrice = servicePrice;
    }

    public String getServiceName() {
        return serviceName.get();
    }

    public SimpleStringProperty serviceNameProperty() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = new SimpleStringProperty(serviceName);
    }

    public String getServiceLocation() {
        return serviceLocation.get();
    }

    public SimpleStringProperty serviceLocationProperty() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = new SimpleStringProperty(serviceLocation);
    }

    public String getTypeOfRepair() {
        return typeOfRepair.get();
    }

    public SimpleStringProperty typeOfRepairProperty() {
        return typeOfRepair;
    }

    public void setTypeOfRepair(String typeOfRepair) {
        this.typeOfRepair = new SimpleStringProperty(typeOfRepair);
    }

    public Date getDateOfService() {
        return dateOfService;
    }

    public void setDateOfService(Date dateOfService) {
        this.dateOfService = dateOfService;
    }

    public Float getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(Float servicePrice) {
        this.servicePrice = servicePrice;
    }
}
