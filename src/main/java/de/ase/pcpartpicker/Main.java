package de.ase.pcpartpicker;

import de.ase.pcpartpicker.adapters.cli.ConsoleMenu;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.DatabaseInitializer;
import de.ase.pcpartpicker.adapters.sqlite.repositories.CpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.GpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.MainboardRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.RamRepository;

public class Main {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(connectionFactory);

        CpuRepository cpuRepository = new CpuRepository(connectionFactory);
        GpuRepository gpuRepository = new GpuRepository(connectionFactory);
        RamRepository ramRepository = new RamRepository(connectionFactory);
        MainboardRepository mainboardRepository = new MainboardRepository(connectionFactory);

        databaseInitializer.initialize();

        ConsoleMenu menu = new ConsoleMenu(cpuRepository, gpuRepository, ramRepository, mainboardRepository);
        menu.start();
    }
}