package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.DatabaseInitializer;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;
import de.ase.pcpartpicker.part_assembly.Computer;

public class AppContext {
    public final InputReader inputReader;
    public final UserRepository userRepository;
    public final ComputerRepository computerRepository;
    public final ComputerDraft computerDraft;
    public final ListConfiguration listConfigs;
    public final DatabaseInitializer databaseInitializer;
    

    private Computer selectedComputer; 

    public AppContext() {
        ConnectionFactory cf = new ConnectionFactory();
        this.inputReader = new InputReader();
        this.userRepository = new UserRepository(cf);
        this.computerRepository = new ComputerRepository(cf);
        this.computerDraft = new ComputerDraft();
        this.listConfigs = new ListConfiguration(cf);
        this.databaseInitializer = new DatabaseInitializer(cf);
    }


    public Computer getSelectedComputer() {
        return selectedComputer; 
    }

    public void setSelectedComputer(Computer selectedComputer) {
        this.selectedComputer = selectedComputer; 
    }
}