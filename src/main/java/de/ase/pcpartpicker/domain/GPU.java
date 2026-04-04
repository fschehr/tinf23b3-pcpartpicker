package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

/**
 * Klasse für die GPU-Komponente.
 * @param id Eindeutige ID der GPU
 * @param name Name der GPU
 * @param price Preis der GPU
 * @param manufacturer Hersteller der GPU
 * @param coreClockMHz Core-Takt in MHz
 * @param boostClockMHz optionale Boost-Frequenz in MHz
 * @param vramGB VRAM-Kapazität der GPU in GB
 * @param powerConsumptionW Maximaler Stromverbrauch der Komponente in Watt
 * @author Fabio
 */
public class GPU extends Component {

    private final double coreClockMHz;
    private final Double boostClockMHz;
    private final int vramGB;

    public GPU(int id, String name, double price, Manufacturer manufacturer, int vramGB, int powerConsumptionW) {
        this(id, name, price, manufacturer, 0d, null, vramGB, powerConsumptionW);
    }

    public GPU(int id, String name, double price, Manufacturer manufacturer, double coreClockMHz, Double boostClockMHz, int vramGB, int powerConsumptionW) {
        super(id, name, price, manufacturer, powerConsumptionW);
        this.coreClockMHz = coreClockMHz;
        this.boostClockMHz = boostClockMHz;
        this.vramGB = vramGB;
    }

    public double getCoreClockMHz() {
        return coreClockMHz;
    }

    public Double getBoostClockMHz() {
        return boostClockMHz;
    }

    public int getVramGB() {
        return vramGB;
    }

    @Override
    public String toString() {
        String boostClockText = boostClockMHz != null ? String.format(" | %.0f MHz", boostClockMHz) : "";
        return String.format(
            "%s | %s | %.2f EUR | %.0f MHz%s | %d GB VRAM | %d W",
            getName(),
            getManufacturer() != null ? getManufacturer().getName() : "-",
            getPrice(),
            coreClockMHz,
            boostClockText,
            vramGB,
            getPowerConsumptionW()
        );
    }
}