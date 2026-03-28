package de.ase.pcpartpicker.adapters.cli;
import de.ase.pcpartpicker.adapters.cli.commands.FinishComputerCommand;
import de.ase.pcpartpicker.adapters.cli.commands.LoginCommand;
import de.ase.pcpartpicker.adapters.cli.commands.NewUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.OpenMenuCommand;
import de.ase.pcpartpicker.adapters.cli.commands.SelectComponentCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowAllUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowComputerCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowCurrentDraftCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowListCommand;
import de.ase.pcpartpicker.adapters.cli.commands.StartComputerDraftCommand;
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
        Menu configuratorMenu = createConfiguratorMenu();
        computerMenu.add(new MenuItem("Neuen Computer anlegen", new StartComputerDraftCommand(computerDraft, configuratorMenu, userRepository, inputReader)));
        computerMenu.add(new MenuItem("Meine Computer anzeigen", new ShowComputerCommand(inputReader, computerRepository, false)));
        computerMenu.add(new MenuItem("Alle Computer anzeigen", new ShowComputerCommand(inputReader, computerRepository, true)));
        return computerMenu;
    }


    private Menu createConfiguratorMenu() {
        Menu menu = new Menu("PC Konfigurator (Entwurf)", inputReader);

    
        menu.add(new MenuItem("CPU auswählen", new SelectComponentCommand<>(
            inputReader, computerDraft, configs.cpu(),
            cpu -> cpu.getId(), cpu -> cpu.getName(),
            (draft, cpu) -> draft.setCpu(cpu)
        )));

       
        menu.add(new MenuItem("GPU auswählen", new SelectComponentCommand<>(
            inputReader, computerDraft, configs.gpu(),
            gpu -> gpu.getId(), gpu -> gpu.getName(), (draft, gpu) -> draft.setGpu(gpu)
        )));

        menu.add(new MenuItem("Mainboard auswählen", new SelectComponentCommand<>(
            inputReader, computerDraft, configs.mainboard(),
            mb -> mb.getId(), mb -> mb.getName(), (draft, mb) -> draft.setMainboard(mb)
        )));

        menu.add(new MenuItem("RAM auswählen", new SelectComponentCommand<>(
            inputReader, computerDraft, configs.ram(),
            ram -> ram.getId(), ram -> ram.getName(), 
            (draft, ram) -> {
                int amount = inputReader.readInt("Wie viele dieser Module willst du verbauen?", 1, 4);
                draft.setRam(ram, amount);
            }
        )));

        menu.add(new MenuItem("Netzteil auswählen", new SelectComponentCommand<>(
            inputReader, computerDraft, configs.psu(),
            psu -> psu.getId(), psu -> psu.getName(), (draft, psu) -> draft.setPsu(psu)
        )));

        menu.add(new MenuItem("Gehäuse auswählen", new SelectComponentCommand<>(
            inputReader, computerDraft, configs.pcCase(),
            c -> c.getId(), c -> c.getName(), (draft, c) -> draft.setComputerCase(c)
        )));

  
        menu.add(new MenuItem("Speicher (SSD) auswählen", new SelectComponentCommand<>(
            inputReader, computerDraft, configs.ssd(),
            ssd -> ssd.getId(), ssd -> ssd.getName(), 
            (draft, ssd) -> draft.addStorage(ssd) 
        )));

        menu.add(new MenuItem("Speicher (M2SSD) auswählen", new SelectComponentCommand<>(
            inputReader, computerDraft, configs.m2ssd(),
                m2ssd -> m2ssd.getId(), m2ssd -> m2ssd.getName(),
                (draft, m2ssd) -> draft.addStorage(m2ssd)
            )));


        menu.add(new MenuItem("Speicher (HDD) auswählen", new SelectComponentCommand<>(
            inputReader, computerDraft, configs.hdd(),
            hdd -> hdd.getId(), hdd -> hdd.getName(), 
            (draft, hdd) -> draft.addStorage(hdd) 
        )));

        menu.add(new MenuItem("Gewählte Komponenten anzeigen", new ShowCurrentDraftCommand(computerDraft, inputReader)));

     
        menu.add(new MenuItem("Computer prüfen & speichern", new FinishComputerCommand(inputReader, computerRepository, computerDraft)));

        return menu;
    }


    private <T extends de.ase.pcpartpicker.domain.Component> MenuItem listItem(rListConfiguration<T> config) {
        return new MenuItem(config.title(), new ShowListCommand<>(config, inputReader));
    }
}