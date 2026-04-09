package de.ase.pcpartpicker.part_assembly;

/**
 * Diese Klasse enthält statische Methoden zur Berechnung der Leistung eines Computers basierend auf seinen Komponenten.
 * @author Tuluhan
 */
public final class Performance {

    // Privater Konstruktor verhindert, dass jemand "new Performance()" aufruft
    private Performance() {}

    /**
     * Berechnet die geschätzten FPS (Frames Per Second) für einen gegebenen Computer und eine bestimmte Auflösung.
     * Grobe (und etwas wilkürliche) Rechnung für Mid-Einstellungen da wir keine Logik haben die irgendwas mit Video Spielen kategorisiert
     * @param computer der Computer, dessen FPS berechnet werden soll
     * @return die geschätzten FPS für den gegebenen Computer für 1080p, 1440p und 4K Auflösung
     */
    public static int[] calculateFPS(Computer computer) {
        
        final int baseFPS = 60;
        final int cores = 8; // Bis die CPU-Kerne in der Datenbank gespeichert werden, nehmen wir einfach 8 an
        final double cpuClock = computer.getCPU().getBoostClockGHz() != null ? computer.getCPU().getBoostClockGHz() : computer.getCPU().getSpeedGHz();

        int fps1080p;
        int fps1440p;
        int fps4K;

        // Ohne GPU nur grobe Schätzung basierend auf der CPU-Leistung, da die GPU der wichtigste Faktor für die FPS ist
        if(computer.getGPU() == null) {
            // FPS für 1080p
            fps1080p = (int) (baseFPS * (cpuClock / 5.0) - (cores / 3.0));
            // FPS für 1440p
            fps1440p = (int) (baseFPS * (cpuClock / 5.0) * 0.70 - (cores / 3.0));
            // FPS für 4K
            fps4K = (int) (baseFPS * (cpuClock / 5.0) * 0.5 - (cores / 3.0));
        } else {
            final double vram = computer.getGPU().getVramGB();
            final double gpuClock = computer.getGPU().getBoostClockMHz() != null ? computer.getGPU().getBoostClockMHz() / 1000.0 : computer.getGPU().getCoreClockMHz() / 1000.0;
            // FPS für 1080p
            fps1080p = (int) (baseFPS * (vram / 10.0) * (cpuClock / 3.0) * (gpuClock / 1.5) - (cores / 2.0));
            // FPS für 1440p
            fps1440p = (int) (baseFPS * (vram / 15.0) * (cpuClock / 3.0) * (gpuClock / 1.5) - (cores / 2.0));
            // FPS für 4K
            fps4K = (int) (baseFPS * (vram / 25.0) * (cpuClock / 3.0) * (gpuClock / 1.5) - (cores / 2.0));
        }

        return new int[]{fps1080p, fps1440p, fps4K};
    }

    /**
     * Berechnet den Schehr Benchmark Score für einen gegebenen Computer.
     * Schehr Benchmark ist ein fiktiver Benchmark für Gaming
     * @param computer der Computer, dessen Schehr Benchmark Score berechnet werden soll
     * @return der berechnete Schehr Benchmark Score
     */
    public static double schehrBenchmarkScore(Computer computer) {

        final double targetScore = 100.0;
        double finalScore = 0.0;

        final int vram = computer.getGPU() == null ? 0 : computer.getGPU().getVramGB();
        final double gpuClock = computer.getGPU() == null ? 0 : (computer.getGPU().getBoostClockMHz() != null ? computer.getGPU().getBoostClockMHz() / 1000.0 : computer.getGPU().getCoreClockMHz() / 1000.0);
        final int cores = 8; // Bis die CPU-Kerne in der Datenbank gespeichert werden, nehmen wir einfach
        final double cpuClock = computer.getCPU().getBoostClockGHz() != null ? computer.getCPU().getBoostClockGHz() : computer.getCPU().getSpeedGHz();
        final int ram = computer.getRAM().getCapacityGB() * computer.getRamModule();
        final double ramSpeed = computer.getRAM().getSpeedMHz() / 1000.0;

        // Division durch High-End Werte um grob eine Punktzashl zwischen 0 und 100 zu erhalten, gewichtet nach der Wichtigkeit der Komponenten
        finalScore = (vram / 24.0) * 30 + (gpuClock / 2.5) * 10 + (cpuClock / 5.5) * 30 + (cores / 8.0) * 10 + (ram / 64.0) * 10 + (ramSpeed / 5.0) * 10;

        return Math.min(finalScore, targetScore);
    }

    /**
     * Berechnet den Weber Benchmark Score für einen gegebenen Computer.
     * Weber Benchmark ist ein fiktiver Benchmark für allgemeine Leistung
     * @param computer der Computer, dessen Weber Benchmark Score berechnet werden soll
     * @return der berechnete Weber Benchmark Score
     */
    public static double weberBenchmarkScore(Computer computer) {

        final double targetScore = 100.0;
        double finalScore = 0.0;

        final int vram = computer.getGPU() == null ? 0 : computer.getGPU().getVramGB();
        final double gpuClock = computer.getGPU() == null ? 0 : (computer.getGPU().getBoostClockMHz() != null ? computer.getGPU().getBoostClockMHz() / 1000.0 : computer.getGPU().getCoreClockMHz() / 1000.0);
        final int cores = 8; // Bis die CPU-Kerne in der Datenbank gespeichert werden, nehmen wir einfach 8 an
        final double cpuClock = computer.getCPU().getBoostClockGHz() != null ? computer.getCPU().getBoostClockGHz() : computer.getCPU().getSpeedGHz();
        final int ram = computer.getRAM().getCapacityGB() * computer.getRamModule();
        final double ramSpeed = computer.getRAM().getSpeedMHz() / 1000.0;

        // Division durch High-End Werte um grob eine Punktzashl zwischen 0 und 100 zu erhalten, gewichtet nach der Wichtigkeit der Komponenten
        finalScore = (vram / 24.0) * 10 + (gpuClock / 2.5) * 10 + (cpuClock / 5.5) * 30 + (cores / 8.0) * 20 + (ram / 64.0) * 15 + (ramSpeed / 5.0) * 15;

        return Math.min(finalScore, targetScore);
    }

    /**
     * Berechnet den Engin Benchmark Score für einen gegebenen Computer.
     * Engin Benchmark ist ein fiktiver Benchmark für CAD, 3D-Modellierung und Videoschnitt
     * @param computer der Computer, dessen Engin Benchmark Score berechnet werden soll
     * @return der berechnete Engin Benchmark Score
     */
    public static double enginBenchmarkScore(Computer computer) {

        final double targetScore = 100.0;
        double finalScore = 0.0;

        final int vram = computer.getGPU() == null ? 0 : computer.getGPU().getVramGB();
        final double gpuClock = computer.getGPU() == null ? 0 : (computer.getGPU().getBoostClockMHz() != null ? computer.getGPU().getBoostClockMHz() / 1000.0 : computer.getGPU().getCoreClockMHz() / 1000.0);
        final int cores = 8; // Bis die CPU-Kerne in der Datenbank gespeichert werden, nehmen wir einfach 8 an
        final double cpuClock = computer.getCPU().getBoostClockGHz() != null ? computer.getCPU().getBoostClockGHz() : computer.getCPU().getSpeedGHz();
        final int ram = computer.getRAM().getCapacityGB() * computer.getRamModule();
        final double ramSpeed = computer.getRAM().getSpeedMHz() / 1000.0;

        // Division durch High-End Werte um grob eine Punktzashl zwischen 0 und 100 zu erhalten, gewichtet nach der Wichtigkeit der Komponenten
        finalScore = (vram / 24.0) * 40 + (gpuClock / 2.5) * 20 + (cpuClock / 5.5) * 10 + (cores / 8.0) * 5 + (ram / 64.0) * 10 + (ramSpeed / 5.0) * 15;

        return Math.min(finalScore, targetScore);
    }
}