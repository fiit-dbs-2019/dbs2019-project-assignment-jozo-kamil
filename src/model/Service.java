package model;

import javafx.beans.property.SimpleStringProperty;

public class Service {
    private Integer serviceID;
    private SimpleStringProperty serviceName;
    private SimpleStringProperty serviceLocation;

    public Service(Integer serviceID, String serviceName, String serviceLocation) {
        this.serviceID = serviceID;
        this.serviceName = new SimpleStringProperty(serviceName);
        this.serviceLocation = new SimpleStringProperty(serviceLocation);
    }

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
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

    public String getNameAndLocation() {
        return serviceName.get() + ", " + serviceLocation.get();
    }
}
