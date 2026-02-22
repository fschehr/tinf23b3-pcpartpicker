package de.ase.pcpartpicker.domain.models;

import java.util.ArrayList;
import java.util.List; 

public class PCConfiguration {
    private final List<Component> selectedComponents = new ArrayList<>(); 


    public void addComponent(Component component) {
        selectedComponents.add(component); 
    }

    public List<Component> getComponents() {
        return selectedComponents;
    }

    public boolean isEmpty() {
        return selectedComponents.isEmpty();
    }

    public long getTotalPrice() {
        long totalPrice = 0; 
        for(Component comp: selectedComponents) {
            totalPrice += comp.getPriceInCents();
        }
        return totalPrice; 
    }
}