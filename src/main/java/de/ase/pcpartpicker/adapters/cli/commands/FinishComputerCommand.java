package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.part_assembly.Computer;

public class FinishComputerCommand implements ICommand {
    private final InputReader inputReader;
    private final ComputerRepository computerRepository;
    private final ComputerDraft draft;

    public FinishComputerCommand(InputReader inputReader, ComputerRepository repo, ComputerDraft draft) {
        this.inputReader = inputReader;
        this.computerRepository = repo;
        this.draft = draft;
    }

    @Override
    public void execute() {
        System.out.println("\n--- Kompatibilität wird geprüft ---");
        Computer newComputer = draft.getBuilder().build();

        if (newComputer != null) {
            // Speichern in Datenbank aktivieren, wenn gewünscht:
            computerRepository.save(SessionManager.getcurrentUser().getId(), newComputer);
            
            System.out.println("Erfolg! Computer wurde erfolgreich gebaut.");
            newComputer.printConfiguration();
            System.out.println("Gesamtpreis: " + newComputer.getTotalPrice() + " €");
            
            draft.clear();
        } 
        
        inputReader.waitForEnter("Enter drücken...");
    }
}