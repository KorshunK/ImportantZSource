package ru.korshun.importantz.kit;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.kit.Kit;
import ru.korshun.importantz.api.kit.KitManager;
import ru.korshun.importantz.api.user.User;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IKitManager implements KitManager {
    private final ImportantZ plugin;
    private FileConfiguration kitsConfig = ImportantZ.getKitsFile();
    private HashMap<String, Kit> kits = ImportantZ.getKits();

    public IKitManager(ImportantZ plugin) {
        this.plugin = plugin;
    }

    @Override
    public void saveKitsFile() {
        for(String kitName : kitsConfig.getKeys(false)) {
            kitsConfig.set(kitName, null);
        }
        for(Kit kit : kits.values()) {
            kitsConfig.set(kit.getName() + ".file", kit.getName() + ".yml");
        }
        try {
            kitsConfig.save(new File(plugin.getDataFolder().getAbsolutePath() + "/kits.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveKit(Kit kit) {
        saveKitsFile();
        YamlConfiguration kitFile = getKitFile(kit.getName());
        kit.serialize(kitFile);
        try {
            kitFile.save(new File(plugin.getDataFolder().getAbsolutePath() + "/kits/" + kit.getName() + ".yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public YamlConfiguration getKitFile(String kitName) {
        File file = new File(plugin.getDataFolder().getAbsolutePath() + "/kits/" + kitName + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public boolean createKit(String name, long cooldown, PlayerInventory inventory) {
        String kitName = name.toLowerCase();
        if(kitExists(name)) return false;
        HashMap<String, ItemStack> map = new HashMap<>();
        ItemStack[] contents = inventory.getContents();
        for(int slot = 0; slot < contents.length; slot++) {
            ItemStack itemStack = contents[slot];
            if(itemStack != null && itemStack.getType() != Material.AIR) {
                map.put(String.valueOf(slot), itemStack.clone());
            }
        }
        Kit kit = ImportantZ.createKitObject(name, cooldown, map);
        kits.put(kitName, kit);
        saveKitsFile();
        saveKit(kit);
        return true;
    }

    @Override
    public boolean deleteKit(String name) {
        String key = name.toLowerCase();
        if(kits.remove(key) != null) {
            saveKitsFile();
            File file = new File(ImportantZ.getInstance().getDataFolder(), "kits/" + key + ".yml");
            if(file.exists()) {
                file.delete();
            }
            return true;
        }
        return false;
    }

    @Override
    public void giveKit(String name, User user) {
        PlayerInventory inventory = user.getInventory();
        Kit kit = kits.get(name.toLowerCase());
        HashMap<String, ItemStack> items = kit.getItems();
        for(String sSlot : items.keySet()) {
            int slot = 0;
            try {
                slot = Integer.parseInt(sSlot);
            } catch (NumberFormatException ignored) {}
            if(slot >= 0 && slot < inventory.getSize()) {
                ItemStack exists = inventory.getItem(slot);
                if(exists == null || exists.getType() == Material.AIR) {
                    inventory.setItem(slot, items.get(sSlot));
                    continue;
                }
            }
            inventory.addItem(items.get(sSlot));
        }
    }

    @Override
    public boolean kitExists(String name) {
        return kits.containsKey(name);
    }

    @Override
    public Kit getKit(String name) {
        return kits.get(name);
    }
}
