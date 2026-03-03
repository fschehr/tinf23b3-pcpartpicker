package de.ase.pcpartpicker.domain.services;

import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;

import java.util.ArrayList;
import java.util.List;



public class ComponentService {
    private final List<CPU> cpus = new ArrayList<>();


    public ComponentService() {
        cpus.add(new CPU(1, "intel Core i7-13700K", 299.99, new Manufacturer(2, "Intel"), new Socket(1, "LGA1700"), 16));
        cpus.add(new CPU(2, "AMD Ryzen 9 7950X", 54500, new Manufacturer(1, "AMD"), new Socket(2, "AM5"), 16));
        cpus.add(new CPU(3, "Intel Core i5-13600K", 31900, new Manufacturer(2, "Intel"), new Socket(1, "LGA1700"), 14));
    }

    public List<CPU> getCPUs() {
        return cpus; 
    }
}
