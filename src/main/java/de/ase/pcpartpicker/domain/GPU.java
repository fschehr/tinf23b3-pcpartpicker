package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

/**
 * Klasse für die GPU-Komponente.
 * @param id Eindeutige ID der GPU
 * @param name Name der GPU
 * @param price Preis der GPU
 * @param manufacturer Hersteller der GPU
 * @param vramGB VRAM-Kapazität der GPU in GB
 * @param powerConsumptionW Maximaler Stromverbrauch der Komponente in Watt
 * @author Fabio
 */
public class GPU extends Component {

    private final int vramGB;

    public GPU(int id, String name, double price, Manufacturer manufacturer, int vramGB, int powerConsumptionW) {
        super(id, name, price, manufacturer, powerConsumptionW);
        this.vramGB = vramGB;
    }

    public int getVramGB() {
        return vramGB;
    }

    @Override
    public String toString() {
        return String.format(
            "%s | %s | %.2f EUR | %d GB VRAM | %d W",
            getName(),
            getManufacturer() != null ? getManufacturer().getName() : "-",
            getPrice(),
            vramGB,
            getPowerConsumptionW()
        );
    }
}