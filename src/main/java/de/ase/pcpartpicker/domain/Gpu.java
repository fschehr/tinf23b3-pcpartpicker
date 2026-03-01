package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.Type;

/**
 * Klasse für die GPU-Komponente.
 * @param id Eindeutige ID der GPU
 * @param name Name der GPU
 * @param manufacturer Hersteller der GPU
 * @param vramGb VRAM-Kapazität der GPU in GB
 * @param price Preis der GPU
 * @author Fabio
 */
public class Gpu extends Component {

    private final int vramGb;

    public Gpu(int id, Type type, String name, double price, Manufacturer manufacturer, int vramGb) {
        super(id, type, name, price, manufacturer);
        this.vramGb = vramGb;
    }

    public int getVramGb() {
        return vramGb;
    }
}