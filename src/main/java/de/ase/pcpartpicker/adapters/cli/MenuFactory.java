package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.cli.commands.*;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;

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
        return mainMenu;
    }

    private Menu createComponentMenu() {
        Menu menu = new Menu("Komponenten auswählen", inputReader);
        
        menu.add(listItem(configs.cpu()));
        menu.add(listItem(configs.gpu()));
        menu.add(listItem(configs.ram()));
        menu.add(listItem(configs.mainboard()));
        menu.add(listItem(configs.psu()));
        menu.add(listItem(configs.pcCase()));
        menu.add(listItem(configs.m2ssd())); 
        menu.add(listItem(configs.ssd()));    
        menu.add(listItem(configs.hdd()));     
        return menu;
    }


    private Menu createConfigurationMenu() {
        Menu menu = new Menu("Aktuelle Konfiguration", inputReader);
        return menu; 
    }


    private <T extends de.ase.pcpartpicker.domain.Component> MenuItem listItem(rComponentConfig<T> config) {
        return new MenuItem(config.title(), new ShowListCommand<>(config, inputReader));
    }
}