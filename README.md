# PC Part Picker - Terminal Edition

Advanced SE Projekt für DHBW TINF23B3

## Projektbeschreibung

Ein terminal-basierter PC Part Picker in Java, der es Nutzern ermöglicht, individuelle PC-Konfigurationen zusammenzustellen und auf Kompatibilität zu überprüfen.

## Features

- **Komponenten-Browser**: Durchsuchen von über 60.000 PC-Komponenten
- **Build-Konfigurator**: Zusammenstellen von PC-Builds
- **Kompatibilitätsprüfung**: Automatische Überprüfung der Komponenten-Kompatibilität
- **Preisberechnung**: Automatische Berechnung der Gesamtkosten
- **Leistungsabschätzung**: Schätzung des Stromverbrauchs
- **Filteroptionen**: Filtern nach Preis, Hersteller und Spezifikationen

## Technische Details

- **Programmiersprache**: Java 17
- **Build-Tool**: Maven
- **JSON-Verarbeitung**: Gson
- **Datenquelle**: PC Part Dataset (https://github.com/docyx/pc-part-dataset)

## Projektstruktur

```
src/main/java/de/dhbw/tinf23b3/pcpartpicker/
├── model/          # Datenmodelle (PCPart, CPU, Motherboard, etc.)
├── data/           # Datenzugriff (DataLoader, PartRepository)
├── business/       # Business-Logik (CompatibilityChecker)
├── ui/             # Benutzeroberfläche (MainMenu, Browser, Configurator)
├── util/           # Hilfsfunktionen (ConsoleHelper, InputValidator)
└── Main.java       # Haupteinstiegspunkt
```

## Klassenhierarchie

Das Projekt umfasst 20+ Klassen:

### Model (8 Klassen)
- `PCPart` (Abstract Base Class)
- `CPU`
- `Motherboard`
- `Memory`
- `Storage`
- `VideoCard`
- `Case`
- `PowerSupply`
- `CPUCooler`
- `PCBuild`

### Data Access (2 Klassen)
- `DataLoader`
- `PartRepository`

### Business Logic (1 Klasse)
- `CompatibilityChecker`

### UI (3 Klassen)
- `MainMenu`
- `PartBrowserUI`
- `BuildConfiguratorUI`

### Utilities (2 Klassen)
- `ConsoleHelper`
- `InputValidator`

### Main (1 Klasse)
- `Main`

## Installation & Ausführung

### Voraussetzungen
- Java 17 oder höher
- Maven 3.6+

### Build
```bash
mvn clean compile
```

### Ausführung
```bash
mvn exec:java -Dexec.mainClass="de.dhbw.tinf23b3.pcpartpicker.Main"
```

Oder nach dem Build:
```bash
java -cp target/classes de.dhbw.tinf23b3.pcpartpicker.Main
```

### Package als JAR
```bash
mvn clean package
java -jar target/pcpartpicker-1.0-SNAPSHOT.jar
```

## Verwendung

1. **Hauptmenü**: Navigation durch die verschiedenen Funktionen
2. **Build Configurator**: Auswahl von Komponenten für den PC-Build
3. **Part Browser**: Durchsuchen aller verfügbaren Komponenten
4. **Compatibility Check**: Überprüfung der Kompatibilität der gewählten Komponenten
5. **Build Summary**: Anzeige der kompletten Build-Zusammenfassung mit Gesamtpreis

## Code-Statistiken

- **Gesamtzahl der Klassen**: 20+
- **Geschätzte Codezeilen**: ~2000+
- **Packages**: 5

## Datenquelle

Die PC-Komponenten-Daten stammen aus dem PC Part Dataset:
https://github.com/docyx/pc-part-dataset

Das Dataset enthält über 66.000 PC-Teile von PCPartPicker.

## Entwickler

DHBW TINF23B3 - Advanced Software Engineering Projekt