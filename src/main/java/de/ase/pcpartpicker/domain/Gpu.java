package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

/**
 * Klasse für die GPU-Komponente.
 * @param id Eindeutige ID der GPU
 * @param name Name der GPU
 * @param manufacturer Hersteller der GPU
 * @param vramGB VRAM-Kapazität der GPU in GB
 * @param price Preis der GPU
 * @author Fabio
 */
public class GPU extends Component {

    private final int vramGB;

    public GPU(int id, String name, double price, Manufacturer manufacturer, int vramGB) {
        super(id, name, price, manufacturer);
        this.vramGB = vramGB;
    }

    public int getVramGB() {
        return vramGB;
    }
}