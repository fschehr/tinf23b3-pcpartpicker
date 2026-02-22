package de.ase.pcpartpicker.parts;

public class Case extends Part {

    private String form;
    
    public Case(float price, String manufacturer, String model, String form) {
        super(price, manufacturer, model);
        this.form = form;
    }

    public String getForm() {
        return form;
    }

}
