package de.ase.pcpartpicker.domain.services;

import de.ase.pcpartpicker.domain.models.CPU;
import java.util.ArrayList;
import java.util.List;



public class ComponentService {
    private final List<CPU> cpus = new ArrayList<>();


    public ComponentService() {
        cpus.add(new CPU("C1", "Intel Core i9-13900K", 58900, "Intel", 125, "LGA1700", 24));
        cpus.add(new CPU("C2", "AMD Ryzen 9 7950X", 54500, "AMD", 170, "AM5", 16));
        cpus.add(new CPU("C3", "Intel Core i5-13600K", 31900, "Intel", 125, "LGA1700", 14));
    }

    public List<CPU> getCPUs() {
        return cpus; 
    }
}
