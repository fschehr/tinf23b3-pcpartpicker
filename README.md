# PC Part Picker - Programmentwurf (TINF23)

Dieses Projekt wurde im Rahmen des Moduls Programmentwurf an der DHBW entwickelt. Es handelt sich um eine Java-basierte Konsolenanwendung zur Konfiguration von PC-Systemen unter Berücksichtigung von Kompatibilität, Leistung und Kosten.

## 1. Übersicht & Funktionalität
Die Anwendung bietet eine strukturierte Lösung für die Auswahl von PC-Komponenten:
* **Kompatibilitätsprüfung**: Validierung von Sockeln, Formfaktoren und TDP in Echtzeit.
* **Bottleneck-Checker**: Analyse von Leistungsengpässen für spezifische Anwendungsfälle (z.B. 4K Gaming).
* **Preis-Kalkulation**: Ermittlung des günstigsten Gesamtangebots und Prognose der Betriebskosten.
* **Persistenz**: Speicherung und Laden von Konfigurationen.

## 2. Voraussetzungen & Start
Die Anwendung ist für den Betrieb unter Linux (getestet auf Fedora) optimiert.
* **Java Version**: 17 oder höher.
* **Build-Tool**: Maven.
* **Codierung**: Alles ist strikt in UTF-8 codiert.
* **Benutzeroberfläche**: Rein textbasierte Ausgabe (keine GUI).

### Startanleitung
1. Navigieren Sie in das Projektverzeichnis: `cd tinf23b3-pcpartpicker`
2. Kompilieren und Testen: `mvn clean install`.
3. Starten der Anwendung: `mvn exec:java -Dexec.main