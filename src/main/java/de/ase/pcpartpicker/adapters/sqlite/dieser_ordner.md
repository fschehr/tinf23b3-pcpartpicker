# kleine Übersicht für diesen ordner

## ConnectionFactory
- kümmert sich um DB Verbindungen

## DatabaseConfig
- konfiguriert die sqlite db (noch nix wirklich drin außer url)

## DatabaseInitializer
- db reset funktion
    - lösche komplette DB
    - erstelle grundstruktur
    - parst .jsonl dateien und erstellt objekte
    - baut verbindungen


## /repositories/*
- Übersetzung db zu java objekte, damit backend damit arbeiten kann.
- mittels prepared statements
- Interface Repository.java implementiert ComponentRepository.java und JDBCRepository.java
    - Interface ComponentRepository implementiert BaseRepository.java
        - abstrakte Klasse BaseRepository.java: gemeinsame Zwischenklasse für alle Komponenten-Repositories
        - Cpu, GPU, RAM, HDD, M2Ssd, SSD, Case, Mainboard, PSU erben von BaseRepository 
    - abstrakte Klasse JDBCRepository.java: JDBC Logik
        - davon erben alle Repos

große Struktur ist hauptsächlich im Sinne der Erweiterbarkeit und semantischen Ordnung. 
Theoretisch ist ComponentRepository.java redundant und fügt nichts an Funktionalität hinzu, es ist als "Marker Interface" zu sehen.