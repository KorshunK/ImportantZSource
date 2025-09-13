package ru.korshun.importantz.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.warp.Warp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarpUtils {
    public static void saveWarp(Warp warp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                YamlConfiguration file = ImportantZ.getWarpsFile();
                file.set(warp.getName() + ".creator", warp.getCreator().getUUID().toString());
                Location location = warp.getLocation();
                String sLoc = "";
                sLoc += location.getWorld().getName() + ":";
                sLoc += location.getX() + ":";
                sLoc += location.getY() + ":";
                sLoc += location.getZ() + ":";
                sLoc += location.getYaw() + ":";
                sLoc += location.getPitch();
                file.set(warp.getName() + ".location", sLoc);
                try {
                    ImportantZ.getWarpsFile().save(new File(ImportantZ.getInstance().getDataFolder().getAbsolutePath() + "/warps.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ImportantZ.getWarps().put(warp.getName(), warp);
            }
        }).start();
    }

    public static void deleteWarp(Warp warp) {
        new Thread(() -> {
            YamlConfiguration file = ImportantZ.getWarpsFile();
            file.set(warp.getName(), null);
            try {
                ImportantZ.getWarpsFile().save(new File(ImportantZ.getInstance().getDataFolder().getAbsolutePath() + "/warps.yml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ImportantZ.getWarps().remove(warp.getName());
        }).start();
    }

    public static List<Warp> getSavedWarps() {
        List<Warp> warps = new ArrayList<>();
        YamlConfiguration file = ImportantZ.getWarpsFile();
        for (String s : file.getKeys(false)) {
//            String[] parts = file.getConfigurationSection(s).getString("location").split(";");
            String[] locationArr = file.getConfigurationSection(s).getString("location").split(":");
            warps.add(ImportantZ.createWarp(s, ImportantZ.getUser(UUID.fromString(file.getConfigurationSection(s).getString("creator"))), new Location(Bukkit.getWorld(locationArr[0]), Double.parseDouble(locationArr[1]), Double.parseDouble(locationArr[2]), Double.parseDouble(locationArr[3]), Float.parseFloat(locationArr[4]), Float.parseFloat(locationArr[5]))));
        }
        return warps;
    }

    public static Warp getWarpByName(String name) {
        for (Warp warp : ImportantZ.getWarps().values()) {
            if (warp.getName().equalsIgnoreCase(name)) {
                return warp;
            }
        }
        return null;
    }

    public static boolean isWarpExists(String name) {
        return ImportantZ.getWarps().containsValue(getWarpByName(name));
    }
}
