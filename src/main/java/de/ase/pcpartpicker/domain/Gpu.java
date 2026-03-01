package de.ase.pcpartpicker.domain;

/**
 * Klasse für die GPU-Komponente.
 * @param id Eindeutige ID der GPU
 * @param name Name der GPU
 * @param vramGb VRAM-Kapazität der GPU in GB
 * @param price Preis der GPU
 * @author Fabio
 */
public class Gpu extends Component {

    private final int vramGb;

    public Gpu(int id, String name, int vramGb, double price) {
        super(id, name, price);
        this.vramGb = vramGb;
    }

    public int getVramGb() {
        return vramGb;
    }
}