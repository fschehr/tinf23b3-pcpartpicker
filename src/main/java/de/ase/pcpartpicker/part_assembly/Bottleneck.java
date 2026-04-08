package de.ase.pcpartpicker.part_assembly;

import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.repositories.CpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.GpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.M2SsdRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.RamRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.SsdRepository;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.Component;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.HDD;
import de.ase.pcpartpicker.domain.M2SSD;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.SSD;
import de.ase.pcpartpicker.domain.Storage;

/**
 * Diese Klasse enthält statische Methoden zur Berechnung von Bottlenecks in einem Computer basierend auf seinen Komponenten.
 * @author Tuluhan
 */
public final class Bottleneck {
    private static boolean bottleneck; // Flag, um zu verfolgen, ob bereits ein Bottleneck gefunden wurde
    private static boolean oneComponent; // Flag, um zu verfolgen, ob es eine Komponente zu kacke ist oder eine Komponente zu stark ist
    
    // Privater Konstruktor verhindert, dass jemand "new Bottleneck()" aufruft
    private Bottleneck() {}

    public static BottleneckResult calculateBottleneck(Computer computer) {
        bottleneck = false;
        oneComponent = false;
        Component bottleneckComponent = calculate(computer);
        return new BottleneckResult(bottleneck, bottleneckComponent, oneComponent, recommendedUpgrade(bottleneckComponent));
    }

    /**
     * Berechnet den Bottleneck eines Computers basierend auf der CPU und GPU.
     * Ein Bottleneck tritt auf, wenn eine Komponente die Leistung einer anderen Komponente begrenzt.
     * @param computer der Computer, dessen Bottleneck berechnet werden soll
     * @return die Komponente, die den Bottleneck darstellt, oder null, wenn kein Bottleneck gefunden wurde
     */
    private static Component calculate(Computer computer) {

        // Ein Modell was Punkte für Leistungen vergibt und dann vergleicht ob eines besonders heraussticht.
        double cpuScore = (computer.getCPU().getBoostClockGHz() != null ? computer.getCPU().getBoostClockGHz() : computer.getCPU().getSpeedGHz()) * computer.getCPU().getCoreCount();
        double gpuScore;
        if (computer.getGPU() != null) {
            gpuScore = (computer.getGPU().getBoostClockMHz() != null ? computer.getGPU().getBoostClockMHz() : computer.getGPU().getCoreClockMHz()) / 1000.0 * ((Math.log(computer.getGPU().getVramGB())/Math.log(2) * 4)); // VRAM trägt auch zur Leistung bei, aber logarithmisch, allen voran weil 32GB die Rechnung sprengt
        } else {
            gpuScore = 0; // Kein GPU, also kein Score
        }
        double ramScore = Math.max(4.5, computer.getRAM().getSpeedMHz() / 1000) * (Math.log(computer.getRAM().getCapacityGB() * computer.getRamModule())/Math.log(2) + 1); // RAM-Geschwindigkeit und -Größe tragen beide zur Gesamtleistung bei. 4.5 als Mindestwert weil DDR4 RAM sonst die Berechnung ordentlich runter zieht
        Storage strongestStorage = strongestStorage(computer.getStorageDevices());
        double storageScore;

        if(strongestStorage instanceof M2SSD) {
            storageScore = 45; // M2-SSDs bieten die beste Leistung
        } else if(strongestStorage instanceof SSD) {
            storageScore = 35; // SSDs sind schneller als HDDs, aber langsamer als M2-SSDs
        } else if(strongestStorage instanceof HDD) {
            storageScore = 15; // HDDs bieten die geringste Leistung (nicht mehr zeitgemäß, wird deswegen mit 10 bestraft wenn stärkste Festplatte)
        } else {
            storageScore = 0; // Kein Speicher oder unbekannter Typ, mittlerer Score
        }

        // Näherungsweise berechnen des Bottlenecks mithilfe des arithmetischen Mittels der Scores. Wenn eine Komponente deutlich schlechter abschneidet als die anderen, könnte sie der Bottleneck sein.
        double arithmeticMean = (cpuScore + gpuScore + ramScore + storageScore) / (gpuScore > 0 ? 4 : 3); // Wenn keine GPU vorhanden ist, nur 3 Komponenten berücksichtigen
        if(gpuScore == 0) gpuScore = arithmeticMean; // Wenn keine GPU vorhanden ist, setzen wir den GPU-Score auf den Durchschnitt, damit er die Berechnung nicht verzerrt

        if(Math.abs(arithmeticMean - cpuScore) > 15 && Math.abs(arithmeticMean - gpuScore) > 15 && Math.abs(arithmeticMean - ramScore) > 15 && Math.abs(arithmeticMean - storageScore) > 15) {
            // Alle Komponenten weichen stark vom Durchschnitt ab also ist das System absolut unbalanced. Bottleneck ja aber keine klare Komponente
            bottleneck = true;
            return null;
        }

        if(Math.abs(arithmeticMean - cpuScore) > 15) {
            bottleneck = true;
            if(cpuScore < arithmeticMean) {
                oneComponent = true;
                return computer.getCPU();
            } else {
                return computer.getCPU();
            }
        } else if(Math.abs(arithmeticMean - gpuScore) > 15) {
            bottleneck = true;
            if(gpuScore < arithmeticMean) {
                oneComponent = true;
                return computer.getGPU();
            } else {
                return computer.getGPU();
            }
        } else if(Math.abs(arithmeticMean - ramScore) > 15) {
            bottleneck = true;
            if(ramScore < arithmeticMean) {
                oneComponent = true;
                return computer.getRAM();
            } else {
                return computer.getRAM();
            }
        } else if(Math.abs(arithmeticMean - storageScore) > 15) {
            bottleneck = true;
            if(storageScore < arithmeticMean) {
                oneComponent = true;
                return strongestStorage;
            } else {
                return strongestStorage;
            }
        } else {
            return null;
        }
    }

    /**
     * Sucht die stärkste Festplatte raus für die Bottleneck Berechnung
     * @param storages
     * @return die stärkste Festplatte
     */
    private static Storage strongestStorage(List<Storage> storages) {
        Storage strongest = null;
        int currentRank = -1; // M2 = 2, SSD = 1, HDD = 0

        for (Storage storage : storages) {
            if (storage == null) continue; // Null-Safety innerhalb der Liste

            int rank = 0;
            if (storage instanceof M2SSD) rank = 2;
            else if (storage instanceof SSD) rank = 1;
            else if (storage instanceof HDD) rank = 0;

            if (rank > currentRank) {
                strongest = storage;
                currentRank = rank;
                if (rank == 2) break; // Schneller Abbruch bei M2
            }
        }
        return strongest;
    }

    /**
     * Empfiehlt eine Aufrüstung basierend auf dem gefundenen Bottleneck. Wenn es keinen Bottleneck gibt ODER keine einzelne Komponente den Bottleneck ausmacht, wird null zurückgegeben.
     * @param bottleneckComponent die Komponente, die den Bottleneck darstellt
     * @return die empfohlene Komponente für ein Upgrade
     */
    private static Component recommendedUpgrade(Component bottleneckComponent) {
        if(!(bottleneck && oneComponent)) return null;

        ConnectionFactory cf = new ConnectionFactory();

        if (bottleneckComponent instanceof CPU cpu) {
            List<CPU> cpus = new CpuRepository(cf).findAll();
            double currentScore = (cpu.getBoostClockGHz() != null ? cpu.getBoostClockGHz() : cpu.getSpeedGHz()) * cpu.getCoreCount();
            CPU best = null;
            double bestScore = Double.MAX_VALUE;

            for (CPU candidate : cpus) {
                if (candidate.getId() == cpu.getId()) continue;
                double score = (candidate.getBoostClockGHz() != null ? candidate.getBoostClockGHz() : candidate.getSpeedGHz()) * candidate.getCoreCount();
                if (score >= currentScore + 15 && score < bestScore) {
                    best = candidate;
                    bestScore = score;
                }
            }
            return best;
        }

        if (bottleneckComponent instanceof GPU gpu) {
            List<GPU> gpus = new GpuRepository(cf).findAll();
            double currentScore = (gpu.getBoostClockMHz() != null ? gpu.getBoostClockMHz() : gpu.getCoreClockMHz()) / 1000.0 * (gpu.getVramGB() / 2.0);
            GPU best = null;
            double bestScore = Double.MAX_VALUE;

            for (GPU candidate : gpus) {
                if (candidate.getId() == gpu.getId()) continue;
                double score = (candidate.getBoostClockMHz() != null ? candidate.getBoostClockMHz() : candidate.getCoreClockMHz()) / 1000.0 * (candidate.getVramGB() / 2.0);
                if (score >= currentScore + 15 && score < bestScore) {
                    best = candidate;
                    bestScore = score;
                }
            }
            return best;
        }

        if (bottleneckComponent instanceof RAM ram) {
            List<RAM> rams = new RamRepository(cf).findAll();
            double currentScore = ram.getSpeedMHz() / 1000.0 * (ram.getCapacityGB() * 2 / 8.0); // Wir rechnen grob mit 2 RAM Sticks in einem PC
            RAM best = null;
            double bestScore = Double.MAX_VALUE;

            for (RAM candidate : rams) {
                if (candidate.getId() == ram.getId()) continue;
                double score = candidate.getSpeedMHz() / 1000.0 * (candidate.getCapacityGB() * 2 / 8.0); // Wir rechnen grob mit 2 RAM Sticks in einem PC
                if (score >= currentScore + 15 && score < bestScore) {
                    best = candidate;
                    bestScore = score;
                }
            }
            return best;
        }

        if(bottleneckComponent instanceof HDD) {
            return (new SsdRepository(cf).findAll().getFirst());
        }

        if(bottleneckComponent instanceof SSD) {
            return (new M2SsdRepository(cf).findAll().getFirst());
        }

        return null;

    }
}
