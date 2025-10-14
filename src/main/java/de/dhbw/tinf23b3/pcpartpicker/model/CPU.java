package de.dhbw.tinf23b3.pcpartpicker.model;

/**
 * CPU (Processor) component
 */
public class CPU extends PCPart {
    private Integer coreCount;
    private Double coreClock;
    private Double boostClock;
    private String microarchitecture;
    private Integer tdp;
    private String graphics;
    private Boolean smt;

    public CPU() {
        super();
        this.type = "CPU";
    }

    public CPU(String name, Double price) {
        super(name, price, "CPU");
    }

    public Integer getCoreCount() {
        return coreCount;
    }

    public void setCoreCount(Integer coreCount) {
        this.coreCount = coreCount;
    }

    public Double getCoreClock() {
        return coreClock;
    }

    public void setCoreClock(Double coreClock) {
        this.coreClock = coreClock;
    }

    public Double getBoostClock() {
        return boostClock;
    }

    public void setBoostClock(Double boostClock) {
        this.boostClock = boostClock;
    }

    public String getMicroarchitecture() {
        return microarchitecture;
    }

    public void setMicroarchitecture(String microarchitecture) {
        this.microarchitecture = microarchitecture;
    }

    public Integer getTdp() {
        return tdp;
    }

    public void setTdp(Integer tdp) {
        this.tdp = tdp;
    }

    public String getGraphics() {
        return graphics;
    }

    public void setGraphics(String graphics) {
        this.graphics = graphics;
    }

    public Boolean getSmt() {
        return smt;
    }

    public void setSmt(Boolean smt) {
        this.smt = smt;
    }

    public boolean hasIntegratedGraphics() {
        return graphics != null && !graphics.isEmpty();
    }

    @Override
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CPU Details ===\n");
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Price: %s\n", getFormattedPrice()));
        if (coreCount != null) {
            sb.append(String.format("Cores: %d\n", coreCount));
        }
        if (coreClock != null) {
            sb.append(String.format("Base Clock: %.1f GHz\n", coreClock));
        }
        if (boostClock != null) {
            sb.append(String.format("Boost Clock: %.1f GHz\n", boostClock));
        }
        if (microarchitecture != null) {
            sb.append(String.format("Architecture: %s\n", microarchitecture));
        }
        if (tdp != null) {
            sb.append(String.format("TDP: %d W\n", tdp));
        }
        if (graphics != null) {
            sb.append(String.format("Integrated Graphics: %s\n", graphics));
        }
        if (smt != null) {
            sb.append(String.format("SMT: %s\n", smt ? "Yes" : "No"));
        }
        return sb.toString();
    }

    @Override
    public String getShortDescription() {
        String cores = coreCount != null ? coreCount + "-Core" : "Unknown";
        String clock = coreClock != null ? String.format("%.1f GHz", coreClock) : "";
        return String.format("%s | %s %s | %s", name, cores, clock, getFormattedPrice());
    }
}
