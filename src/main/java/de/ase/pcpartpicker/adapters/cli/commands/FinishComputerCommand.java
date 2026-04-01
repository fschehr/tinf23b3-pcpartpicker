package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.domain.HelperClasses.User;
import de.ase.pcpartpicker.part_assembly.Computer;
import de.ase.pcpartpicker.adapters.cli.utils.TableUtils;

import de.ase.pcpartpicker.ColorConstants;

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
        
        try {
            System.out.println("\n--- Kompatibilität wird geprüft ---\n");
            Computer newComputer = draft.getBuilder().build();

            if (newComputer != null) {
                User currentUser = SessionManager.getcurrentUser();
                computerRepository.save(currentUser.getId(), newComputer);
                
                System.out.println(ColorConstants.GREEN("ERFOLG") + " | Computer wurde erfolgreich gebaut.");
                TableGenerator table = new TableGenerator(new String[]{"Komponente", "Eigenschaft", "Details"});
                for (String[] row : TableUtils.getComputerAsTableRows(newComputer)){
                    table.addRow(row);
                }
                table.printTable();
                // TODO: erstmal so, dass man einen neuen Computer anlegen kann
                draft.startNewDraft();
            } 
        } catch (Exception e) {
            System.out.println(ColorConstants.RED("FEHLER") + " | Beim Speichern ist ein Fehler aufgetreten: " + e.getMessage());
        }
        
        
        inputReader.waitForEnter("Enter drücken...");
    }
}