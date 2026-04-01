package de.ase.pcpartpicker.part_assembly;

import de.ase.pcpartpicker.domain.*;

/**
 * Diese Klasse enthält statische Methoden zur Berechnung der Leistung eines Computers basierend auf seinen Komponenten.
 * @author Tuluhan
 */
public final class Performance {

    // Privater Konstruktor verhindert, dass jemand "new Performance()" aufruft
    private Performance() {}

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
        final double clock = computer.getCPU().getSpeedGHz();
        final int ram = computer.getRAM().getCapacityGB() * computer.getRamModule();

        // Division durch High-End Werte um grob eine Punktzashl zwischen 0 und 100 zu erhalten, gewichtet nach der Wichtigkeit der Komponenten
        finalScore = (vram / 24.0) * 40 + (clock / 6.0) * 40 + (ram / 64.0) * 20;

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
        final double clock = computer.getCPU().getSpeedGHz();
        final int ram = computer.getRAM().getCapacityGB() * computer.getRamModule();

        // Division durch High-End Werte um grob eine Punktzashl zwischen 0 und 100 zu erhalten, gewichtet nach der Wichtigkeit der Komponenten
        finalScore = (vram / 24.0) * 20 + (clock / 6.0) * 50 + (ram / 64.0) * 30;

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
        final double clock = computer.getCPU().getSpeedGHz();
        final int ram = computer.getRAM().getCapacityGB() * computer.getRamModule();

        // Division durch High-End Werte um grob eine Punktzashl zwischen 0 und 100 zu erhalten, gewichtet nach der Wichtigkeit der Komponenten
        finalScore = (vram / 24.0) * 60 + (clock / 6.0) * 15 + (ram / 64.0) * 25;

        return Math.min(finalScore, targetScore);
    }
}