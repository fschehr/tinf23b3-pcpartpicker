package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.MotherboardFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;

/**
 * Klasse für die Mainboard-Komponente.
 * @param id Eindeutige ID des Mainboards
 * @param name Name des Mainboards
 * @param manufacturer Hersteller des Mainboards
 * @param socket Sockeltyp des Mainboards (z.B. AM4, LGA1200)
 * @param formFactor Formfaktor des Mainboards (z.B. ATX, Micro-ATX)
 * @param price Preis des Mainboards
 * @author Fabio
 */
public class Mainboard extends Component {

    private final Socket socket;
    private final MotherboardFormFactor formFactor;

    public Mainboard(
        int id,
        String name,
        double price,
        Manufacturer manufacturer,
        Socket socket,
        MotherboardFormFactor formFactor
    ) {
        super(id, name, price, manufacturer);
        this.socket = socket;
        this.formFactor = formFactor;
    }

    public Socket getSocket() {
        return socket;
    }

    public MotherboardFormFactor getFormFactor() {
        return formFactor;
    }
}