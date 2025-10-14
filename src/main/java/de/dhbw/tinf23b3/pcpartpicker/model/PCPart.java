package de.dhbw.tinf23b3.pcpartpicker.model;

/**
 * Abstract base class for all PC parts
 */
public abstract class PCPart {
    protected String name;
    protected Double price;
    protected String type;

    public PCPart() {
    }

    public PCPart(String name, Double price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean hasPrice() {
        return price != null;
    }

    public String getFormattedPrice() {
        if (price == null) {
            return "N/A";
        }
        return String.format("$%.2f", price);
    }

    /**
     * Get a detailed description of the part
     */
    public abstract String getDetailedDescription();

    /**
     * Get a brief one-line description
     */
    public String getShortDescription() {
        return String.format("%s - %s", name, getFormattedPrice());
    }

    @Override
    public String toString() {
        return getShortDescription();
    }
}
