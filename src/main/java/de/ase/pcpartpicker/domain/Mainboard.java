package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.MotherboardFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;

/**
 * Klasse für die Mainboard-Komponente.
 * @param id Eindeutige ID des Mainboards
 * @param name Name des Mainboards
 * @param price Preis des Mainboards
 * @param manufacturer Hersteller des Mainboards
 * @param socket Sockeltyp des Mainboards (z.B. AM4, LGA1200)
 * @param formFactor Formfaktor des Mainboards (z.B. ATX, Micro-ATX)
 * @author Fabio
 */
public class Mainboard extends Component {

    private final Socket socket;
    private final MotherboardFormFactor formFactor;
    private final int ramSlots;
    private final int pcieSlots;
    private final int sataSlots;
    private final int m2Slots;

    public Mainboard(
        int id,
        String name,
        double price,
        Manufacturer manufacturer,
        Socket socket,
        MotherboardFormFactor formFactor,
        int ramSlots,
        int pcieSlots,
        int sataSlots,
        int m2Slots
    ) {
        super(id, name, price, manufacturer, 15); // Ein Mainboard hat einen angenommenen Stromverbrauch von 15 Watt
        this.socket = socket;
        this.formFactor = formFactor;
        this.ramSlots = ramSlots;
        this.pcieSlots = pcieSlots;
        this.sataSlots = sataSlots;
        this.m2Slots = m2Slots;
    }

    public Socket getSocket() {
        return socket;
    }

    public MotherboardFormFactor getFormFactor() {
        return formFactor;
    }

    public int getRamSlots() {
        return ramSlots;
    }

    public int getPcieSlots() {
        return pcieSlots;
    }

    public int getSataSlots() {
        return sataSlots;
    }

    public int getM2Slots() {
        return m2Slots;
    }
}