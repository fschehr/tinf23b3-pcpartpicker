package de.dhbw.tinf23b3.pcpartpicker.model;

/**
 * Power Supply Unit (PSU) component
 */
public class PowerSupply extends PCPart {
    private String formFactor;
    private String efficiencyRating;
    private Integer wattage;
    private String modular;
    private String color;

    public PowerSupply() {
        super();
        this.type = "Power Supply";
    }

    public PowerSupply(String name, Double price) {
        super(name, price, "Power Supply");
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public String getEfficiencyRating() {
        return efficiencyRating;
    }

    public void setEfficiencyRating(String efficiencyRating) {
        this.efficiencyRating = efficiencyRating;
    }

    public Integer getWattage() {
        return wattage;
    }

    public void setWattage(Integer wattage) {
        this.wattage = wattage;
    }

    public String getModular() {
        return modular;
    }

    public void setModular(String modular) {
        this.modular = modular;
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
        sb.append("=== Power Supply Details ===\n");
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Price: %s\n", getFormattedPrice()));
        if (wattage != null) {
            sb.append(String.format("Wattage: %d W\n", wattage));
        }
        if (efficiencyRating != null) {
            sb.append(String.format("Efficiency: %s\n", efficiencyRating));
        }
        if (modular != null) {
            sb.append(String.format("Modular: %s\n", modular));
        }
        if (formFactor != null) {
            sb.append(String.format("Form Factor: %s\n", formFactor));
        }
        if (color != null) {
            sb.append(String.format("Color: %s\n", color));
        }
        return sb.toString();
    }

    @Override
    public String getShortDescription() {
        String wattageInfo = wattage != null ? wattage + "W" : "Unknown";
        String efficiency = efficiencyRating != null ? efficiencyRating : "";
        return String.format("%s | %s %s | %s", name, wattageInfo, efficiency, getFormattedPrice());
    }
}
