package de.dhbw.tinf23b3.pcpartpicker.model;

/**
 * CPU Cooler component
 */
public class CPUCooler extends PCPart {
    private Integer[] rpm; // [min, max] RPM range
    private Object noiseLevel; // Can be single value or [min, max] range
    private String color;
    private Integer size; // Radiator size in mm

    public CPUCooler() {
        super();
        this.type = "CPU Cooler";
    }

    public CPUCooler(String name, Double price) {
        super(name, price, "CPU Cooler");
    }

    public Integer[] getRpm() {
        return rpm;
    }

    public void setRpm(Integer[] rpm) {
        this.rpm = rpm;
    }

    public Object getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(Object noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CPU Cooler Details ===\n");
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Price: %s\n", getFormattedPrice()));
        if (size != null) {
            sb.append(String.format("Radiator Size: %d mm\n", size));
        }
        if (rpm != null && rpm.length == 2) {
            sb.append(String.format("Fan Speed: %d-%d RPM\n", rpm[0], rpm[1]));
        }
        if (noiseLevel != null) {
            sb.append(String.format("Noise Level: %s dB\n", noiseLevel.toString()));
        }
        if (color != null) {
            sb.append(String.format("Color: %s\n", color));
        }
        return sb.toString();
    }

    @Override
    public String getShortDescription() {
        String sizeInfo = size != null ? size + "mm" : "Air Cooler";
        return String.format("%s | %s | %s", name, sizeInfo, getFormattedPrice());
    }
}
