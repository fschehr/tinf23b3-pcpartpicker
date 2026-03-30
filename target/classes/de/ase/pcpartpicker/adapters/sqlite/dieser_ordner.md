# kleine Übersicht für diesen ordner

## ConnectionFactory
- kümmert sich um DB Verbindungen

## DatabaseConfig
- konfiguriert die sqlite db (noch nix wirklich drin außer url)

## DatabaseInitializer
- erstellt die tabellen in der db falls noch nicht geschehen.

## /repositories/*
- Repositories für Komponenten sollen Daten über die jeweiligen Komponenten aus der db ziehen.
- Erstellung von Objekten.
- Interface ComponentRepository.java wird von abstrakter Klasse BaseRepository.java implementiert und davon erben Repos für die jeweiligen Komponenten. Siehe grafik
