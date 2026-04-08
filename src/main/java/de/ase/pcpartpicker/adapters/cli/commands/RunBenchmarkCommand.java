package de.ase.pcpartpicker.adapters.cli.commands;


import de.ase.pcpartpicker.adapters.cli.Renderable;
import de.ase.pcpartpicker.part_assembly.Computer;
import de.ase.pcpartpicker.part_assembly.Performance;
/**
 * Klasse die die Benchmark Ergebnisse ausgibt, je nach gewähltem Benchmark
 * @param computer Computer für den der Benchmark durchgeführt wird
 * @param benchmark Der Benchmark der durchgeführt werden soll
 * @param benchmarkResults Ergebnisse des jeweiligen Benchmarks 
 * @param fpsResults Ergebnisse des FPS-Checks
 * @author Henri
 */
public class RunBenchmarkCommand implements Renderable {

    private final Computer computer; 
    private final int benchmark; 
    private double benchmarkResults; 
    private int[] fpsResults;
    
    public RunBenchmarkCommand(Computer computer, int benchmark) {
        this.computer = computer; 
        this.benchmark = benchmark; 
    }

    @Override
    public void render(String title){

        switch (benchmark) {
            case 1 -> {
                benchmarkResults = Performance.schehrBenchmarkScore(computer);
                printBenchmarkResults(benchmarkResults);}
            case 2 -> {
                benchmarkResults = Performance.weberBenchmarkScore(computer);
                printBenchmarkResults(benchmarkResults);}
            case 3 -> {
                benchmarkResults = Performance.enginBenchmarkScore(computer);
                printBenchmarkResults(benchmarkResults);}
            case 4 -> {
                fpsResults = Performance.calculateFPS(computer);
                printFPSResults(fpsResults);
                }
            default -> throw new AssertionError();
        }

    }

    private void printBenchmarkResults(double results) {
        System.out.println("Der Benchmark Score ist: " + String.format("%.2f", results ));
    }

    private void printFPSResults(int[] results) {
        int fps1080p = results[0];
        int fps1440p = results[1];
        int fps4K = results[2];

        System.out.println("Die FPS deines Computers in verschiedenen Auflösungen: \n");
        System.out.println("1080p: " + String.format("%d", fps1080p));
        System.out.println("1440p: " + String.format("%d", fps1440p));
        System.out.println("4k: " + String.format("%d", fps4K));
        System.out.println("");
    }
    


    
    
}
