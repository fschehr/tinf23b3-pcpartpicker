package de.dhbw.tinf23b3.pcpartpicker.model;

/**
 * Storage (Hard Drive/SSD) component
 */
public class Storage extends PCPart {
    private Integer capacity;
    private Double pricePerGb;
    private String storageType; // "SSD" or RPM value for HDD
    private Integer cache;
    private String formFactor;
    private String interfaceType;

    public Storage() {
        super();
        this.type = "Storage";
    }

    public Storage(String name, Double price) {
        super(name, price, "Storage");
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getPricePerGb() {
        return pricePerGb;
    }

    public void setPricePerGb(Double pricePerGb) {
        this.pricePerGb = pricePerGb;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public Integer getCache() {
        return cache;
    }

    public void setCache(Integer cache) {
        this.cache = cache;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public boolean isSSD() {
        return storageType != null && storageType.equalsIgnoreCase("SSD");
    }

    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Storage Details ===\n");
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Price: %s\n", getFormattedPrice()));
        if (capacity != null) {
            sb.append(String.format("Capacity: %d GB\n", capacity));
        }
        if (storageType != null) {
            sb.append(String.format("Type: %s\n", storageType));
        }
        if (formFactor != null) {
            sb.append(String.format("Form Factor: %s\n", formFactor));
        }
        if (interfaceType != null) {
            sb.append(String.format("Interface: %s\n", interfaceType));
        }
        if (cache != null) {
            sb.append(String.format("Cache: %d MB\n", cache));
        }
        if (pricePerGb != null) {
            sb.append(String.format("Price per GB: $%.4f\n", pricePerGb));
        }
        return sb.toString();
    }

    @Override
    public String getShortDescription() {
        String capacityInfo = capacity != null ? (capacity >= 1000 ? 
            (capacity / 1000) + "TB" : capacity + "GB") : "Unknown";
        String typeInfo = storageType != null ? storageType : "Unknown Type";
        return String.format("%s | %s %s | %s", name, capacityInfo, typeInfo, getFormattedPrice());
    }
}
