# kleine Übersicht für diesen ordner

Hier sind die Klassen und Verantwortlichkeiten des CLI-Adapters (`adapters.cli`).

## AppContext
- Kapselt gemeinsam genutzte Objekte der CLI-App:
	- `InputReader`, `DatabaseInitializer`
	- Repositories: `UserRepository`, `ComputerRepository`
	- `ComputerDraft`, `ListConfiguration`

## InputReader
- Liest Konsoleneingaben: Integer, Strings und Warte-Eingaben (`waitForEnter`).

## Menüsystem (Menu / MenuItem / MenuFactory / IMenuComponent)
- Kleines, wiederverwendbares Menü-Framework für die CLI.
- `Menu`: loop-basierte Anzeige, Auswahl-Handling, unterstützt `NavMode` (STANDARD, PAGING).
- `MenuItem`: einzelne Aktion mit Sichtbarkeitssteuerung.
- `MenuFactory`: Hilfsklasse zum Zusammenbauen von Menüs.
- `IMenuComponent`: gemeinsames Interface für Menüpunkte.

## Darstellung / Utilities (Renderable / TableGenerator / Table)
- `Renderable`: Schnittstelle für darstellbare Inhalte im CLI.
- `TableGenerator`: Formatiert Tabellen (Header, Spaltenbreiten, farbige Zeilen).

## SessionManager
- Verwaltet aktuelle Sitzung und angemeldeten `User` (Login-Status, aktueller Nutzer).

## ComputerDraft
- Zwischenzustand beim Erstellen/Bearbeiten eines Computers.
- Hält `Computer.Builder`, aktuell gewählte Komponenten (CPU, GPU, Mainboard, RAM, PSU, Case, Storage), plus Utility-Methoden (Gesamtpreis, Gesamtverbrauch).

## ListConfiguration / rListConfiguration
- Einstellungen für Listendarstellungen (Paging, Spalten, Standardansichten).

## Sonstiges
- CLI-Basiskomponenten (`InputReader`, `TableGenerator`, Menüsystem) sind für alle Interaktionen wiederverwendbar.
