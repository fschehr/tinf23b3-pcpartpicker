package de.ase.pcpartpicker.domain;

/**
 * Klasse für die CPU-Komponente.
 * @param id Eindeutige ID der CPU
 * @param name Name der CPU
 * @param socket Sockeltyp der CPU (z.B. AM4, LGA1200)
 * @param speedGhz Taktfrequenz der CPU in GHz
 * @param price Preis der CPU
 * @author Fabio
 */
public class Cpu extends Component {

    private final String socket;
    private final double speedGhz;

    public Cpu(int id, String name, String socket, double speedGhz, double price) {
        super(id, name, price);
        this.socket = socket;
        this.speedGhz = speedGhz;
    }

    public String getSocket() {
        return socket;
    }

    public double getSpeedGhz() {
        return speedGhz;
    }
}