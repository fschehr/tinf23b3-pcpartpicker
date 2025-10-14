package de.dhbw.tinf23b3.pcpartpicker.model;

/**
 * Memory (RAM) component
 */
public class Memory extends PCPart {
    private int[] speed; // [DDR version, RAM speed in MHz]
    private int[] modules; // [number of modules, size of each module in GB]
    private Double pricePerGb;
    private String color;
    private Double firstWordLatency;
    private Integer casLatency;

    public Memory() {
        super();
        this.type = "Memory";
    }

    public Memory(String name, Double price) {
        super(name, price, "Memory");
    }

    public int[] getSpeed() {
        return speed;
    }

    public void setSpeed(int[] speed) {
        this.speed = speed;
    }

    public int[] getModules() {
        return modules;
    }

    public void setModules(int[] modules) {
        this.modules = modules;
    }

    public Double getPricePerGb() {
        return pricePerGb;
    }

    public void setPricePerGb(Double pricePerGb) {
        this.pricePerGb = pricePerGb;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getFirstWordLatency() {
        return firstWordLatency;
    }

    public void setFirstWordLatency(Double firstWordLatency) {
        this.firstWordLatency = firstWordLatency;
    }

    public Integer getCasLatency() {
        return casLatency;
    }

    public void setCasLatency(Integer casLatency) {
        this.casLatency = casLatency;
    }

    public int getTotalCapacity() {
        if (modules != null && modules.length == 2) {
            return modules[0] * modules[1];
        }
        return 0;
    }

    public String getMemoryType() {
        if (speed != null && speed.length >= 1) {
            return "DDR" + speed[0];
        }
        return "Unknown";
    }

    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Memory Details ===\n");
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Price: %s\n", getFormattedPrice()));
        if (modules != null && modules.length == 2) {
            sb.append(String.format("Configuration: %dx%dGB (%dGB Total)\n", 
                modules[0], modules[1], getTotalCapacity()));
        }
        if (speed != null && speed.length >= 2) {
            sb.append(String.format("Type: DDR%d-%d\n", speed[0], speed[1]));
        }
        if (casLatency != null) {
            sb.append(String.format("CAS Latency: %d\n", casLatency));
        }
        if (firstWordLatency != null) {
            sb.append(String.format("First Word Latency: %.2f ns\n", firstWordLatency));
        }
        if (pricePerGb != null) {
            sb.append(String.format("Price per GB: $%.2f\n", pricePerGb));
        }
        if (color != null) {
            sb.append(String.format("Color: %s\n", color));
        }
        return sb.toString();
    }

    @Override
    public String getShortDescription() {
        String capacity = modules != null && modules.length == 2 ? 
            String.format("%dx%dGB", modules[0], modules[1]) : "Unknown";
        String memType = getMemoryType();
        return String.format("%s | %s %s | %s", name, capacity, memType, getFormattedPrice());
    }
}
