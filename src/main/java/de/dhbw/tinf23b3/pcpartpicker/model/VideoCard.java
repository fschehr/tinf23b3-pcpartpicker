package de.dhbw.tinf23b3.pcpartpicker.model;

/**
 * Video Card (GPU) component
 */
public class VideoCard extends PCPart {
    private String chipset;
    private Integer memory;
    private Integer coreClock;
    private Integer boostClock;
    private String color;
    private Integer length;

    public VideoCard() {
        super();
        this.type = "Video Card";
    }

    public VideoCard(String name, Double price) {
        super(name, price, "Video Card");
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getCoreClock() {
        return coreClock;
    }

    public void setCoreClock(Integer coreClock) {
        this.coreClock = coreClock;
    }

    public Integer getBoostClock() {
        return boostClock;
    }

    public void setBoostClock(Integer boostClock) {
        this.boostClock = boostClock;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Video Card Details ===\n");
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Price: %s\n", getFormattedPrice()));
        if (chipset != null) {
            sb.append(String.format("Chipset: %s\n", chipset));
        }
        if (memory != null) {
            sb.append(String.format("Memory: %d GB\n", memory));
        }
        if (coreClock != null) {
            sb.append(String.format("Core Clock: %d MHz\n", coreClock));
        }
        if (boostClock != null) {
            sb.append(String.format("Boost Clock: %d MHz\n", boostClock));
        }
        if (length != null) {
            sb.append(String.format("Length: %d mm\n", length));
        }
        if (color != null) {
            sb.append(String.format("Color: %s\n", color));
        }
        return sb.toString();
    }

    @Override
    public String getShortDescription() {
        String memoryInfo = memory != null ? memory + "GB" : "Unknown";
        String chipsetInfo = chipset != null ? chipset : "";
        return String.format("%s | %s %s | %s", name, chipsetInfo, memoryInfo, getFormattedPrice());
    }
}
