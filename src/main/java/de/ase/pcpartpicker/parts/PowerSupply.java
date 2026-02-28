package de.ase.pcpartpicker.parts;
import de.ase.pcpartpicker.parts.additional.Type;
import de.ase.pcpartpicker.parts.additional.Form;

// Netzteil eines Computers
public class PowerSupply extends Part {

    private Form form; // Formfaktor, z.B. ATX, SFX, FlexATX, SFX-L
    private int wattage; // Leistung in Watt
    private String efficiencyRating; // Effizienzklasse, z.B. 80 Plus Bronze, Silver, Gold, Platinum, Titanium
    private boolean modular; // Ob das Netzteil modular ist oder nicht. Um zu vereinfachen: Teilmodular wird als modular betrachtet
    private boolean atx31Support; // Ob das Netzteil ATX 3.1 unterstützt oder nicht (false -> ATX 3.0, 2.4 wird ignoriert)

    public PowerSupply(double price, String manufacturer, String model, String form, int wattage, String efficiencyRating, boolean modular, boolean atx31Support) {
        super(price, manufacturer, model);
        this.type = new Type("PSU");
        this.form = new Form(form, false);
        this.wattage = wattage;
        this.efficiencyRating = efficiencyRating;
        this.modular = modular;
        this.atx31Support = atx31Support;
    }

    public String getForm() {
        return form.getForm();
    }

    public int getWattage() {
        return wattage;
    }

    public String getEfficiencyRating() {
        return efficiencyRating;
    }
    
    public boolean isModular() {
        return modular;
    }

    public boolean supportsATX31() {
        return atx31Support;
    }
}
