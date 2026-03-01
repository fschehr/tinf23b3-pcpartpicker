package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;
import de.ase.pcpartpicker.domain.HelperClasses.Type;

/**
 * Klasse für die CPU-Komponente.
 * @param id Eindeutige ID der CPU
 * @param name Name der CPU
 * @param manufacturer Hersteller der CPU
 * @param socket Sockeltyp der CPU (z.B. AM4, LGA1200)
 * @param speedGhz Taktfrequenz der CPU in GHz
 * @param price Preis der CPU
 * @author Fabio
 */
public class Cpu extends Component {

    private final Socket socket;
    private final double speedGhz;

    public Cpu(int id, Type type, String name, double price, Manufacturer manufacturer, Socket socket, double speedGhz) {
        super(id, type, name, price, manufacturer);
        this.socket = socket;
        this.speedGhz = speedGhz;
    }

    public Socket getSocket() {
        return socket;
    }

    public double getSpeedGhz() {
        return speedGhz;
    }
}