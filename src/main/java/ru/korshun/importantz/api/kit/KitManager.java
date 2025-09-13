package ru.korshun.importantz.api.kit;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.PlayerInventory;
import ru.korshun.importantz.api.user.User;

public interface KitManager {
    void saveKitsFile();
    void saveKit(Kit kit);
    YamlConfiguration getKitFile(String kitName);
    boolean createKit(String name, long cooldown, PlayerInventory inventory);
    boolean deleteKit(String name);
    void giveKit(String name, User user);
    boolean kitExists(String name);
    Kit getKit(String name);
}
