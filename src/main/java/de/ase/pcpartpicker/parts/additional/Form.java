package de.ase.pcpartpicker.parts.additional;
import de.ase.pcpartpicker.parts.additional.Type;;

// Klasse die Formfaktoren validiert
public class Form {
    
    private String form;
    private boolean motherboard; // true -> Formfaktor für Mainboard, false -> Formfaktor für Netzteil
    private String[] formfactors;
    private String[] formfactorsMB = { "E-ATX", "XL-ATX", "ATX", "Micro-ATX", "Mini-ITX"};
    private String[] formfactorsPSU = { "ATX", "SFX-L", "SFX", "FlexATX" };
    
    public Form(String form, boolean motherboard) {
        setForm(form, motherboard);
    }

    // Formfaktor wird für Motherboard oder Netzteil validiert, je nachdem was übergeben wird
    private void setForm(String form, boolean motherboard) {
        this.motherboard = motherboard;
        if(motherboard) {
            formfactors = formfactorsMB;
        } else {
            formfactors = formfactorsPSU;
        }
        for(String f: formfactors) {
            if(f.equals(form)) {
                this.form = form;
                return;
            }
        }


        throw new IllegalArgumentException(form + " is not a valid form factor for a " + (motherboard ? "Motherboard" : "PSU") + ". Valid form factors are: " + String.join("\n", formfactors));
    }

    // Gibt alle möglichen Mainboardformate zurück, die mit dem Formfaktor des Gehäuses kompatibel sind
    public String[] possibleMotherboards() {
        String[] possibleMBs;
        if(!motherboard)
            throw new IllegalStateException("This method is only available for Motherboard form factors.");

        for(int i = 0; i < formfactors.length; i++) {
            if(formfactors[i].equals(form)) {
                possibleMBs = new String[formfactors.length - i];
                System.arraycopy(formfactors, i, possibleMBs, 0, formfactors.length - i);
                return possibleMBs;
            }
        }
        throw new IllegalStateException("Form factor " + form + " not found.");
    } 

    public String getForm() {
        return form;
    }
}
