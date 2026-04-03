package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.ColorConstants;
import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class SaveDraftCommand implements ICommand {
    private final InputReader inputReader;
    private final ComputerRepository computerRepository;
    private final ComputerDraft draft;

    public SaveDraftCommand(InputReader inputReader, ComputerRepository computerRepository, ComputerDraft draft) {
        this.inputReader = inputReader;
        this.computerRepository = computerRepository;
        this.draft = draft;
    }

    @Override
    public void execute() {
        if (!SessionManager.isLoggedIn()) {
            System.out.println(ColorConstants.RED("FEHLER") + " | Du musst eingeloggt sein, um Entwürfe zu speichern.");
            inputReader.waitForEnter("Enter drücken...");
            return;
        }

        try {
            User currentUser = SessionManager.getcurrentUser();
            int computerId = computerRepository.saveAsDraft(currentUser.getId(), draft);
            draft.markAsSaved();
            System.out.println(ColorConstants.GREEN("ERFOLG") + " | Entwurf gespeichert (Computer-ID: " + computerId + ").");
        } catch (Exception e) {
            System.out.println(ColorConstants.RED("FEHLER") + " | Entwurf konnte nicht gespeichert werden: " + e.getMessage());
        }

        inputReader.waitForEnter("Enter drücken...");
    }
}
