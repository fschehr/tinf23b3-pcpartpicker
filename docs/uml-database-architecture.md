# UML – Datenbankarchitektur und Verbindung (SQLite)

```mermaid
classDiagram
    direction LR

    class ConnectionFactory {
      +createConnection() Connection
    }

    class DatabaseInitializer {
      -ConnectionFactory connectionFactory
      +initialize() void
    }

    class ComponentRepository~T extends Component~ {
      <<interface>>
      +findAll() List~T~
    }

    class BaseRepository~T extends Component~ {
      <<abstract>>
      #connectionFactory : ConnectionFactory
      +findAll() List~T~
    }

    class CpuRepository {
      +findAll() List~Cpu~
    }

    class GpuRepository {
      +findAll() List~Gpu~
    }

    class RamRepository {
      +findAll() List~Ram~
    }

    class MainboardRepository {
      +findAll() List~Mainboard~
    }

    class Component {
      <<abstract>>
      -id : int
      -name : String
      -price : double
      +getId() int
      +getName() String
      +getPrice() double
    }

    class Cpu {
      -socket : String
      -speedGhz : int
      +getSocket() String
      +getSpeedGhz() int
    }

    class Gpu {
      -vramGb : int
      +getVramGb() int
    }

    class Ram {
      -capacityGb : int
      -speedMhz : int
      +getCapacityGb() int
      +getSpeedMhz() int
    }

    class Mainboard {
      -socket : String
      -formFactor : String
      +getSocket() String
      +getFormFactor() String
    }

    ComponentRepository <|.. BaseRepository
    BaseRepository <|-- CpuRepository
    BaseRepository <|-- GpuRepository
    BaseRepository <|-- RamRepository
    BaseRepository <|-- MainboardRepository

    Component <|-- Cpu
    Component <|-- Gpu
    Component <|-- Ram
    Component <|-- Mainboard

    CpuRepository --> Cpu : maps ResultSet to
    GpuRepository --> Gpu : maps ResultSet to
    RamRepository --> Ram : maps ResultSet to
    MainboardRepository --> Mainboard : maps ResultSet to

    DatabaseInitializer --> ConnectionFactory : uses
    BaseRepository --> ConnectionFactory : uses
```

## Hinweis
- `ComponentRepository` definiert den Vertrag, `BaseRepository` liefert gemeinsame Infrastruktur.
- `ConnectionFactory` und `DatabaseInitializer` sind die gemeinsame DB-Infrastruktur.