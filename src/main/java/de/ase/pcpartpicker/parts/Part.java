package de.ase.pcpartpicker.parts;
import de.ase.pcpartpicker.parts.additional.Type;
//import java.util.Date;

public abstract class Part {

    // Grundlegende Eigenschaften, die jedes PC Teil besitzt
    protected Type type;
    private double price;
    private String manufacturer;
    private String model;

    // optional
    //private Date releasedate;

    public Part(double price, String manufacturer, String model) {
        this.price = price;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    // Stand jetzt nicht gedacht, dass man die Eigenschaften ändern kann, deswegen nur Getter
    public String getType() {
        return type.getType();
    }

    public double getPrice() {
        return price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String toString() {
        return "Type: " + type.getType() + "\nPrice: " + price + "\nManufacturer: " + manufacturer + "\nModel: " + model + "\n";
    }
}
