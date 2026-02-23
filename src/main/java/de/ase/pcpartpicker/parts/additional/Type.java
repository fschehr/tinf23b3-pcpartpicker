package de.ase.pcpartpicker.parts.additional;

// Klasse die Typen validiert, z.B. CPU, GPU, RAM, etc.
public class Type {

    private String type;
    private String[] types = { "CPU", "GPU", "RAM", "Motherboard", "HDD", "SATA", "M.2", "PSU", "Case", "Cooling" };

    public Type(String type) {
        setType(type);
    }

    private void setType(String type) {
        for(String t: types) {
            if(t.equals(type)) {
                this.type = type;
                return;
            }
        }
        
        throw new IllegalArgumentException(type + " is not a valid type. Valid types are: " + String.join("\n", types));
    }

    public String getType() {
        return type;
    }
    
}
