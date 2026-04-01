package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.utils.NavigationUtils;
import de.ase.pcpartpicker.adapters.cli.utils.TableUtils;

public class ShowCurrentDraftCommand implements ICommand {
    private final AppContext context; 

    public ShowCurrentDraftCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {

        NavigationUtils.clear();
        System.out.println("\n==================================");
        System.out.println("   DEIN AKTUELLER PC-ENTWURF");
        System.out.println("==================================");

        TableGenerator table = new TableGenerator("Komponente", "Eigenschaft", "Details");
        for (String[] row : TableUtils.getDraftAsTableRows(context.computerDraft)) {
            table.addRow(row);
        }
        table.printTable();

        System.out.println("==================================\n");
        context.inputReader.waitForEnter("Enter drücken um zurückzukehren...");

    }
}