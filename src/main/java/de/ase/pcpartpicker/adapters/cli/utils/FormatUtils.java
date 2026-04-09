package de.ase.pcpartpicker.adapters.cli.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {
    
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.GERMANY); 
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.GERMANY); 


    public static String formatPrice(double price) {
        return CURRENCY_FORMAT.format(price);
    }

    public static String formatNumber(int number) {
        return NUMBER_FORMAT.format(number); 
    }

    public static String formatNumer(double number) {
        return NUMBER_FORMAT.format(number); 
    }


}
