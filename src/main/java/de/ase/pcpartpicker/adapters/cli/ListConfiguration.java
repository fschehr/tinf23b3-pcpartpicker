package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.repositories.CaseRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
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
import de.ase.pcpartpicker.part_assembly.Computer;

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
            new String[]{"#", "Name", "Hersteller", "Sockel", "Kerne", "Takt (GHz)", "Boost-Takt (GHz)", "TDP (W)", "Preis"},
            new CpuRepository(cf),
            cpu -> new String[]{
                String.valueOf(cpu.getId()),
                cpu.getName(),
                cpu.getManufacturer().getName(),
                cpu.getSocket().getName(),
                String.valueOf(cpu.getCoreCount()),
                String.format("%.1f",cpu.getSpeedGHz()),
                cpu.getBoostClockGHz() != null ? String.format("%.1f", cpu.getBoostClockGHz()) : "N/A",
                String.valueOf(cpu.getPowerConsumptionW()),
                String.format("%.2f", cpu.getPrice()) + " €"
            }
        );
    }

    public rListConfiguration<GPU> gpu() {
        return new rListConfiguration<>(
            "GPUs",
            new String[]{"#", "Name", "Hersteller", "VRAM (GB)", "Base-Clock (Mhz)", "Boost-Clock (Mhz)", "TDP (W)", "Preis"},
            new GpuRepository(cf),
            gpu -> new String[]{
                String.valueOf(gpu.getId()),
                gpu.getName(),
                gpu.getManufacturer().getName(),
                String.valueOf(gpu.getVramGB()),
                String.format("%.0f", gpu.getCoreClockMHz()),
                gpu.getBoostClockMHz() != null ? String.format("%.0f", gpu.getBoostClockMHz()) : "N/A",
                String.valueOf(gpu.getPowerConsumptionW()),
                String.format("%.2f", gpu.getPrice()) + " €"
            }
        );
    }

    public rListConfiguration<RAM> ram() {
        return new rListConfiguration<>(
            "RAM",
            new String[]{"#", "Name", "Hersteller", "Kapzität (GB)", "Takt (MHz)", "Preis"},
            new RamRepository(cf),
            ram -> new String[]{
                String.valueOf(ram.getId()),
                ram.getName(),
                ram.getManufacturer().getName(),
                String.valueOf(ram.getCapacityGB()),
                String.valueOf(ram.getSpeedMHz()),
                String.format("%.2f", ram.getPrice()) + " €"
            }
        );
    }

    public rListConfiguration<Mainboard> mainboard() {
        return new rListConfiguration<>(
            "Mainboards",
            new String[]{"#", "Name", "Hersteller", "Sockel", "RAM Slots", "Formfaktor", "Preis"},
            new MainboardRepository(cf),
            mb -> new String[]{
                String.valueOf(mb.getId()),
                mb.getName(),
                mb.getManufacturer().getName(),
                mb.getSocket().getName(),
                String.valueOf(mb.getRamSlots()),
                mb.getFormFactor().getName(),
                String.format("%.2f", mb.getPrice()) + " €"
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
                String.format("%.2f", psu.getPrice()) + " €"
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
                c.getMotherboardFormFactor().getName(),
                String.format("%.2f", c.getPrice()) + " €"
            }
        );
    }

    public rListConfiguration<SSD> ssd() {
        return new rListConfiguration<>(
            "SSDs",
            new String[]{"#", "Name", "Hersteller", "Kapazität (GB)", "Preis"},
            new SsdRepository(cf),
            ssd -> new String[]{
                String.valueOf(ssd.getId()),
                ssd.getName(),
                ssd.getManufacturer().getName(),
                String.valueOf(ssd.getCapacityGB()),
                String.format("%.2f", ssd.getPrice()) + " €"
            }
        );
    }

    public rListConfiguration<M2SSD> m2ssd() {
        return new rListConfiguration<>(
            "M.2 SSDs",
            new String[]{"#", "Name", "Hersteller", "Kapaität (GB)", "Preis"},
            new M2SsdRepository(cf),
            m2 -> new String[]{
                String.valueOf(m2.getId()),
                m2.getName(),
                m2.getManufacturer().getName(),
                String.valueOf(m2.getCapacityGB()),
                String.format("%.2f", m2.getPrice()) + " €"
            }
        );
    }

    public rListConfiguration<HDD> hdd() {
        return new rListConfiguration<>(
            "HDDs",
            new String[]{"#", "Name", "Hersteller", "Kapatität", "Preis"},
            new HddRepository(cf),
            hdd -> new String[]{
                String.valueOf(hdd.getId()),
                hdd.getName(),
                hdd.getManufacturer().getName(),
                String.valueOf(hdd.getCapacityGB()),
                String.format("%.2f", hdd.getPrice()) + " €"
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

    public rListConfiguration<Computer> computer() {
        return new rListConfiguration<>(
            "Computer",
            new String[]{"CPU", "GPU", "Mainboard", "RAM", "Netzteil", "Gehäuse", "HDD", "SSD", "M2SSD", "Gesamtpreis"},
            new ComputerRepository(cf),
            user -> new String[] {
                
            }
        );
    }
}