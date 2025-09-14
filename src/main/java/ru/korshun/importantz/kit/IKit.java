package ru.korshun.importantz.kit;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.kit.Kit;
import ru.korshun.importantz.api.user.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IKit implements Kit {
    private String name;
    private long cooldown;
    private HashMap<String, ItemStack> items;

    public IKit(String name, long cooldown, HashMap<String, ItemStack> items) {
        this.name = name;
        this.cooldown = cooldown;
        this.items = items;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public HashMap<String, ItemStack> getItems() {
        return items;
    }

    @Override
    public void give(User user) {
        ImportantZ.getKitManager().giveKit(this.name, user);
    }

    @Override
    public void setItems(HashMap<Integer, ItemStack> items) {

    }

    @Override
    public ItemStack getHelmet() {
        return this.items.get("helmet");
    }

    @Override
    public ItemStack getChestplate() {
        return this.items.get("chestplate");
    }

    @Override
    public ItemStack getLeggins() {
        return this.items.get("leggings");
    }

    @Override
    public ItemStack getBoots() {
        return this.items.get("boots");
    }

//    @Override
//    public void serialize(ConfigurationSection section) {
//        section.set("cooldown", cooldown);
//        ConfigurationSection itemsSection = section.createSection("items");
//        for(HashMap.Entry entry : this.items.entrySet()) {
//            String key = String.valueOf(entry.getKey());
//            ItemStack value = (ItemStack) entry.getValue();
//            ConfigurationSection itemSection = itemsSection.createSection("slot_" + key);
//            itemSection.set("slot", key);
//            itemSection.set("material", value.getType().name());
//            itemSection.set("amount", value.getAmount());
//            List<String> enchantments = new ArrayList<>();
//            for(Enchantment enchantment : value.getEnchantments().keySet()) {
//                int level = value.getEnchantments().get(enchantment);
//                enchantments.add(enchantment.getName() + ":" + level);
//            }
//            itemSection.set("enchantments", enchantments);
//        }
//    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("cooldown", cooldown);
        ConfigurationSection itemsSec = section.createSection("items");
        for (HashMap.Entry<String, ItemStack> entry : items.entrySet()) {
            itemsSec.set(String.valueOf(entry.getKey()), entry.getValue());
        }
    }

    private HashMap<String, ItemStack> parseItems() {
        HashMap<String, ItemStack> items = new HashMap<>();
        File kitFile = new File(ImportantZ.getInstance().getDataFolder().getAbsolutePath() + "/kits/" + name + ".yml");
        YamlConfiguration file = YamlConfiguration.loadConfiguration(kitFile);
        for(String itemName : file.getConfigurationSection("items").getKeys(false)) {
            ConfigurationSection item = file.getConfigurationSection("items").getConfigurationSection(itemName);
            String slot = item.getString("slot");
            Material material = Material.valueOf(item.getString("material"));
            int amount = item.getInt("amount", 1);
            HashMap<Enchantment, Integer> enchantments = new HashMap<>();
            for(String s: item.getStringList("enchantments")) {
                String[] enchantment = s.split(":");
                String enchantmentName = enchantment[0];
                int enchantmentLevel = Integer.parseInt(enchantment[1]);
                enchantments.put(Enchantment.getByName(enchantmentName), enchantmentLevel);
            }
            ItemStack itemStack = new ItemStack(material, amount);
            ItemMeta im = itemStack.getItemMeta();
            for(Enchantment ench : enchantments.keySet()) {
                int enchLevel = enchantments.get(ench);
                im.addEnchant(ench, enchLevel, true);
            }
            itemStack.setItemMeta(im);
            items.put(slot, itemStack);
        }
        return items;
    }
}
