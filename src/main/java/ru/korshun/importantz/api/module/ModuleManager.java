package ru.korshun.importantz.api.module;


import org.bukkit.configuration.InvalidConfigurationException;
import ru.korshun.importantz.ImportantZ;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ModuleManager {
    public static void enableModule(String name) {
        ImportantZ.getModules().set(name, true);
        try {
            ImportantZ.getModules().load(new File(ImportantZ.getInstance().getDataFolder().getAbsolutePath() + "/modules.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public static void disableModule(String name) {
        ImportantZ.getModules().set(name, false);
        try {
            ImportantZ.getModules().load(new File(ImportantZ.getInstance().getDataFolder().getAbsolutePath() + "/modules.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isModuleDisabled(String name) {
        return !ImportantZ.getModules().getBoolean(name);
    }
}
