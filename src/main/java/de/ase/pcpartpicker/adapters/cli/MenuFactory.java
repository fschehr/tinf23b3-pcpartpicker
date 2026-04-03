package de.ase.pcpartpicker.adapters.cli;

import java.util.List;

import de.ase.pcpartpicker.ColorConstants;
import de.ase.pcpartpicker.adapters.cli.commands.FinishComputerCommand;
import de.ase.pcpartpicker.adapters.cli.commands.LoginCommand;
import de.ase.pcpartpicker.adapters.cli.commands.LogoutCommand;
import de.ase.pcpartpicker.adapters.cli.commands.NewUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.OpenMenuCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ResetDatabaseCommand;
import de.ase.pcpartpicker.adapters.cli.commands.SaveDraftCommand;
import de.ase.pcpartpicker.adapters.cli.commands.SelectComponentCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowAllUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowComputerCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowCurrentDraftCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowListCommand;
import de.ase.pcpartpicker.adapters.cli.commands.StartComputerDraftCommand;
import de.ase.pcpartpicker.adapters.cli.utils.NavigationUtils;
import de.ase.pcpartpicker.domain.Component;
import de.ase.pcpartpicker.domain.HDD;
import de.ase.pcpartpicker.domain.HelperClasses.User;
import de.ase.pcpartpicker.domain.M2SSD;
import de.ase.pcpartpicker.domain.SSD;

/**
 * Klasse, die die Menüs erstellt und konfiguriert.
 * Bezieht alle Abhängigkeiten über den AppContext.
 */
public class MenuFactory {
    

    private final AppContext context;

    public MenuFactory(AppContext context) {
        this.context = context;
    }

    public static Menu createApp() {
        AppContext context = new AppContext(); 
        return new MenuFactory(context).createMainMenu();
    }

    public Menu createMainMenu() {
        Menu mainMenu = new Menu("PC Part Picker - Hauptmenü", context.inputReader);
        mainMenu.add(new MenuItem("Verfügbare Teile ansehen", new OpenMenuCommand(createComponentMenu()))); 
        mainMenu.add(new MenuItem("Computerverwaltung", new OpenMenuCommand(createComputerMenu())));
        mainMenu.add(new MenuItem("Nutzerverwaltung", new OpenMenuCommand(createUserMenu()))); 
        mainMenu.add(new MenuItem("Login", new OpenMenuCommand(createLoginMenu()))); 
        mainMenu.add(new MenuItem("Datenbank reset", new ResetDatabaseCommand(context.inputReader, context.databaseInitializer)));
        
        NavigationUtils.addExitNavigation(mainMenu);
        return mainMenu;
    }

    private Menu createComponentMenu() {
        Menu componentMenu = new Menu("Komponenten auswählen", context.inputReader);

        Object[][] configs = {
            { "CPUs anzeigen", context.listConfigs.cpu() },
            { "GPUs anzeigen", context.listConfigs.gpu() },
            { "RAM anzeigen", context.listConfigs.ram() },
            { "Mainboards anzeigen", context.listConfigs.mainboard() },
            { "Netzteile anzeigen", context.listConfigs.psu() },
            { "Gehäuse anzeigen", context.listConfigs.pcCase() },
            { "M.2 SSDs anzeigen", context.listConfigs.m2ssd() },
            { "SSDs anzeigen", context.listConfigs.ssd() },
            { "HDDs anzeigen", context.listConfigs.hdd() }
        };

        for (Object[] entry : configs) {
            String title = (String) entry[0];
            rListConfiguration<?> config = (rListConfiguration<?>) entry[1];
            componentMenu.add(new MenuItem(title, new OpenMenuCommand(createListMenu(config))));
        }

        NavigationUtils.addBackNavigation(componentMenu);
        return componentMenu;
    }

    private Menu createUserMenu() {
        Menu userMenu = new Menu("Nutzerverwaltung", context.inputReader); 
        userMenu.add(new MenuItem("Neuen Nutzer anlegen", new NewUserCommand(context.inputReader, context.userRepository))); 
        userMenu.add(new MenuItem("Zeige alle Nutzer", new ShowAllUserCommand(context.inputReader, context.listConfigs))); 
        
        NavigationUtils.addBackNavigation(userMenu);
        return userMenu; 
    }

    private Menu createLoginMenu() {
        Menu loginMenu = new Menu("Login", context.inputReader);
        loginMenu.add(new MenuItem("Login starten", new LoginCommand(context.inputReader, context.userRepository)));
        loginMenu.add(new MenuItem("Logout", new LogoutCommand()));
        
        NavigationUtils.addBackNavigation(loginMenu);
        return loginMenu;
    }

    private Menu createComputerMenu() {
        Menu computerMenu = new Menu("Computerverwaltung", context.inputReader);
        Menu configuratorMenu = createConfiguratorMenu();
        computerMenu.add(new MenuItem("Neuen Computer anlegen", new StartComputerDraftCommand(context, configuratorMenu)));
        computerMenu.add(new MenuItem("Meine Computer anzeigen", new OpenMenuCommand(createOwnComputerMenu())));
        computerMenu.add(new MenuItem("Alle Computer anzeigen", new OpenMenuCommand(createAllComputerMenu())));
        
        NavigationUtils.addBackNavigation(computerMenu);
        return computerMenu;
    }

    private Menu createOwnComputerMenu() {
        Menu menu = new Menu("Meine Computer", context.inputReader,Menu.NavMode.PAGING); 
        ShowComputerCommand show = new ShowComputerCommand(context,ShowComputerCommand.Mode.OWN);
        menu.setCustomContent(show);
        return menu;
    }

    private Menu createAllComputerMenu() {
        Menu showComputerMenu = new Menu("Alle Computer: User wählen", context.inputReader); 
        
        List<User> users = context.userRepository.findAll();
        for(User user : users) {
            String username = user.getName(); 
            int userID = user.getId();
            showComputerMenu.add(new MenuItem(username, new OpenMenuCommand(createUserComputerMenu(username, userID))));
        }
        
        NavigationUtils.addBackNavigation(showComputerMenu);
        return showComputerMenu;
    }

    private Menu createUserComputerMenu(String name, int userID){
        Menu menu = new Menu(name + "'s Computer", context.inputReader, Menu.NavMode.PAGING); 
        ShowComputerCommand showComputer = new ShowComputerCommand(context, userID); 
        menu.setCustomContent(showComputer);
        return menu; 
    }

    public Menu createConfiguratorMenu() {
        Menu menu = new Menu("PC Konfigurator (Entwurf)", context.inputReader);
        ComputerDraft draft = context.computerDraft;

        menu.add(createDraftMenuItem(
            "CPU auswählen",
            () -> draft.getCPU() != null,
            context.listConfigs.cpu(),
            ComputerDraft::setCpu
        ));

        menu.add(createDraftMenuItem(
            "GPU auswählen",
            () -> draft.getGPU() != null,
            context.listConfigs.gpu(),
            ComputerDraft::setGpu
        ));

        menu.add(createDraftMenuItem(
            "Mainboard auswählen",
            () -> draft.getMainboard() != null,
            context.listConfigs.mainboard(),
            ComputerDraft::setMainboard
        ));

        menu.add(createDraftMenuItem(
            "RAM auswählen",
            () -> draft.getRAM() != null,
            context.listConfigs.ram(),
            (d, ram) -> {
                int amount = context.inputReader.readInt("Wie viele dieser Module willst du verbauen?", 1, d.getMainboardRamSlots());
                d.setRam(ram, amount);
            }
        ));

        menu.add(createDraftMenuItem(
            "Netzteil auswählen",
            () -> draft.getPSU() != null,
            context.listConfigs.psu(),
            ComputerDraft::setPsu
        ));

        menu.add(createDraftMenuItem(
            "Gehäuse auswählen",
            () -> draft.getComputerCase() != null,
            context.listConfigs.pcCase(),
            ComputerDraft::setComputerCase
        ));

        menu.add(createDraftMenuItem(
            "Speicher (SSD) auswählen",
            () -> draft.getStorage() != null && draft.getStorage().stream().anyMatch(s -> s instanceof SSD),
            context.listConfigs.ssd(),
            ComputerDraft::addStorage
        ));

        menu.add(createDraftMenuItem(
            "Speicher (M2SSD) auswählen",
            () -> draft.getStorage() != null && draft.getStorage().stream().anyMatch(s -> s instanceof M2SSD),
            context.listConfigs.m2ssd(),
            ComputerDraft::addStorage
        ));

        menu.add(createDraftMenuItem(
            "Speicher (HDD) auswählen",
            () -> draft.getStorage() != null && draft.getStorage().stream().anyMatch(s -> s instanceof HDD),
            context.listConfigs.hdd(),
            ComputerDraft::addStorage
        ));

        menu.add(new MenuItem("Gewählte Komponenten anzeigen", new ShowCurrentDraftCommand(context)));
        menu.add(new MenuItem("Entwurf speichern", new SaveDraftCommand(context.inputReader, context.computerRepository, draft)));
        menu.add(new MenuItem("Computer prüfen & speichern", new FinishComputerCommand(context.inputReader, context.computerRepository, draft)));

        NavigationUtils.addConfiguratorBackNavigation(menu, context.inputReader, draft);

        return menu;
    }

    private <T> Menu createListMenu(rListConfiguration<T> config) {
        Menu menu = new Menu(config.title(), context.inputReader, Menu.NavMode.PAGING);
        menu.setCustomContent(new ShowListCommand<>(config, context.inputReader));
        return menu;
    }


    private <T extends Component> MenuItem createDraftMenuItem(
        String title,
        java.util.function.Supplier<Boolean> isSelected,
        rListConfiguration<T> config,
        java.util.function.BiConsumer<ComputerDraft, T> draftSetter
    ) {
        return new MenuItem(
            () -> title + (isSelected.get() ? ColorConstants.GREEN(" (ausgewählt)") : ""),
            new SelectComponentCommand<>(context.inputReader, context.computerDraft, config, draftSetter)
        );
    }


}