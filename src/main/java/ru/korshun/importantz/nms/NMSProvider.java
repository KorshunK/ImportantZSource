package ru.korshun.importantz.nms;

public class NMSProvider {
    private static Class<?> craftItemStack;
    private static Class<?> nmsItemStack;

    static {
        try {
            craftItemStack = Class.forName("org.bukkit.craftbukkit");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
