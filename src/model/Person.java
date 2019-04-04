package model;

import javafx.beans.property.SimpleStringProperty;

public abstract class Person {
    private SimpleStringProperty ID;

    public Person(String ID) {
        this.ID = new SimpleStringProperty(ID);
    }

    public String getID() {
        return ID.get();
    }

    public SimpleStringProperty IDProperty() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = new SimpleStringProperty(ID);
    }
}
