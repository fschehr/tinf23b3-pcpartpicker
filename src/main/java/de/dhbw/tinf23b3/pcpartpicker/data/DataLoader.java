package de.dhbw.tinf23b3.pcpartpicker.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.dhbw.tinf23b3.pcpartpicker.model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading PC part data from JSON files
 */
public class DataLoader {
    private final Gson gson;
    private final String dataPath;

    public DataLoader() {
        this.gson = new GsonBuilder().create();
        this.dataPath = "/data/";
    }

    /**
     * Load CPUs from JSON file
     */
    public List<CPU> loadCPUs() {
        return loadData("cpu.json", new TypeToken<List<CPU>>() {}.getType());
    }

    /**
     * Load CPU Coolers from JSON file
     */
    public List<CPUCooler> loadCPUCoolers() {
        return loadData("cpu-cooler.json", new TypeToken<List<CPUCooler>>() {}.getType());
    }

    /**
     * Load Motherboards from JSON file
     */
    public List<Motherboard> loadMotherboards() {
        return loadData("motherboard.json", new TypeToken<List<Motherboard>>() {}.getType());
    }

    /**
     * Load Memory from JSON file
     */
    public List<Memory> loadMemory() {
        return loadData("memory.json", new TypeToken<List<Memory>>() {}.getType());
    }

    /**
     * Load Storage from JSON file
     */
    public List<Storage> loadStorage() {
        return loadData("internal-hard-drive.json", new TypeToken<List<Storage>>() {}.getType());
    }

    /**
     * Load Video Cards from JSON file
     */
    public List<VideoCard> loadVideoCards() {
        return loadData("video-card.json", new TypeToken<List<VideoCard>>() {}.getType());
    }

    /**
     * Load Cases from JSON file
     */
    public List<Case> loadCases() {
        return loadData("case.json", new TypeToken<List<Case>>() {}.getType());
    }

    /**
     * Load Power Supplies from JSON file
     */
    public List<PowerSupply> loadPowerSupplies() {
        return loadData("power-supply.json", new TypeToken<List<PowerSupply>>() {}.getType());
    }

    /**
     * Generic method to load data from JSON file
     */
    private <T> List<T> loadData(String filename, Type type) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(dataPath + filename);
            
            if (inputStream == null) {
                System.err.println("Warning: Could not find " + filename + ", returning empty list");
                return new ArrayList<>();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            List<T> data = gson.fromJson(reader, type);
            reader.close();
            
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading " + filename + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Create sample data for testing when JSON files are not available
     */
    public void createSampleData() {
        System.out.println("Creating sample data...");
        // This method can be used to generate sample JSON files for testing
    }
}
