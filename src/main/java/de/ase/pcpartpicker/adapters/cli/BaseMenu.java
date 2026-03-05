package de.ase.pcpartpicker.adapters.cli;

public abstract class BaseMenu {
    protected boolean running = true; 

    public void start() {
        running = true;
        while(running) {
            UIUtils.clear();
            run();
        }
    }

    protected abstract void run(); 

    public void stop() {
        this.running = false; 
    }
}
