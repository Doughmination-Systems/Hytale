package win.doughmination.plural.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import win.doughmination.plural.MainClass;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    private final MainClass plugin;
    private final File dataFile;
    private final Gson gson;
    private Map<UUID, PlayerSystem> systems;

    public DataManager(MainClass plugin) {
        this.plugin = plugin;
        // Create data directory in plugins/Plural
        File dataDir = new File("plugins/Plural");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        this.dataFile = new File(dataDir, "systems.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.systems = new HashMap<>();
    }

    public void load() {
        if (!dataFile.exists()) {
            System.out.println("[Plural] No existing data file found, starting fresh.");
            return;
        }

        try (Reader reader = new FileReader(dataFile)) {
            SystemData data = gson.fromJson(reader, SystemData.class);
            if (data != null && data.systems != null) {
                this.systems = data.systems;
                System.out.println("[Plural] Loaded " + systems.size() + " player systems.");
            }
        } catch (IOException e) {
            System.err.println("[Plural] Failed to load data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            File parentDir = dataFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (Writer writer = new FileWriter(dataFile)) {
                SystemData data = new SystemData();
                data.systems = this.systems;
                gson.toJson(data, writer);
                System.out.println("[Plural] Saved " + systems.size() + " player systems.");
            }
        } catch (IOException e) {
            System.err.println("[Plural] Failed to save data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PlayerSystem getSystem(UUID playerId) {
        return systems.get(playerId);
    }

    public void createSystem(UUID playerId) {
        systems.put(playerId, new PlayerSystem());
        save();
    }

    public void deleteSystem(UUID playerId) {
        systems.remove(playerId);
        save();
    }

    public boolean hasSystem(UUID playerId) {
        return systems.containsKey(playerId);
    }

    private static class SystemData {
        Map<UUID, PlayerSystem> systems;
    }
}