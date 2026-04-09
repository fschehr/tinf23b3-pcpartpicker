# PC Part Picker - Programmentwurf (TINF23)

Dieses Projekt wurde im Rahmen des Moduls Programmentwurf an der DHBW entwickelt. Es handelt sich um eine Java-basierte Konsolenanwendung zur Konfiguration von PC-Systemen unter Berücksichtigung von Kompatibilität, Leistung und Kosten.

## Übersicht & Funktionalität
Die Anwendung bietet eine strukturierte Lösung für die Auswahl von PC-Komponenten:
* **Kompatibilitätsprüfung**: Validierung von Sockeln, Formfaktoren und TDP in Echtzeit.
* **Bottleneck-Checker**: Analyse von Leistungsengpässen.
* **Preis-Kalkulation**: Ermittlung des günstigsten Gesamtangebots und Prognose der Betriebskosten.
* **Persistenz**: Speicherung und Laden von Konfigurationen.

## Start
```sh
git clone https://github.com/fschehr/tinf23b3-pcpartpicker.git
cd tinf23b3-pcpartpicker
git switch endprodukt
java -jar pc-konfigurator.jar
```
