# kleine Übersicht für den PC-Zusammenbau Ordner
Hier sind die Klassen definiert, die für den Zusammenbau erforderlich sind.

## [Computer](Computer.java)
Klasse die einen vollständig konfigurierten Computer abbildet.
- id
- computerCase ([Case](../domain/Case.java))
- cpu ([CPU](../domain/CPU.java))
- gpu ([GPU](../domain/GPU.java))
- mainboard ([Mainboard](../domain/Mainboard.java))
- ram ([RAM](../domain/RAM.java))
- ramModule (int) - Anzahl RAM Module
- psu ([PSU](../domain/PSU.java))
- storageDevices (List<[Storage](../domain/Storage.java)>) - Liste der installierten Speichermodule
### [Computer.Builder](Computer.java)
Computer enthält eine integrierte Builder Klasse, um den Computer zusammenzubauen.

Hier gibt es auch eine validate Funktion, die eine live Überprüfung der Kompatibilität ermöglicht.

hier ein Beispiel wie das aufgebaut werden könnte:
``` java
Computer computer = new Computer.Builder()
    .setCPU(cpu)
    .setGPU(gpu)
    .setMainboard(mainboard)
    .setRAM(ram, 5)
    .setPSU(psu)
    .setComputerCase(computerCase)
    .setStorageDevices({ssd, hdd})
    .build();
```

## [Config](Config.java)
Speichert die verschiedenen Konfigurationen
- id (int)
- user ([User](../domain/HelperClasses/User.java))
- computer ([Computer](./Computer.java))

## [Bottleneck](Bottleneck.java)
Methoden zur Berechnung von Bottlenecks in einem Computer
- bottleneck (bool) - bereits ein Bottleneck gefunden?
- oneComponent (bool) - Komponente zu schwach/stark?

## [BottleneckResult](BottleneckResult.java)
Record für das Bottleneck (Ergebnis)
- hasBottleneck (bool)
- bottleneckComponent ([Component](../domain/Component.java))
- isUnderperforming (bool)
- alternativeComponent ([Component](../domain/Component.java))
## [Performance](Performance.java)
Berechnung der Leistung eines Computers