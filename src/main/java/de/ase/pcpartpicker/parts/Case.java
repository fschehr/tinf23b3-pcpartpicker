package de.ase.pcpartpicker.parts;
import de.ase.pcpartpicker.parts.additional.Form;
import de.ase.pcpartpicker.parts.additional.Type;

// Gehäuse eines Computers
public class Case extends Part {

    private Form formMB; // Formfaktor Mainboard, z.B. ATX, Micro-ATX, Mini-ITX
    private Form formPSU; // Formfaktor Netzteil, z.B. ATX, SFX, FlexATX, SFX-L
    private boolean hasWindow; // Ob das Gehäuse ein Fenster hat oder nicht
    private int fanSlots; // Anzahl der Slots für Gehäuselüfter (im ersten Schritt nur 120mm Lüfter)
    
    public Case(double price, String manufacturer, String model, String formMB, String formPSU, boolean hasWindow, int fanSlots) {
        super(price, manufacturer, model);
        this.type = new Type("Case");
        this.formMB = new Form(formMB, true);
        this.formPSU = new Form(formPSU, false);
        this.hasWindow = hasWindow;
        this.fanSlots = fanSlots;
    }

    public String getFormMB() {
        return formMB.getForm();
    }

    public String[] possibleMBs() {
        return formMB.possibleMotherboards();
    }

    public String getFormPSU() {
        return formPSU.getForm();
    }

    public boolean hasWindow() {
        return hasWindow;
    }

    public int getfanSlots() {
        return fanSlots;
    }
}
