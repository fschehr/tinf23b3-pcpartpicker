package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;

public class ShowCurrentDraftCommand implements ICommand {
    private final ComputerDraft draft;
    private final InputReader inputReader;

    public ShowCurrentDraftCommand(ComputerDraft draft, InputReader inputReader) {
        this.draft = draft;
        this.inputReader = inputReader;
    }

    @Override
    public void execute() {
        System.out.println("\n==================================");
        System.out.println("   DEIN AKTUELLER PC-ENTWURF");
        System.out.println("==================================");
        
        // draft.printCurrentState(); 

        TableGenerator table = new TableGenerator(new String[]{"Komponente", "Eigenschaft", "Details"});
        for (String[] row : draft.getDraftAsTableRows()) {
            table.addRow(row);
        }
        table.printTable();
        
        System.out.println("==================================\n");
        inputReader.waitForEnter("Enter drücken um zurückzukehren...");
    }
}