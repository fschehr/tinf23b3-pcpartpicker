package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;

import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.utils.ExceptionUtils;
import de.ase.pcpartpicker.adapters.cli.utils.TableUtils;
import de.ase.pcpartpicker.domain.HelperClasses.User;
import de.ase.pcpartpicker.part_assembly.Computer;

public class FinishComputerCommand implements ICommand {

    private final AppContext context; 
    // private final InputReader inputReader;
    // private final ComputerRepository computerRepository;
    // private final ComputerDraft draft;

    public FinishComputerCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        
        try {
            System.out.println("\n--- Kompatibilität wird geprüft ---\n");
            Computer newComputer = context.computerDraft.getBuilder().build();

            if (newComputer != null) {
                User currentUser = SessionManager.getcurrentUser();

                int saveId = context.computerRepository.save(currentUser.getId(), newComputer);

                List<Computer> computers = context.computerRepository.findAllByUserId(currentUser.getId());


                Computer updatedComputer = computers.stream()
                    .filter(c -> c.getId() == saveId)
                    .findFirst()
                    .orElse(null);

                context.setSelectedComputer(updatedComputer);
                
                ExceptionUtils.printSuccess("Computer wurde erfolgreich gebaut.");
                TableGenerator table = new TableGenerator(new String[]{"Komponente", "Eigenschaft", "Details"});
                for (String[] row : TableUtils.getComputerAsTableRows(newComputer)){
                    table.addRow(row);
                }
                table.printTable();
                context.computerDraft.setEditingComputerId(saveId);
                context.computerDraft.markAsSaved();
            } 
        } catch (Exception e) {
            ExceptionUtils.printError("Beim Speichern ist ein Fehler aufgetreten: \" + e.getMessage()");
        }

        context.inputReader.waitForEnter("Enter drücken...");
    }
}