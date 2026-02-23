package de.ase.pcpartpicker.parts.additional;

// Klasse die Formfaktoren validiert
public class Form {
    
    private String form;
    private String[] formfactors = { "E-ATX", "XL-ATX", "ATX", "Micro-ATX", "Mini-ITX"};

    public Form(String form) {
        setForm(form);
    }

    private void setForm(String form) {
        for(String f: formfactors) {
            if(f.equals(form)) {
                this.form = form;
                return;
            }
        }
        
        throw new IllegalArgumentException(form + " is not a valid form factor. Valid form factors are: " + String.join("\n", formfactors));
    }

    public String getForm() {
        return form;
    }
}
