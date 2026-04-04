package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;

/**
 * Klasse für die CPU-Komponente.
 * @param id Eindeutige ID der CPU
 * @param name Name der CPU
 * @param price Preis der CPU
 * @param manufacturer Hersteller der CPU
 * @param socket Sockeltyp der CPU (z.B. AM4, LGA1200)
 * @param speedGHz Taktfrequenz der CPU in GHz
 * @param boostClockGHz optionale Boost-Frequenz der CPU in GHz
 * @param hasIntegratedGraphics Gibt an, ob die CPU integrierte Grafik hat
 * @param powerConsumptionW Maximaler Stromverbrauch der Komponente in Watt
 * @author Fabio
 */
public class CPU extends Component {

    private final Socket socket;
    private final double speedGHz;
    private final Double boostClockGHz;
    private final boolean hasIntegratedGraphics;

    public CPU(int id, String name, double price, Manufacturer manufacturer, Socket socket, double speedGHz, boolean hasIntegratedGraphics, int powerConsumptionW) {
        this(id, name, price, manufacturer, socket, speedGHz, null, hasIntegratedGraphics, powerConsumptionW);
    }

    public CPU(int id, String name, double price, Manufacturer manufacturer, Socket socket, double speedGHz, Double boostClockGHz, boolean hasIntegratedGraphics, int powerConsumptionW) {
        super(id, name, price, manufacturer, powerConsumptionW);
        this.socket = socket;
        this.speedGHz = speedGHz;
        this.boostClockGHz = boostClockGHz;
        this.hasIntegratedGraphics = hasIntegratedGraphics;
    }

    public Socket getSocket() {
        return socket;
    }

    public double getSpeedGHz() {
        return speedGHz;
    }

    public Double getBoostClockGHz() {
        return boostClockGHz;
    }

    public boolean hasIntegratedGraphics() {
        return hasIntegratedGraphics;
    }

    @Override
    public String toString() {
        String socketName = socket != null ? socket.getName() : "-";
        String boostClockText = boostClockGHz != null ? String.format(" | %.2f GHz", boostClockGHz) : "";
        return String.format(
            "%s | %s | %.2f EUR | %s | %.2f GHz%s | %s | %d W",
            getName(),
            getManufacturer() != null ? getManufacturer().getName() : "-",
            getPrice(),
            socketName,
            speedGHz,
            boostClockText,
            hasIntegratedGraphics ? "ja" : "nein",
            getPowerConsumptionW()
        );
    }
    
}