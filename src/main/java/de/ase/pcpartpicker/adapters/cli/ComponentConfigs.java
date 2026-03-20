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

public class ComponentConfigs {
    private final ConnectionFactory cf;

    public ComponentConfigs(ConnectionFactory cf) {
        this.cf = cf;
    }

    public rComponentConfig<CPU> cpu() {
        return new rComponentConfig<>(
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

    public rComponentConfig<GPU> gpu() {
        return new rComponentConfig<>(
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

    public rComponentConfig<RAM> ram() {
        return new rComponentConfig<>(
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

    public rComponentConfig<Mainboard> mainboard() {
        return new rComponentConfig<>(
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

    public rComponentConfig<PSU> psu() {
        return new rComponentConfig<>(
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

    public rComponentConfig<Case> pcCase() {
        return new rComponentConfig<>(
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

    public rComponentConfig<SSD> ssd() {
        return new rComponentConfig<>(
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

    public rComponentConfig<M2SSD> m2ssd() {
        return new rComponentConfig<>(
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

    public rComponentConfig<HDD> hdd() {
        return new rComponentConfig<>(
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

    public rComponentConfig<User> user() {
        return new rComponentConfig<>(
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