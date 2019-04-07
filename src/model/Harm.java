package model;

import javafx.beans.property.SimpleStringProperty;

public class Harm {

    private int ID;
    private SimpleStringProperty typeOfHarm;
    private SimpleStringProperty harmDescription;

    public Harm(int ID, String typeOfHarm, String harmDescription) {
        this.ID = ID;
        this.typeOfHarm = new SimpleStringProperty(typeOfHarm);
        this.harmDescription = new SimpleStringProperty(harmDescription);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTypeOfHarm() {
        return typeOfHarm.get();
    }

    public SimpleStringProperty typeOfHarmProperty() {
        return typeOfHarm;
    }

    public void setTypeOfHarm(String typeOfHarm) {
        this.typeOfHarm = new SimpleStringProperty(typeOfHarm);
    }

    public String getHarmDescription() {
        return harmDescription.get();
    }

    public SimpleStringProperty harmDescriptionProperty() {
        return harmDescription;
    }

    public void setHarmDescription(String harmDescription) {
        this.harmDescription = new SimpleStringProperty(harmDescription);
    }
}
