package de.ase.pcpartpicker.parts;
import de.ase.pcpartpicker.parts.additional.Form;
import de.ase.pcpartpicker.parts.additional.Type;

// Gehäuse eines Computers
public class Case extends Part {

    private Form form; // Formfaktor, z.B. ATX, Micro-ATX, Mini-ITX
    private boolean hasWindow; // Ob das Gehäuse ein Fenster hat oder nicht
    private int fanSlots; // Anzahl der Slots für Gehäuselüfter (im ersten Schritt nur 120mm Lüfter)
    
    public Case(double price, String manufacturer, String model, String form, boolean hasWindow, int fanSlots) {
        super(price, manufacturer, model);
        this.type = new Type("Case");
        this.form = new Form(form);
        this.hasWindow = hasWindow;
        this.fanSlots = fanSlots;
    }

    public String getForm() {
        return form.getForm();
    }

    public boolean hasWindow() {
        return hasWindow;
    }

    public int getfanSlots() {
        return fanSlots;
    }
}
