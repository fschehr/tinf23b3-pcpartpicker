package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.repositories.CaseRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.CpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.GpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.HddRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.M2SsdRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.MainboardRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.PsuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.RamRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.SsdRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.Case;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.HDD;
import de.ase.pcpartpicker.domain.HelperClasses.User;
import de.ase.pcpartpicker.domain.M2SSD;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.SSD;

/**
 * Klasse, in der die Konfigurationen der einzelnen Komponenten gespeichert sind
 * Wenn eine Komponente eine Eigenschaft fehlt kann sie hier ergänzt werden 
 * @author Henri
 */

public class ListConfiguration {
    private final ConnectionFactory cf;

    public ListConfiguration(ConnectionFactory cf) {
        this.cf = cf;
    }

    public rListConfiguration<CPU> cpu() {
        return new rListConfiguration<>(
            "CPUs",
            new String[]{"#", "Name", "Hersteller", "Sockel", "Takt (GHz)", "Preis"},
            new CpuRepository(cf),
            cpu -> new String[]{
                String.valueOf(cpu.getId()),
                cpu.getName(),
                cpu.getManufacturer().getName(),
                cpu.getSocket().getName(),
                String.format("%.1f",cpu.getspeedGHz()),
                cpu.getPrice() + " €"
            }
        );
    }

    public rListConfiguration<GPU> gpu() {
        return new rListConfiguration<>(
            "GPUs",
            new String[]{"#", "Name", "Hersteller", "Preis"},
            new GpuRepository(cf),
            gpu -> new String[]{
                String.valueOf(gpu.getId()),
                gpu.getName(),
                gpu.getManufacturer().getName(),
                gpu.getPrice() + " €"
            }
        );
    }

    public rListConfiguration<RAM> ram() {
        return new rListConfiguration<>(
            "RAM",
            new String[]{"#", "Name", "Hersteller", "Preis"},
            new RamRepository(cf),
            ram -> new String[]{
                String.valueOf(ram.getId()),
                ram.getName(),
                ram.getManufacturer().getName(),
                ram.getPrice() + " €"
            }
        );
    }

    public rListConfiguration<Mainboard> mainboard() {
        return new rListConfiguration<>(
            "Mainboards",
            new String[]{"#", "Name", "Hersteller", "Sockel", "Formfaktor", "Preis"},
            new MainboardRepository(cf),
            mb -> new String[]{
                String.valueOf(mb.getId()),
                mb.getName(),
                mb.getManufacturer().getName(),
                mb.getSocket().getName(),
                mb.getFormFactor().getName(),
                mb.getPrice() + " €"
            }
        );
    }

    public rListConfiguration<PSU> psu() {
        return new rListConfiguration<>(
            "Netzteile",
            new String[]{"#", "Name", "Hersteller", "Watt", "Formfaktor", "Preis"},
            new PsuRepository(cf),
            psu -> new String[]{
                String.valueOf(psu.getId()),
                psu.getName(),
                psu.getManufacturer().getName(),
                psu.getWattage() + " W",
                psu.getFormFactor().getName(),
                psu.getPrice() + " €"
            }
        );
    }

    public rListConfiguration<Case> pcCase() {
        return new rListConfiguration<>(
            "Gehäuse",
            new String[]{"#", "Name", "Hersteller", "Formfaktor", "Preis"},
            new CaseRepository(cf),
            c -> new String[]{
                String.valueOf(c.getId()),
                c.getName(),
                c.getManufacturer().getName(),
                c.getPrice() + " €"
            }
        );
    }

    public rListConfiguration<SSD> ssd() {
        return new rListConfiguration<>(
            "SSDs",
            new String[]{"#", "Name", "Hersteller", "Preis"},
            new SsdRepository(cf),
            ssd -> new String[]{
                String.valueOf(ssd.getId()),
                ssd.getName(),
                ssd.getManufacturer().getName(),
                ssd.getPrice() + " €"
            }
        );
    }

    public rListConfiguration<M2SSD> m2ssd() {
        return new rListConfiguration<>(
            "M.2 SSDs",
            new String[]{"#", "Name", "Hersteller", "Preis"},
            new M2SsdRepository(cf),
            m2 -> new String[]{
                String.valueOf(m2.getId()),
                m2.getName(),
                m2.getManufacturer().getName(),
                m2.getPrice() + " €"
            }
        );
    }

    public rListConfiguration<HDD> hdd() {
        return new rListConfiguration<>(
            "HDDs",
            new String[]{"#", "Name", "Hersteller", "Preis"},
            new HddRepository(cf),
            hdd -> new String[]{
                String.valueOf(hdd.getId()),
                hdd.getName(),
                hdd.getManufacturer().getName(),
                hdd.getPrice() + " €"
            }
        );
    }

    public rListConfiguration<User> user() {
        return new rListConfiguration<>(
            "Nutzer",
            new String[]{"ID", "Name"},
            new UserRepository(cf),
            user -> new String[] {
                String.valueOf(user.getId()),
                user.getName()
            }
        );

    }
}