package de.dhbw.tinf23b3.pcpartpicker.model;

/**
 * Case component
 */
public class Case extends PCPart {
    private String caseType;
    private String color;
    private String sidePanel;
    private String externalVolume;
    private String internalBays;

    public Case() {
        super();
        this.type = "Case";
    }

    public Case(String name, Double price) {
        super(name, price, "Case");
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSidePanel() {
        return sidePanel;
    }

    public void setSidePanel(String sidePanel) {
        this.sidePanel = sidePanel;
    }

    public String getExternalVolume() {
        return externalVolume;
    }

    public void setExternalVolume(String externalVolume) {
        this.externalVolume = externalVolume;
    }

    public String getInternalBays() {
        return internalBays;
    }

    public void setInternalBays(String internalBays) {
        this.internalBays = internalBays;
    }

    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Case Details ===\n");
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Price: %s\n", getFormattedPrice()));
        if (caseType != null) {
            sb.append(String.format("Type: %s\n", caseType));
        }
        if (color != null) {
            sb.append(String.format("Color: %s\n", color));
        }
        if (sidePanel != null) {
            sb.append(String.format("Side Panel: %s\n", sidePanel));
        }
        if (externalVolume != null) {
            sb.append(String.format("Volume: %s L\n", externalVolume));
        }
        if (internalBays != null) {
            sb.append(String.format("Drive Bays: %s\n", internalBays));
        }
        return sb.toString();
    }

    @Override
    public String getShortDescription() {
        String typeInfo = caseType != null ? caseType : "Unknown";
        String colorInfo = color != null ? color : "";
        return String.format("%s | %s %s | %s", name, typeInfo, colorInfo, getFormattedPrice());
    }
}
