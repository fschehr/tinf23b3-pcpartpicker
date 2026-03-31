package de.ase.pcpartpicker.adapters.cli.utils;

import de.ase.pcpartpicker.adapters.cli.commands.BackCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ExitCommand;
import de.ase.pcpartpicker.domain.HelperClasses.User;
import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.Menu;
import de.ase.pcpartpicker.adapters.cli.MenuItem;
import de.ase.pcpartpicker.adapters.cli.commands.*;
import de.ase.pcpartpicker.adapters.sqlite.repositories.*;
import java.util.List; 


/**
 * Klasse, die Hilfsmethoden enhält
 * @link {@link #clear()} Bereinigt die CLI
 * @author Henri
 */
public class UIUtils {

    private final ComputerRepository computerRepository; 
    private final UserRepository userRepository;
    private final InputReader inputReader; 

    public UIUtils(ComputerRepository computerRepository, UserRepository userRepository, InputReader inputReader) {
        this.computerRepository = computerRepository; 
        this.inputReader = inputReader;
        this.userRepository = userRepository;
    }



}
