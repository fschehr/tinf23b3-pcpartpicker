package de.ase.pcpartpicker;

import java.io.Console;

import de.ase.pcpartpicker.adapters.cli.*;

public class Main {
    public static void main(String[] args) {
        ConsoleMenu menu = new ConsoleMenu();
        menu.start();
    }
}