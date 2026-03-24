package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.cli.commands.CreateComputerCommand;
import de.ase.pcpartpicker.adapters.cli.commands.LoginCommand;
import de.ase.pcpartpicker.adapters.cli.commands.NewUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.OpenMenuCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowAllUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowListCommand;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;

/**
 * Klasse, die die Menüs erstellt und konfiguriert
 * @param inputReader Liest Benutzereingaben
 * @param configs Enthält Konfigurationen für PC Komponenten
 * @author Henri
 */
public class MenuFactory {
    private final ListConfiguration configs;
    private final InputReader inputReader = new InputReader();
    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private final UserRepository userRepository = new UserRepository(connectionFactory); 
    private final ComputerRepository computerRepository = new ComputerRepository(connectionFactory);
    private final SessionManager sessionManager = new SessionManager();

    public static Menu createApp() {
        return new MenuFactory().createMainMenu();
    }

    public MenuFactory() {
        this.configs = new ListConfiguration(connectionFactory);
    }

    public Menu createMainMenu() {
        Menu mainMenu = new Menu("PC Part Picker - Hauptmenü", inputReader);
        mainMenu.add(new MenuItem("Konfiguration ansehen", new OpenMenuCommand(createConfigurationMenu()))); 
        mainMenu.add(new MenuItem("Computerverwaltung", new OpenMenuCommand(createComputerMenu())));
        mainMenu.add(new MenuItem("Komponenten auswählen", new OpenMenuCommand(createComponentMenu())));
        mainMenu.add(new MenuItem("Nutzerverwaltung", new OpenMenuCommand(createUserMenu()))); 
        mainMenu.add(new MenuItem("Login", new OpenMenuCommand(createLoginMenu()))); 
        return mainMenu;
    }

    private Menu createComponentMenu() {
        Menu componentMenu = new Menu("Komponenten auswählen", inputReader);
        
        componentMenu.add(listItem(configs.cpu()));
        componentMenu.add(listItem(configs.gpu()));
        componentMenu.add(listItem(configs.ram()));
        componentMenu.add(listItem(configs.mainboard()));
        componentMenu.add(listItem(configs.psu()));
        componentMenu.add(listItem(configs.pcCase()));
        componentMenu.add(listItem(configs.m2ssd())); 
        componentMenu.add(listItem(configs.ssd()));    
        componentMenu.add(listItem(configs.hdd()));     
        return componentMenu;
    }


    private Menu createUserMenu() {
        Menu userMenu = new Menu("Nutzerverwaltung",inputReader); 
        userMenu.add(new MenuItem("Neuen Nutzer anlegen", new NewUserCommand(inputReader, userRepository))); 
        userMenu.add(new MenuItem("Zeige alle Nutzer", new ShowAllUserCommand(inputReader,configs))); 
        return userMenu; 
    }

    private Menu createLoginMenu() {
        Menu loginMenu = new Menu("Login", inputReader);
        loginMenu.add(new MenuItem("Login starten", new LoginCommand(inputReader, userRepository)));
        return loginMenu;
    }


    private Menu createConfigurationMenu() {
        Menu menu = new Menu("Aktuelle Konfiguration", inputReader);
        return menu; 
    }

    private Menu createComputerMenu() {
        Menu computerMenu = new Menu("Computerverwaltung", inputReader);
        computerMenu.add(new MenuItem("neuen Computer anlegen", new CreateComputerCommand(inputReader, computerRepository, sessionManager)));
        return computerMenu;
    }


    private <T extends de.ase.pcpartpicker.domain.Component> MenuItem listItem(rListConfiguration<T> config) {
        return new MenuItem(config.title(), new ShowListCommand<>(config, inputReader));
    }
}