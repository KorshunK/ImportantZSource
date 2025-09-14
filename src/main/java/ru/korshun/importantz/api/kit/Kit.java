package ru.korshun.importantz.api.kit;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.kit.IKit;

import java.util.HashMap;

public interface Kit {
    String getName();
    long getCooldown();
    HashMap<String, ItemStack> getItems();
    void give(User user);
    void setItems(HashMap<Integer, ItemStack> items);
    ItemStack getHelmet();
    ItemStack getChestplate();
    ItemStack getLeggins();
    ItemStack getBoots();
    void serialize(ConfigurationSection section);
    static Kit deserialize(String name) {
        YamlConfiguration kitFile = ImportantZ.getKitManager().getKitFile(name);
        long cooldown = kitFile.getLong("cooldown", 0L);
        HashMap<String, ItemStack> slotMap = new HashMap<>();
        ConfigurationSection itemsSec = kitFile.getConfigurationSection("items");
        if (itemsSec != null) {
            for (String key : itemsSec.getKeys(false)) {
                try {
                    ItemStack item = itemsSec.getItemStack(key);
                    if (item != null) {
                        slotMap.put(key, item);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return ImportantZ.createKitObject(name, cooldown, slotMap);
    }
}
