package de.ase.pcpartpicker.domain.models;


public class Mainboard extends Component {
    private final String chipset;
    private final String socket; 

    public Mainboard(String id, String name, long price, String manufacturer, int tdp, String chipset, String socket) {
        super(id, name, price, manufacturer, tdp);
        this.chipset = chipset;
        this.socket = socket; 
    }

    public String getChipset() {return chipset;}
    public String getSocket() {return socket;}
}