# kleine Übersicht für den Domäne Ordner
Hier sind die Klassen der PC-Komponenten definiert.

## Component (abstrakt)
Basisklasse für alle Komponenten:
- id (int)
- name (String)
- price (double)
- manufacturer (Manufacturer)
- powerConsumptionW (int) - Maximaler Stromverbrauch in Watt

## CPU
- socket (Socket) - z.B. AM4, LGA1200
- speedGHz (double) - Basistaktfrequenz
- coreCount (int) - Anzahl der physischen Kerne
- boostClockGHz (Double) - optionale Boost-Frequenz
- hasIntegratedGraphics (boolean) - hat iGPU

## GPU
- coreClockMHz (double) - Core-Taktfrequenz
- boostClockMHz (Double) - optionale Boost-Frequenz
- vramGB (int) - VRAM-Kapazität

## RAM
- capacityGB (int) - Kapazität pro Modul
- speedMHz (int) - Taktfrequenz

## Mainboard
- socket (Socket) - Sockeltyp
- formFactor (MotherboardFormFactor) - z.B. ATX, Micro-ATX
- ramSlots (int) - Anzahl RAM-Slots
- pcieSlots (int) - Anzahl PCIe-Slots
- sataSlots (int) - Anzahl SATA-Anschlüsse
- m2Slots (int) - Anzahl M.2-Slots

## Case
- motherboardFormFactor (MotherboardFormFactor) - unterstützter Mobo-Formfaktor
- psuFormFactor (PSUFormFactor) - unterstützter PSU-Formfaktor
- hasWindow (boolean) - Fenster vorhanden
- fanSlots (int) - Anzahl Lüfterplätze

## PSU
- wattage (int) - Leistung in Watt
- formFactor (PSUFormFactor) - z.B. ATX, SFX

## Storage (abstrakt)
Basisklasse für Speichergeräte:
- capacityGB (int) - Kapazität in GB

### SSD
Solid State Drive via SATA

### M2SSD
NVMe M.2 Solid State Drive

### HDD
Hard Disk Drive - Festplatte

# ./HelperClasses
Hier werden Hilfsklassen definiert, die zur Struktur und Verbindung zwischen Objekten notwendig sind.

## HelperTable (abstrakt)
- id (int)
- name (String)

### Manufacturer
Hersteller

### MotherboardFormFactor
Formfaktor für Motherboards (Verbindung Case <-> Motherboard)

### PSUFormFactor
Formfaktor für Netzteile (Verbindung Case <-> Netzteil)

### Socket
Sockel von CPU (Verbindung Motherboard <-> CPU)

### User
Nutzertabelle (Verbindung Computer <-> User)