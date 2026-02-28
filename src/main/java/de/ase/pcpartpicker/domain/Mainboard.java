package de.ase.pcpartpicker.domain;

/**
 * Klasse für die Mainboard-Komponente.
 * @param id Eindeutige ID des Mainboards
 * @param name Name des Mainboards
 * @param socket Sockeltyp des Mainboards (z.B. AM4, LGA1200)
 * @param formFactor Formfaktor des Mainboards (z.B. ATX, Micro-ATX)
 * @param price Preis des Mainboards
 * @author Fabio
 */
public class Mainboard extends Component {

    private final String socket;
    private final String formFactor;

    public Mainboard(int id, String name, String socket, String formFactor, double price) {
        super(id, name, price);
        this.socket = socket;
        this.formFactor = formFactor;
    }

    public String getSocket() {
        return socket;
    }

    public String getFormFactor() {
        return formFactor;
    }
}