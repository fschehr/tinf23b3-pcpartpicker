package de.ase.pcpartpicker.adapters.cli.commands;


import de.ase.pcpartpicker.adapters.cli.Renderable;
import de.ase.pcpartpicker.part_assembly.Computer;
import de.ase.pcpartpicker.part_assembly.Performance;

public class RunBenchmarkCommand implements Renderable {

    private final Computer computer; 
    private final int benchmark; 
    private double results; 
    
    public RunBenchmarkCommand(Computer computer, int benchmark) {
        this.computer = computer; 
        this.benchmark = benchmark; 
    }

    @Override
    public void render(String title){

        System.out.println(title);

        switch (benchmark) {
            case 1 -> results = Performance.schehrBenchmarkScore(computer);
            case 2 -> results = Performance.weberBenchmarkScore(computer);
            case 3 -> results = Performance.enginBenchmarkScore(computer);
            default -> throw new AssertionError();
        }

        System.out.println(String.format("Die Ergebnisse des Benchmarks sind: %.0f", results));
    }
    


    
    
}
