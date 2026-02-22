package de.ase.pcpartpicker.domain.models;

public abstract class Component {
    private final String id; 
    private final String name; 
    private final long priceInCent; 
    private final String manufacturer; 
    private final int tdp; 

    public Component(String id, String name, long priceInCent, String manufacturer, int tdp) {
        this.id = id; 
        this.name = name;
        this.priceInCent = priceInCent; 
        this.manufacturer = manufacturer;
        this.tdp = tdp; 
    }

    public String getId() {return id;}
    public String getName() {return name;}
    public long getPriceInCents() {return priceInCent;}
    public String getManufacturer() {return manufacturer;}

    public double getPriceInEuro() {
        return priceInCent / 100.0; 
    }

}
