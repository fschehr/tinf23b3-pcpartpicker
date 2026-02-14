package de.ase.pcpartpicker.adapters.cli;

import java.util.Scanner;

public class InputReader {
    private final Scanner scanner; 

    public InputReader() {
        this.scanner = new Scanner(System.in, "UTF-8"); 
    }


    public int readInt(String prompt, int min, int max) {
        int input; 
        while(true){
            System.out.print(prompt + " (" + min + "-" + max + "): ");
            try{
                input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) return input;
                System.out.println("Fehler: Bitte eine Zahl zwischen " + min +" und " + max + " eingeben."); 
            }
            catch (NumberFormatException e){
                System.out.println("Fehler: Ungültige Eingabe. Bitte eine Zahl eingeben.");
            }
        }
    }
}