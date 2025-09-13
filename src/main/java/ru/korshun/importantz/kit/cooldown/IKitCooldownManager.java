package ru.korshun.importantz.kit.cooldown;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.kit.cooldown.KitCooldownManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IKitCooldownManager implements KitCooldownManager {
    private final ImportantZ plugin;
    private File dataFile;
    private YamlConfiguration dataConfig;
    private final HashMap<String, Long> lastClaims = new HashMap<>();

    public IKitCooldownManager(ImportantZ plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder().getAbsolutePath() + "/kits_data.yml");
        this.dataConfig = ImportantZ.getKitsDataFile();
    }

    @Override
    public void load() {
        lastClaims.clear();
        if(dataConfig == null) plugin.loadKitsDataFile();
        for(String key : dataConfig.getKeys(false)) {
            long when = dataConfig.getLong(key, 0L);
            lastClaims.put(key, when);
        }
    }

    @Override
    public void save() {
        if(dataConfig == null) plugin.loadKitsDataFile();
        for(Map.Entry<String, Long> entry : lastClaims.entrySet()) {
            dataConfig.set(entry.getKey(), entry.getValue());
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {}
    }

    private String key(String kitName, UUID playerUUID) {
        return kitName.toLowerCase() + ":" + playerUUID.toString();
    }

    @Override
    public long getSecondsUntilAvailable(String kitName, UUID playerUUID, long cooldown) {
        long now = System.currentTimeMillis() / 1000L;
        Long last = lastClaims.get(key(kitName, playerUUID));
        if(last == null) return 0L;
        long elapsed = now - last;
        long remaining = cooldown - elapsed;
        return Math.max(0L, remaining);
    }

    @Override
    public void markClaimed(String kitName, UUID playerUUID) {
        long now = System.currentTimeMillis() / 1000L;
        lastClaims.put(key(kitName, playerUUID), now);
        save();
    }
}
