package de.ase.pcpartpicker.adapters.cli.utils;

import de.ase.pcpartpicker.ColorConstants;

public class ExceptionUtils {
    
    public static void printError(String message) {
        System.out.println(ColorConstants.RED("ERROR") + " | " + message);
    }

    public static void printWarning(String message) {
        System.out.println(ColorConstants.YELLOW("WARNING") + " | " + message);      
    }


    public static void printInfo(String message) {
        System.out.println(ColorConstants.BLUE("INFO") + " | " + message);
    }

}
