package ru.korshun.importantz.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.spawn.Spawn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SpawnUtil {
    public static void setSpawn(Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileConfiguration file = ImportantZ.getInstance().getConfig();
                String spawnName = "spawn";
                file.set(spawnName + ".world", location.getWorld().getName());
                file.set(spawnName + ".x", location.getX());
                file.set(spawnName + ".y", location.getY());
                file.set(spawnName + ".z", location.getZ());
                file.set(spawnName + ".yaw", location.getYaw());
                file.set(spawnName + ".pitch", location.getPitch());
                try {
                    ImportantZ.getInstance().getConfig().save(new File(ImportantZ.getInstance().getDataFolder().getAbsolutePath() + "/config.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ImportantZ.setSpawn(ImportantZ.createSpawn(location));
            }
        }).start();
    }

    public void deleteSpawn(Spawn spawn) {
        new Thread(() -> {
            FileConfiguration file = ImportantZ.getInstance().getConfig();
            file.set("spawn", null);
            try {
                ImportantZ.getInstance().getConfig().save(new File(ImportantZ.getInstance().getDataFolder().getAbsolutePath() + "/spawns.yml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ImportantZ.setSpawn(null);
        }).start();
    }

    public static Spawn getSpawn() {
        Spawn spawn = null;
        FileConfiguration file = ImportantZ.getInstance().getConfig();
        ConfigurationSection spawnSection = file.getConfigurationSection("spawn");
        System.out.println("SpawnSection: " + spawnSection == null);
        if(spawnSection == null || !spawnSection.contains("world")) {
            return null;
        } else {
            spawn = ImportantZ.createSpawn(new Location(Bukkit.getWorld(spawnSection.getString("world")), spawnSection.getDouble("x"), spawnSection.getDouble("y"), spawnSection.getDouble("z"), spawnSection.getInt("yaw"), spawnSection.getInt("pitch")));
            return spawn;
        }
    }
}
