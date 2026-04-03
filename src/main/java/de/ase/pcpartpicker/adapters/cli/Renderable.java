package de.ase.pcpartpicker.adapters.cli;

/**
 * Interface für beliebige renderbare CLI-Inhalte (Tabellen, Listen, etc.)
 */
public interface Renderable {
    void render(String menuTitle);
}