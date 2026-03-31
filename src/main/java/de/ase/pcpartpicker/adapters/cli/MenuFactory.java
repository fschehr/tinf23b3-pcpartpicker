package de.ase.pcpartpicker.adapters.cli;
import de.ase.pcpartpicker.ColorConstants;
import de.ase.pcpartpicker.adapters.cli.commands.FinishComputerCommand;
import de.ase.pcpartpicker.adapters.cli.commands.LoginCommand;
import de.ase.pcpartpicker.adapters.cli.commands.LogoutCommand;
import de.ase.pcpartpicker.adapters.cli.commands.NewUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.OpenMenuCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ResetDatabaseCommand;
import de.ase.pcpartpicker.adapters.cli.commands.SelectComponentCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowAllUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowComputerCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowCurrentDraftCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowListCommand;
import de.ase.pcpartpicker.adapters.cli.commands.StartComputerDraftCommand;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.DatabaseInitializer;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;
import de.ase.pcpartpicker.domain.*;

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
    private final DatabaseInitializer databaseInitializer = new DatabaseInitializer(connectionFactory);
    private final UserRepository userRepository = new UserRepository(connectionFactory); 
    private final ComputerRepository computerRepository = new ComputerRepository(connectionFactory);
    private final ComputerDraft computerDraft = new ComputerDraft();

    public static Menu createApp() {
        return new MenuFactory().createMainMenu();
    }

    public MenuFactory() {
        this.configs = new ListConfiguration(connectionFactory);
    }

    public Menu createMainMenu() {
        Menu mainMenu = new Menu("PC Part Picker - Hauptmenü", inputReader);
        mainMenu.add(new MenuItem("Verfügbare Teile ansehen", new OpenMenuCommand(createComponentMenu()))); 
        mainMenu.add(new MenuItem("Computerverwaltung", new OpenMenuCommand(createComputerMenu())));
        mainMenu.add(new MenuItem("Nutzerverwaltung", new OpenMenuCommand(createUserMenu()))); 
        mainMenu.add(new MenuItem("Login", new OpenMenuCommand(createLoginMenu()))); 
        mainMenu.add(new MenuItem("Datenbank reset", new ResetDatabaseCommand(inputReader, databaseInitializer)));
        UIUtils.addExitNavigation(mainMenu);
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
        UIUtils.addBackNavigation(componentMenu);
        return componentMenu;
    }


    private Menu createUserMenu() {
        Menu userMenu = new Menu("Nutzerverwaltung",inputReader); 
        userMenu.add(new MenuItem("Neuen Nutzer anlegen", new NewUserCommand(inputReader, userRepository))); 
        userMenu.add(new MenuItem("Zeige alle Nutzer", new ShowAllUserCommand(inputReader,configs))); 
        UIUtils.addBackNavigation(userMenu);
        return userMenu; 
    }

    private Menu createLoginMenu() {
        Menu loginMenu = new Menu("Login", inputReader);
        loginMenu.add(new MenuItem("Login starten", new LoginCommand(inputReader, userRepository)));
        loginMenu.add(new MenuItem("Logout", new LogoutCommand()));
        UIUtils.addBackNavigation(loginMenu);
        return loginMenu;
    }


    private Menu createComputerMenu() {
        Menu computerMenu = new Menu("Computerverwaltung", inputReader);
        Menu configuratorMenu = createConfiguratorMenu();
        computerMenu.add(new MenuItem("Neuen Computer anlegen", new StartComputerDraftCommand(computerDraft, configuratorMenu, userRepository, inputReader)));
        computerMenu.add(new MenuItem("Meine Computer anzeigen", new ShowComputerCommand(inputReader, computerRepository, false)));
        computerMenu.add(new MenuItem("Alle Computer anzeigen", new ShowComputerCommand(inputReader, computerRepository, true)));
        UIUtils.addBackNavigation(computerMenu);
        return computerMenu;
    }


    private Menu createConfiguratorMenu() {
        Menu menu = new Menu("PC Konfigurator (Entwurf)", inputReader);

        menu.add(createDraftMenuItem(
            "CPU auswählen",
            () -> computerDraft.getCPU() != null,
            configs.cpu(),
            ComputerDraft::setCpu
        ));

        menu.add(createDraftMenuItem(
            "GPU auswählen",
            () -> computerDraft.getGPU() != null,
            configs.gpu(),
            ComputerDraft::setGpu
        ));

        menu.add(createDraftMenuItem(
            "Mainboard auswählen",
            () -> computerDraft.getMainboard() != null,
            configs.mainboard(),
            ComputerDraft::setMainboard
        ));

        // Beim RAM brauchen wir weiterhin die Lambda, wegen der Zusatz-Abfrage
        menu.add(createDraftMenuItem(
            "RAM auswählen",
            () -> computerDraft.getRAM() != null,
            configs.ram(),
            (draft, ram) -> {
                int amount = inputReader.readInt("Wie viele dieser Module willst du verbauen?", 1, draft.getMainboardRamSlots());
                draft.setRam(ram, amount);
            }
        ));

        menu.add(createDraftMenuItem(
            "Netzteil auswählen",
            () -> computerDraft.getPSU() != null,
            configs.psu(),
            ComputerDraft::setPsu
        ));

        menu.add(createDraftMenuItem(
            "Gehäuse auswählen",
            () -> computerDraft.getComputerCase() != null,
            configs.pcCase(),
            ComputerDraft::setComputerCase
        ));

        menu.add(createDraftMenuItem(
            "Speicher (SSD) auswählen",
            () -> computerDraft.getStorage() != null && computerDraft.getStorage().stream().anyMatch(s -> s instanceof SSD),
            configs.ssd(),
            ComputerDraft::addStorage
        ));

        menu.add(createDraftMenuItem(
            "Speicher (M2SSD) auswählen",
            () -> computerDraft.getStorage() != null && computerDraft.getStorage().stream().anyMatch(s -> s instanceof M2SSD),
            configs.m2ssd(),
            ComputerDraft::addStorage
        ));

        menu.add(createDraftMenuItem(
            "Speicher (HDD) auswählen",
            () -> computerDraft.getStorage() != null && computerDraft.getStorage().stream().anyMatch(s -> s instanceof HDD),
            configs.hdd(),
            ComputerDraft::addStorage
        ));

        menu.add(new MenuItem("Gewählte Komponenten anzeigen", new ShowCurrentDraftCommand(computerDraft, inputReader)));
        menu.add(new MenuItem("Computer prüfen & speichern", new FinishComputerCommand(inputReader, computerRepository, computerDraft)));

        UIUtils.addConfiguratorBackNavigation(menu, inputReader, computerDraft);

        return menu;
    }


    private <T extends Component> MenuItem listItem(rListConfiguration<T> config) {
        return new MenuItem(config.title(), new ShowListCommand<>(config, inputReader));
    }

    private <T extends Component> MenuItem createDraftMenuItem(
        String title,
        java.util.function.Supplier<Boolean> isSelected,
        rListConfiguration<T> config,
        java.util.function.BiConsumer<ComputerDraft, T> draftSetter
    ) {
        return new MenuItem(
            () -> title + (isSelected.get() ? ColorConstants.GREEN(" (ausgewählt)") : ""),
            new SelectComponentCommand<>(inputReader, computerDraft, config, draftSetter)
        );
    }
}