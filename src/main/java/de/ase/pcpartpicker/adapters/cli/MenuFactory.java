package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.cli.commands.CreateUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.NewUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.OpenMenuCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowListCommand;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;

/**
 * Klasse, die die Menüs erstellt und konfiguriert
 * @param inputReader Liest Benutzereingaben
 * @param configs Enthält Konfigurationen für PC Komponenten
 * @author Henri
 */
public class MenuFactory {
    private final InputReader inputReader;
    private final ComponentConfigs configs;

    public static Menu createApp() {
        return new MenuFactory().createMainMenu();
    }

    public MenuFactory() {
        this.inputReader = new InputReader();
        this.configs = new ComponentConfigs(new ConnectionFactory());
    }

    public Menu createMainMenu() {
        Menu mainMenu = new Menu("PC Part Picker - Hauptmenü", inputReader);
        mainMenu.add(new MenuItem("Konfiguration ansehen", new OpenMenuCommand(createConfigurationMenu()))); 
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
        userMenu.add(new MenuItem("Neuen Nutzer anlegen", new NewUserCommand(inputReader))); 
        return userMenu; 
    }

    private Menu createLoginMenu() {
        Menu loginMenu = new Menu("Login", inputReader);
        
        return loginMenu;
    }

    private Menu createConfigurationMenu() {
        Menu menu = new Menu("Aktuelle Konfiguration", inputReader);
        return menu; 
    }


    private <T extends de.ase.pcpartpicker.domain.Component> MenuItem listItem(rComponentConfig<T> config) {
        return new MenuItem(config.title(), new ShowListCommand<>(config, inputReader));
    }
}