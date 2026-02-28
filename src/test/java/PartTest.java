import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.ase.pcpartpicker.parts.Part;
import de.ase.pcpartpicker.parts.PowerSupply;
import de.ase.pcpartpicker.parts.Case;

public class PartTest {
    
    // Vorbereiten der Tests
    @BeforeAll
    public static void setup() {
        
    }

    @Test
    public void testCorrectCase() {
        Part korrekt = new Case(499.99, "Corsair", "Fabio Schehr", "ATX", "ATX", true, 9);
        System.out.println(korrekt);

        assert(korrekt.getType().equals("Case"));
        System.out.println(korrekt.getType() + " richtig erkannt");

        assert(korrekt.getPrice() == 499.99);
        System.out.println(korrekt.getPrice() + " richtig erkannt");

        assert(korrekt.getManufacturer().equals("Corsair"));
        System.out.println(korrekt.getManufacturer() + " richtig erkannt");

        assert(korrekt.getModel().equals("Fabio Schehr"));
        System.out.println(korrekt.getModel() + " richtig erkannt");
        

        Case korrektCase = (Case) korrekt;
        assert(korrektCase.getFormMB().equals("ATX"));
        System.out.println(korrektCase.getFormMB() + " richtig erkannt");

        assert(korrektCase.getFormPSU().equals("ATX"));
        System.out.println(korrektCase.getFormPSU() + " richtig erkannt");

        assert(korrektCase.hasWindow() == true);
        System.out.println(korrektCase.hasWindow() + " richtig erkannt");

        assert(korrektCase.getfanSlots() == 9);
        System.out.println(korrektCase.getfanSlots() + " richtig erkannt");

        assert(korrektCase.possibleMBs().length == 3);
        System.out.println("Anzahl möglicher Mainboardformate: " + korrektCase.possibleMBs().length + "\n");
    }

    @Test
    public void testCorrectPSU() {
        Part korrekt = new PowerSupply(199.99, "Asus Rog", "Finn Hannon", "SFX", 1000, "80 Plus Gold", true, true);
        System.out.println(korrekt);

        assert(korrekt.getType().equals("PSU"));
        System.out.println(korrekt.getType() + " richtig erkannt");

        assert(korrekt.getPrice() == 199.99);
        System.out.println(korrekt.getPrice() + " richtig erkannt");

        assert(korrekt.getManufacturer().equals("Asus Rog"));
        System.out.println(korrekt.getManufacturer() + " richtig erkannt");

        assert(korrekt.getModel().equals("Finn Hannon"));
        System.out.println(korrekt.getModel() + " richtig erkannt");

        PowerSupply korrektPSU = (PowerSupply) korrekt;
        assert(korrektPSU.getForm().equals("SFX"));
        System.out.println(korrektPSU.getForm() + " richtig erkannt");

        assert(korrektPSU.getWattage() == 1000);
        System.out.println(korrektPSU.getWattage() + " richtig erkannt");

        assert(korrektPSU.getEfficiencyRating().equals("80 Plus Gold"));
        System.out.println(korrektPSU.getEfficiencyRating() + " richtig erkannt");
        
        assert(korrektPSU.isModular() == true);
        System.out.println(korrektPSU.isModular() + " richtig erkannt\n");
    }



    @Test
    public void testFalseCase() {
        assertThrows(IllegalArgumentException.class, () -> new Case(99999.99, "Henri Tech", "a", "Wasser", "Benjamin", false, 27));
        System.out.println("Ungültige Werte für Case erkannt\n");
    }

    @Test
    public void testFalsePSU() {
        assertThrows(IllegalArgumentException.class, () -> new PowerSupply(99999.99, "Henri Tech", "a", "Wasser", 10000, "80 Plus Diamond", false, true));
        System.out.println("Ungültige Werte für PSU erkannt\n");
    }

}
