# PC Part Picker - Klassendiagramm

## Übersicht der Klassenstruktur

```
┌─────────────────────────────────────────────────────────────────┐
│                            Main.java                             │
│                     (Haupteinstiegspunkt)                        │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                         MainMenu.java                            │
│        (Hauptmenü und Anwendungssteuerung - 222 Zeilen)         │
└───────┬─────────────────┬──────────────────┬────────────────────┘
        │                 │                  │
        ▼                 ▼                  ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────────┐
│PartBrowserUI │  │BuildConf...  │  │CompatibilityCheck│
│  (217 Zeilen)│  │  (356 Zeilen)│  │    (204 Zeilen)  │
└──────┬───────┘  └──────┬───────┘  └────────┬─────────┘
       │                 │                    │
       └─────────────────┴────────────────────┘
                         │
                         ▼
              ┌──────────────────┐
              │ PartRepository   │
              │  (250 Zeilen)    │
              └─────────┬────────┘
                        │
                        ▼
              ┌──────────────────┐
              │   DataLoader     │
              │  (112 Zeilen)    │
              └──────────────────┘
```

## Model-Hierarchie (Vererbung)

```
                    ┌──────────────────┐
                    │   PCPart.java    │
                    │  (Abstract Base) │
                    │   (71 Zeilen)    │
                    └────────┬─────────┘
                             │
           ┌─────────────────┼─────────────────┐
           │                 │                 │
     ┌─────▼─────┐    ┌─────▼─────┐    ┌─────▼─────┐
     │  CPU.java │    │ Memory    │    │VideoCard  │
     │(120 Zeilen│    │(120 Zeilen│    │(104 Zeilen│
     └───────────┘    └───────────┘    └───────────┘
           │                 │                 │
     ┌─────▼─────┐    ┌─────▼─────┐    ┌─────▼─────┐
     │Motherboard│    │ Storage   │    │   Case    │
     │(92 Zeilen)│    │(109 Zeilen│    │(92 Zeilen)│
     └───────────┘    └───────────┘    └───────────┘
           │                 │                 │
     ┌─────▼─────┐    ┌─────▼─────┐
     │PowerSupply│    │CPUCooler  │
     │(92 Zeilen)│    │(79 Zeilen)│
     └───────────┘    └───────────┘
```

## PCBuild Komposition

```
┌──────────────────────────────────────────┐
│          PCBuild.java                    │
│         (195 Zeilen)                     │
├──────────────────────────────────────────┤
│ - buildName: String                      │
│ - cpu: CPU                               │
│ - cpuCooler: CPUCooler                   │
│ - motherboard: Motherboard               │
│ - memory: Memory                         │
│ - storageDevices: List<Storage>          │
│ - videoCard: VideoCard                   │
│ - pcCase: Case                           │
│ - powerSupply: PowerSupply               │
├──────────────────────────────────────────┤
│ + getTotalPrice(): double                │
│ + isComplete(): boolean                  │
│ + getMissingComponents(): List<String>  │
│ + getSummary(): String                   │
└──────────────────────────────────────────┘
```

## Utility Klassen

```
┌─────────────────────┐       ┌─────────────────────┐
│  ConsoleHelper      │       │  InputValidator     │
│  (157 Zeilen)       │       │  (91 Zeilen)        │
├─────────────────────┤       ├─────────────────────┤
│ + clearScreen()     │       │ + getIntInput()     │
│ + printHeader()     │       │ + getDoubleInput()  │
│ + printPartList()   │       │ + getStringInput()  │
│ + printError()      │       │ + getYesNoInput()   │
│ + printSuccess()    │       │ + waitForEnter()    │
│ + formatPrice()     │       │ + getMenuChoice()   │
└─────────────────────┘       └─────────────────────┘
```

## Package-Struktur mit Zeilenzahlen

```
de.dhbw.tinf23b3.pcpartpicker/
│
├── Main.java (30 Zeilen)
│
├── model/ (1.084 Zeilen)
│   ├── PCPart.java (71 Zeilen)
│   ├── CPU.java (120 Zeilen)
│   ├── CPUCooler.java (79 Zeilen)
│   ├── Motherboard.java (92 Zeilen)
│   ├── Memory.java (120 Zeilen)
│   ├── Storage.java (109 Zeilen)
│   ├── VideoCard.java (104 Zeilen)
│   ├── Case.java (92 Zeilen)
│   ├── PowerSupply.java (92 Zeilen)
│   └── PCBuild.java (195 Zeilen)
│
├── data/ (362 Zeilen)
│   ├── DataLoader.java (112 Zeilen)
│   └── PartRepository.java (250 Zeilen)
│
├── business/ (204 Zeilen)
│   └── CompatibilityChecker.java (204 Zeilen)
│
├── ui/ (795 Zeilen)
│   ├── MainMenu.java (222 Zeilen)
│   ├── PartBrowserUI.java (217 Zeilen)
│   └── BuildConfiguratorUI.java (356 Zeilen)
│
└── util/ (248 Zeilen)
    ├── ConsoleHelper.java (157 Zeilen)
    └── InputValidator.java (91 Zeilen)

GESAMT: 2.713 Zeilen über 19 Klassen
```

## Datenfluss

```
     ┌──────────┐
     │  User    │
     └────┬─────┘
          │
          ▼
     ┌──────────────┐
     │  MainMenu    │
     └────┬─────────┘
          │
    ┌─────┴─────┐
    │           │
    ▼           ▼
┌───────┐   ┌────────────────┐
│Browser│   │  Configurator  │
└───┬───┘   └────┬───────────┘
    │            │
    └─────┬──────┘
          │
          ▼
    ┌──────────────┐
    │ Repository   │
    └──────┬───────┘
           │
           ▼
    ┌──────────────┐
    │  DataLoader  │
    └──────┬───────┘
           │
           ▼
    ┌──────────────┐
    │  JSON Files  │
    │  or Sample   │
    │     Data     │
    └──────────────┘
```

## Wichtige Beziehungen

1. **MainMenu** orchestriert alle UI-Komponenten
2. **PartRepository** ist zentrale Datenquelle für alle UI-Klassen
3. **PCBuild** wird zwischen UI-Komponenten geteilt
4. **CompatibilityChecker** arbeitet mit PCBuild und Model-Klassen
5. **Utility-Klassen** werden von allen UI-Komponenten verwendet

## Design Prinzipien

- **Single Responsibility**: Jede Klasse hat eine klare Aufgabe
- **Open/Closed**: PCPart ist erweiterbar durch Vererbung
- **Dependency Inversion**: UI hängt von Abstraktionen ab
- **Separation of Concerns**: Model, Data, Business, UI getrennt
