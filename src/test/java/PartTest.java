import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.ase.pcpartpicker.parts.Part;
import de.ase.pcpartpicker.parts.Case;

public class PartTest {
    
    // Vorbereiten der Tests
    @BeforeAll
    public static void setup() {
        
    }

    @Test
    public void testCaseCreation() {
        Part korrekt = new Case(499.99, "Corsair", "Fabio Schehr", "ATX", true, 9);
        System.out.println(korrekt);
        assert(korrekt.getType().equals("Case"));
        assert(korrekt.getPrice() == 499.99);
        assert(korrekt.getManufacturer().equals("Corsair"));
        assert(korrekt.getModel().equals("Fabio Schehr"));
        assert(((Case) korrekt).getForm().equals("ATX"));
    }

    @Test
    public void testFalseCase() {
        assertThrows(IllegalArgumentException.class, () -> new Case(99999.99, "Henri Tech", "a", "Wasser", false, 27));
    }



}
