# PC Part Picker - Projektdokumentation

## Übersicht

Ein terminal-basierter PC Part Picker in Java, entwickelt für das Advanced Software Engineering Projekt an der DHBW TINF23B3.

## Anforderungen

Das Projekt erfüllt folgende Anforderungen:
- ✅ Java-Projekt mit **19 Klassen**
- ✅ Umfang von **2.713 Zeilen Code** (Ziel: ~2000 Zeilen)
- ✅ Terminal-basierte Anwendung
- ✅ Integration mit PC Part Dataset (https://github.com/docyx/pc-part-dataset)

## Architektur

### Package-Struktur

```
de.dhbw.tinf23b3.pcpartpicker/
├── model/              # Datenmodelle (10 Klassen)
│   ├── PCPart.java     # Abstrakte Basisklasse
│   ├── CPU.java
│   ├── CPUCooler.java
│   ├── Motherboard.java
│   ├── Memory.java
│   ├── Storage.java
│   ├── VideoCard.java
│   ├── Case.java
│   ├── PowerSupply.java
│   └── PCBuild.java
├── data/               # Datenzugriff (2 Klassen)
│   ├── DataLoader.java
│   └── PartRepository.java
├── business/           # Geschäftslogik (1 Klasse)
│   └── CompatibilityChecker.java
├── ui/                 # Benutzeroberfläche (3 Klassen)
│   ├── MainMenu.java
│   ├── PartBrowserUI.java
│   └── BuildConfiguratorUI.java
├── util/               # Hilfsfunktionen (2 Klassen)
│   ├── ConsoleHelper.java
│   └── InputValidator.java
└── Main.java           # Haupteinstieg (1 Klasse)
```

## Funktionsumfang

### 1. Model Layer (10 Klassen)
- **PCPart**: Abstrakte Basisklasse für alle PC-Komponenten
  - Gemeinsame Attribute: Name, Preis, Typ
  - Abstrakte Methode für detaillierte Beschreibung
  - Hilfsmethoden für Preisformatierung

- **Komponentenklassen** (CPU, Motherboard, Memory, etc.):
  - Spezifische Attribute pro Komponententyp
  - Implementierung der Detailansicht
  - Getter/Setter für alle Attribute

- **PCBuild**: Verwaltung einer kompletten PC-Konfiguration
  - Speichert alle ausgewählten Komponenten
  - Berechnet Gesamtpreis
  - Prüft Vollständigkeit der Konfiguration
  - Generiert Build-Zusammenfassung

### 2. Data Access Layer (2 Klassen)
- **DataLoader**: 
  - Lädt PC-Komponenten aus JSON-Dateien
  - Verwendet Gson für JSON-Parsing
  - Fehlerbehandlung bei fehlenden Dateien

- **PartRepository**:
  - Zentrale Verwaltung aller Komponenten
  - Such- und Filterfunktionen
  - Sortierung nach Preis
  - Generierung von Beispieldaten

### 3. Business Logic Layer (1 Klasse)
- **CompatibilityChecker**:
  - Prüfung CPU/Motherboard Socket-Kompatibilität
  - Validierung RAM-Kapazität vs. Motherboard
  - Case/Motherboard Form Factor-Kompatibilität
  - Netzteil-Leistungsberechnung
  - Stromverbrauchsschätzung
  - Liste von Kompatibilitätswarnungen

### 4. UI Layer (3 Klassen)
- **MainMenu**:
  - Hauptnavigation der Anwendung
  - Build-Status-Anzeige
  - Menüführung

- **PartBrowserUI**:
  - Durchsuchen aller Komponenten
  - Pagination (15 Items pro Seite)
  - Such- und Filterfunktionen
  - Detailansicht einzelner Teile

- **BuildConfiguratorUI**:
  - Auswahl von Komponenten für Build
  - Anzeige aktueller Konfiguration
  - Hinzufügen/Entfernen von Komponenten
  - Pagination für große Komponentenlisten

### 5. Utility Layer (2 Klassen)
- **ConsoleHelper**:
  - Formatierte Konsolenausgabe
  - Header und Trennlinien
  - Tabellen und Listen
  - Fehler-/Erfolgs-/Warnmeldungen
  - Pagination-Info

- **InputValidator**:
  - Validierung von Benutzereingaben
  - Zahlen mit Min/Max-Bereich
  - Ja/Nein-Eingaben
  - String-Eingaben
  - Menüauswahl

## Technische Details

### Verwendete Technologien
- **Java**: Version 17
- **Build-Tool**: Maven 3.x
- **JSON-Library**: Gson 2.10.1
- **Testing**: JUnit 5

### Design Patterns
1. **Repository Pattern**: PartRepository für Datenzugriff
2. **Factory Pattern**: Sample-Daten-Generierung
3. **Strategy Pattern**: Verschiedene UI-Komponenten
4. **Template Method**: PCPart mit abstrakter Beschreibung

### Besondere Features
1. **Pagination**: Alle Listen sind paginiert für bessere Übersicht
2. **Sample Data**: Funktioniert auch ohne externe Datenbankdateien
3. **Kompatibilitätsprüfung**: Intelligente Erkennung von Inkompatibilitäten
4. **Power Calculation**: Schätzung des Strombedarfs
5. **Price Tracking**: Automatische Preisberechnung des Builds

## Code-Metriken

- **Gesamtzeilen**: 2.713 Zeilen Java-Code
- **Klassen**: 19 Klassen
- **Packages**: 5 Packages
- **Durchschnittliche Klassengröße**: ~143 Zeilen
- **Größte Klasse**: BuildConfiguratorUI (421 Zeilen)
- **Kleinste Klasse**: Main (33 Zeilen)

## Build & Ausführung

### Kompilierung
```bash
mvn clean compile
```

### Ausführung
```bash
mvn exec:java -Dexec.mainClass="de.dhbw.tinf23b3.pcpartpicker.Main"
```

### JAR erstellen
```bash
mvn clean package
java -jar target/pcpartpicker-1.0-SNAPSHOT.jar
```

## Beispiel-Workflow

1. **Start**: Anwendung zeigt Willkommensnachricht
2. **Hauptmenü**: Navigation zu verschiedenen Funktionen
3. **Browse Parts**: Durchsuchen von Komponenten nach Kategorie
4. **Configure Build**: Auswahl von Komponenten
5. **Compatibility Check**: Prüfung der Kompatibilität
6. **Build Summary**: Anzeige der Zusammenfassung mit Preis

## Erweiterungsmöglichkeiten

1. **Datenbankanbindung**: SQLite für persistente Speicherung
2. **Export-Funktion**: Builds als JSON/CSV exportieren
3. **Mehr Komponenten**: Monitore, Peripherie, etc.
4. **Performance-Vergleich**: Benchmark-Daten integrieren
5. **Preisverlauf**: Historische Preisdaten tracken
6. **Build-Vorlagen**: Vorkonfigurierte Builds (Gaming, Office, etc.)

## Fazit

Das Projekt erfüllt alle Anforderungen:
- ✅ Terminal-basierte Anwendung
- ✅ 19 Klassen mit klarer Struktur
- ✅ 2.713 Zeilen gut strukturierter Code
- ✅ Integration mit PC Part Dataset
- ✅ Umfassende Funktionalität
- ✅ Professionelle Code-Qualität
- ✅ Gute Erweiterbarkeit
