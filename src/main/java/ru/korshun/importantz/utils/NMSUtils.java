package ru.korshun.importantz.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class NMSUtils {
    private static final String NMS_VERSION = getNMSVersion();
    private static final String CRAFTBUKKIT_PACKAGE = "org.bukkit.craftbukkit." + NMS_VERSION;
    private static final String NMS_PACKAGE = "net.minecraft.server." + NMS_VERSION;
    private static boolean isPre13 = NMS_VERSION.startsWith("v1_8") || NMS_VERSION.startsWith("v1_9") || NMS_VERSION.startsWith("v1_10") || NMS_VERSION.startsWith("v1_11") || NMS_VERSION.startsWith("v1_12");

    private static String getNMSVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public static Material getNMSMaterial(String materialName) {
        try {
            Class<?> minecraftKeyClass = Class.forName(NMS_PACKAGE + ".MinecraftKey");
            Constructor<?> minecraftKeyConstructor = minecraftKeyClass.getConstructor(String.class, String.class); // two string
            minecraftKeyConstructor.setAccessible(true);
            Object minecraftKey;

            String namespace = "minecraft";
            String key = materialName;

            if(materialName.contains(":")){
                String[] parts = materialName.split(":");
                namespace = parts[0];
                key = parts[1];
            }

            minecraftKey = minecraftKeyConstructor.newInstance(namespace, key);

            Class<?> nmsItemClass = Class.forName(NMS_PACKAGE + ".Item");
            Field registryField = nmsItemClass.getDeclaredField("REGISTRY");
            registryField.setAccessible(true);
            Object registry = registryField.get(null);

            Method getMethod = registry.getClass().getMethod("get", Object.class);
            Object nmsItem = getMethod.invoke(registry, minecraftKey);

            if (nmsItem != null) {
                // Get CraftItemStack
                Class<?> craftItemStackClass = Class.forName(CRAFTBUKKIT_PACKAGE + ".inventory.CraftItemStack");
                Method asBukkitCopyMethod = craftItemStackClass.getMethod("asBukkitCopy", nmsItem.getClass()); // Используем getClass() для nmsItem
                return (Material) asBukkitCopyMethod.invoke(null, nmsItem);

            } else {
                Bukkit.getLogger().log(Level.WARNING, "NMS Item returned null for " + materialName);
                return null;
            }

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to get material " + materialName + " using NMS: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for detailed error information
            return null;
        }
    }
}
