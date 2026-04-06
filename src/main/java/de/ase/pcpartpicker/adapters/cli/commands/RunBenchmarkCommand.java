package de.ase.pcpartpicker.adapters.cli.commands;


import de.ase.pcpartpicker.adapters.cli.Renderable;
import de.ase.pcpartpicker.part_assembly.Computer;
import de.ase.pcpartpicker.part_assembly.Performance;

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
        System.out.println("Der Benchmark Score ist: " + results);
    }

    private void printFPSResults(int[] results) {
        int fps1080p = results[0];
        int fps1440p = results[1];
        int fps4K = results[2];

        System.out.println("Die FPS deines Computers in verschiednenen Auflösungen: \n");
        System.out.println("1080p: " + fps1080p);
        System.out.println("1440p: " + fps1440p);
        System.out.println("4k: " + fps4K);
        System.out.println("");
    }
    


    
    
}
