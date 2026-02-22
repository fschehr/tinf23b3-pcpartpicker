package de.ase.pcpartpicker.adapters.cli;


import de.ase.pcpartpicker.domain.models.Component;
import de.ase.pcpartpicker.domain.models.PCConfiguration;

public class ConfigurationMenu {
    private final PCConfiguration config;

    public ConfigurationMenu(PCConfiguration config) {
        this.config = config; 
    }


    public void showCurrentConfiguration() {

        if(config.isEmpty()) {
            System.out.println("Der Warenkorb ist noch leer.");
        }
        else {
            TableGenerator table = new TableGenerator("Typ", "Modell", "Preis");
            for(Component comp: config.getComponents()) {
                table.addRow(
                    comp.getClass().getSimpleName(),
                    comp.getName(),
                    String.format("%.2f", comp.getPriceInEuro())
                );
            }

            table.printTable();

            double totalPrice = config.getTotalPrice() / 100.0;
            System.out.printf("GESAMTSUMME: %.2f€%n", totalPrice);
        }
        System.out.println("\n(Drücken Sie Enter, um zurückzukehren"); 
    }
}
