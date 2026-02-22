package de.ase.pcpartpicker.parts;
//import java.util.Date;

public abstract class Part {

    // Grundlegende Eigenschaften, die jedes PC Teil besitzt
    private float price;
    private String type;
    private String manufacturer;
    private String model;

    // optional
    //private Date releasedate;

    public Part(float price, String manufacturer, String model) {
        this.price = price;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    // Stand jetzt nicht gedacht, dass man die Eigenschaften ändern kann, deswegen nur Getter
    public float getPrice() {
        return price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }
}
