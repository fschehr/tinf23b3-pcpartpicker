# kleine Übersicht für diesen ordner
Hier sind die Klassen der PC-Komponenten definiert.

Es wird von der abstrakten Klasse Component.java geerbt. Diese gibt folgendes vor:
- preis (double)
- name (str)
- id (int)

Der Rest wird dann in den einzelnen Komponentenklassen ergänzt.

## cpu
- sockel (str)
- Taktfrequenz [GHz] (double)
## gpu
- vram [GB] (int)
## ram
- größe [GB] (int)
- Taktfrequenz [MHz] (int)
## Mainboard
- sockel (str)
- formfaktor (str)