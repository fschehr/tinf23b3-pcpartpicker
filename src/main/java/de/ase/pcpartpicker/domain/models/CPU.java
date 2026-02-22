package de.ase.pcpartpicker.domain.models;

public class CPU extends Component {
    private final String socket; 
    private final int cores; 

    public CPU(String id, String name, long price, String manufacturer, int tdp, String socket, int cores) {
        super(id,name,price,manufacturer, tdp); 
        this.socket = socket; 
        this.cores = cores;
    }


    public String getSocket() {return socket;}
    public int getCores() {return cores;}
     
}
