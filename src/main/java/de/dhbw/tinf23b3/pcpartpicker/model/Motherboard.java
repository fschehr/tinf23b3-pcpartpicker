package de.dhbw.tinf23b3.pcpartpicker.model;

/**
 * Motherboard component
 */
public class Motherboard extends PCPart {
    private String socket;
    private String formFactor;
    private Integer maxMemory;
    private Integer memorySlots;
    private String color;

    public Motherboard() {
        super();
        this.type = "Motherboard";
    }

    public Motherboard(String name, Double price) {
        super(name, price, "Motherboard");
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public Integer getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(Integer maxMemory) {
        this.maxMemory = maxMemory;
    }

    public Integer getMemorySlots() {
        return memorySlots;
    }

    public void setMemorySlots(Integer memorySlots) {
        this.memorySlots = memorySlots;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Motherboard Details ===\n");
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Price: %s\n", getFormattedPrice()));
        if (socket != null) {
            sb.append(String.format("Socket: %s\n", socket));
        }
        if (formFactor != null) {
            sb.append(String.format("Form Factor: %s\n", formFactor));
        }
        if (maxMemory != null) {
            sb.append(String.format("Max Memory: %d GB\n", maxMemory));
        }
        if (memorySlots != null) {
            sb.append(String.format("Memory Slots: %d\n", memorySlots));
        }
        if (color != null) {
            sb.append(String.format("Color: %s\n", color));
        }
        return sb.toString();
    }

    @Override
    public String getShortDescription() {
        String socketInfo = socket != null ? socket : "Unknown Socket";
        String formFactorInfo = formFactor != null ? formFactor : "";
        return String.format("%s | %s %s | %s", name, socketInfo, formFactorInfo, getFormattedPrice());
    }
}
