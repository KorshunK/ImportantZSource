package ru.korshun.importantz;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.korshun.importantz.api.home.Home;
import ru.korshun.importantz.api.kit.Kit;
import ru.korshun.importantz.api.kit.KitManager;
import ru.korshun.importantz.api.kit.cooldown.KitCooldownManager;
import ru.korshun.importantz.api.message.PrivateMessage;
import ru.korshun.importantz.api.spawn.Spawn;
import ru.korshun.importantz.api.user.OfflineUser;
import ru.korshun.importantz.api.user.TeleportRequest;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.api.warp.Warp;
import ru.korshun.importantz.commands.TestCommand;
import ru.korshun.importantz.commands.back.BackCommand;
import ru.korshun.importantz.commands.death.DeathCommand;
import ru.korshun.importantz.commands.fly.FlyCommand;
import ru.korshun.importantz.commands.home.DelHomeCommand;
import ru.korshun.importantz.commands.home.HomeCommand;
import ru.korshun.importantz.commands.home.HomesCommand;
import ru.korshun.importantz.commands.home.SetHomeCommand;
import ru.korshun.importantz.commands.invsee.InvseeCommand;
import ru.korshun.importantz.commands.kit.CreateKitCommand;
import ru.korshun.importantz.commands.kit.DeleteKitCommand;
import ru.korshun.importantz.commands.kit.KitCommand;
import ru.korshun.importantz.commands.message.MessageCommand;
import ru.korshun.importantz.commands.message.ReplyCommand;
import ru.korshun.importantz.commands.seen.SeenCommand;
import ru.korshun.importantz.commands.spawn.SetSpawnCommand;
import ru.korshun.importantz.commands.spawn.SpawnCommand;
import ru.korshun.importantz.commands.sudo.SudoCommand;
import ru.korshun.importantz.commands.tp.TPCommand;
import ru.korshun.importantz.commands.tp.TPHereCommand;
import ru.korshun.importantz.commands.tp.TPPosCommand;
import ru.korshun.importantz.commands.tpa.TPAAcceptCommand;
import ru.korshun.importantz.commands.tpa.TPACancelCommand;
import ru.korshun.importantz.commands.tpa.TPACommand;
import ru.korshun.importantz.commands.tpa.TPADenyCommand;
import ru.korshun.importantz.commands.vanish.VanishCommand;
import ru.korshun.importantz.commands.warp.DelWarpCommand;
import ru.korshun.importantz.commands.warp.SetWarpCommand;
import ru.korshun.importantz.commands.warp.WarpCommand;
import ru.korshun.importantz.commands.warp.WarpsCommand;
import ru.korshun.importantz.home.IHome;
import ru.korshun.importantz.kit.IKit;
import ru.korshun.importantz.kit.IKitManager;
import ru.korshun.importantz.kit.cooldown.IKitCooldownManager;
import ru.korshun.importantz.listeners.Events;
import ru.korshun.importantz.message.IPrivateMessage;
import ru.korshun.importantz.spawn.ESpawn;
import ru.korshun.importantz.user.IOfflineUser;
import ru.korshun.importantz.user.ITeleportRequest;
import ru.korshun.importantz.user.IUser;
import ru.korshun.importantz.utils.ConfigUtils;
import ru.korshun.importantz.utils.SpawnUtil;
import ru.korshun.importantz.utils.WarpUtils;
import ru.korshun.importantz.warp.IWarp;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class ImportantZ extends JavaPlugin {
    private static ImportantZ instance;
    private static YamlConfiguration messages;
    private static List<OfflineUser> offlineUsers;
    private static List<User> onlineUsers;
    private static HashMap<String, Boolean> modules;
    private static YamlConfiguration modulesFile;
    private static List<User> vanishedUsers;
    private static YamlConfiguration warpsFile;
    private static HashMap<String, Warp> warps;
    private static Spawn serverSpawn;
    private static HashMap<OfflineUser, YamlConfiguration> userdataConfigs;
    private static HashMap<String, Kit> kits;
    private static YamlConfiguration kitsConfig;
    private static YamlConfiguration kitsDataFile;

    // Managers
    private static KitManager kitManager;
    private static KitCooldownManager kitCooldownManager;


    @Override
    public void onEnable() {
        instance = this;
        vanishedUsers = new ArrayList<>();
        offlineUsers = new ArrayList<>();
        onlineUsers = new ArrayList<>();
        modules = new HashMap<>();
        warps = new HashMap<>();
        userdataConfigs = new HashMap<>();
        kits = new HashMap<>();

        // Files loading
        loadModulesFile();
        loadWarpsFile();
        loadKitsFile();

        // Managers
        kitManager = new IKitManager(this);
        kitCooldownManager = new IKitCooldownManager(this);

        saveDefaultConfig();
        loadMessages();
        loadOfflineUsers();
        loadUsers();
        registerCommands();
        registerEvents();
        loadModules();
        loadWarps();
        loadSpawn();
        loadKits();
        loadKitsDataFile();
        kitCooldownManager.load();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling!");
    }

    @Override
    public void onLoad() {
        loadUsers();
    }

    public static ImportantZ getInstance() {
        if(instance == null) {
            instance = new ImportantZ();
        }
        return instance;
    }

    private void registerEvents() {
        registerEvent(new Events());
        registerEvent(new InvseeCommand());
    }

    public void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    private void registerCommands() {
        registerCommand("fly", new FlyCommand());
        registerCommand("kit", new KitCommand());
        registerCommand("createkit", new CreateKitCommand());
        registerCommand("deletekit", new DeleteKitCommand());
        registerCommand("tpa", new TPACommand());
        registerCommand("tpaccept", new TPAAcceptCommand());
        registerCommand("tpadeny", new TPADenyCommand());
        registerCommand("tpacancel", new TPACancelCommand());
        registerCommand("invsee", new InvseeCommand());
        registerCommand("sudo", new SudoCommand());
        registerCommand("message", new MessageCommand());
        registerCommand("reply", new ReplyCommand());
        registerCommand("vanish", new VanishCommand());
//        registerCommand("tp", new TPCommand());
        registerCommand("sethome", new SetHomeCommand());
        registerCommand("home", new HomeCommand());
        registerCommand("delhome", new DelHomeCommand());
        registerCommand("homes", new HomesCommand());
        registerCommand("warp", new WarpCommand());
        registerCommand("setwarp", new SetWarpCommand());
        registerCommand("delwarp", new DelWarpCommand());
        registerCommand("warps", new WarpsCommand());
        registerCommand("setspawn", new SetSpawnCommand());
        registerCommand("spawn", new SpawnCommand());
        registerCommand("back", new BackCommand());
        registerCommand("death", new DeathCommand());
        registerCommand("seen", new SeenCommand());
        registerCommand("tp", new TPCommand());
        registerCommand("tphere", new TPHereCommand());
        registerCommand("tppos", new TPPosCommand());
        registerCommand("test", new TestCommand());
    }

    public void registerCommand(String name, CommandExecutor executor) {
        this.getCommand(name).setExecutor(executor);
    }

    private void loadUsers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = new IUser(player);
            onlineUsers.add(user);
        }
    }

    private void loadOfflineUsers() {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            OfflineUser user = new IOfflineUser(player.getUniqueId());
            offlineUsers.add(user);
        }
    }

    private void loadMessages() {
        File file = new File(getDataFolder().getAbsolutePath() + "/messages/messages_" + ConfigUtils.getLanguage() + ".yml");
        if (!file.exists()) {
            saveResource("messages/messages_" + ConfigUtils.getLanguage() + ".yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(file);
    }

    private void loadModulesFile() {
        File file = new File(getDataFolder().getAbsolutePath() + "/modules.yml");
        if (!file.exists()) {
            saveResource("modules.yml", false);
        }
        modulesFile = YamlConfiguration.loadConfiguration(file);
    }

    private void loadWarpsFile() {
        File file = new File(getDataFolder().getAbsolutePath() + "/warps.yml");
        if (!file.exists()) {
            saveResource("warps.yml", false);
        }
        warpsFile = YamlConfiguration.loadConfiguration(file);
    }

    public void loadKitsDataFile() {
        File file = new File(getDataFolder().getAbsolutePath() + "/kits_data.yml");
        if(!file.exists()) {
            saveResource("kits_data.yml", false);
        }
        kitsDataFile = YamlConfiguration.loadConfiguration(file);
    }

    private void loadModules() {
        for (String key : modulesFile.getKeys(false)) {
            modules.put(key, modulesFile.getBoolean(key));
        }
    }

    private void loadWarps() {
        for (Warp warp : WarpUtils.getSavedWarps()) {
            warps.put(warp.getName(), warp);
        }
    }

    private void loadKitsFile() {
        File file = new File(getDataFolder().getAbsolutePath() + "/kits.yml");
        File dir = new File(getDataFolder(), "kits");
        dir.mkdir();
        if (!file.exists()) {
            saveResource("kits.yml", false);
        }
        kitsConfig = YamlConfiguration.loadConfiguration(file);
    }

    public void loadKits() {
        kits.clear();
        if(kitsConfig == null) loadKitsFile();
        for(String kitName : kitsConfig.getKeys(false)) {
            Kit kit = Kit.deserialize(kitName);
            kits.put(kitName.toLowerCase(), kit);
        }
    }

    public static void createUserData(OfflineUser user) {
        File file = new File(getInstance().getDataFolder().getAbsolutePath() + "/userdata/" + user.getUUID().toString() + ".yml");
        new Thread(() -> {
            File folder = new File(getInstance().getDataFolder().getAbsolutePath() + "/userdata");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        userdataConfigs.put(user, config);
    }

    public static void loadUserData(OfflineUser user) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(getInstance().getDataFolder().getAbsolutePath() + "/userdata/" + user.getUUID().toString() + ".yml"));
        userdataConfigs.put(user, config);
    }

    public static YamlConfiguration getUserDataFile(OfflineUser user) {
        return userdataConfigs.get(user);
    }

    public static void saveUserData(OfflineUser user) {
        new Thread(() -> {
            try {
                getUserDataFile(user).save(new File(getInstance().getDataFolder().getAbsolutePath() + "/userdata/" + user.getUUID().toString() + ".yml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void loadSpawn() {
        serverSpawn = SpawnUtil.getSpawn();
    }

    public static void setSpawn(Spawn spawn) {
        serverSpawn = spawn;
    }

    public static Spawn getSpawn() {
        return serverSpawn;
    }

    public static User getUser(UUID uuid) {
        for (User user : onlineUsers) {
            if (user.getUUID() == uuid) {
                return user;
            }
        }
        return null;
    }

    public static List<User> getVanishedUsers() {
        if(vanishedUsers == null) {
            vanishedUsers = new ArrayList<>();
        }
        return vanishedUsers;
    }

    public static User getUser(CommandSender sender) {
        return getUser(sender.getName());
    }

    public static User getUser(String name) {
        return getUser(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public static HashMap<String, Kit> getKits() {
        return kits;
    }

    public static OfflineUser getOfflineUser(UUID uuid) {
        for (OfflineUser user : offlineUsers) {
            if (user.getUUID().toString().equals(uuid.toString())) {
                return user;
            }
        }
        return null;
    }

    public static OfflineUser getOfflineUser(String name) {
        return getOfflineUser(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public static YamlConfiguration getMessages() {
        return messages;
    }

    public static YamlConfiguration getModules() {
        return modulesFile;
    }

    public static YamlConfiguration getWarpsFile() {
        return warpsFile;
    }
    public static YamlConfiguration getKitsFile() {
        return kitsConfig;
    }

    public static YamlConfiguration getKitsDataFile() {
        return kitsDataFile;
    }

    public static HashMap<String, Warp> getWarps() {
        return warps;
    }

    public static List<User> getOnlineUsers() {
        return onlineUsers;
    }

    public static List<OfflineUser> getOfflineUsers() {
        return offlineUsers;
    }

    public static User createNewUser(UUID uuid) {
        return new IUser(uuid);
    }

    public static OfflineUser createOfflineUser(User user) {
        return new IOfflineUser(user.getUUID());
    }

    public static PrivateMessage createMessage(User sender, User target, String content) {
        return new IPrivateMessage(sender, target, content);
    }

    public static TeleportRequest createTeleportRequestObject(User sender, User target, long time) {
        return new ITeleportRequest(sender, target, time);
    }

    public static Home createHome(String name, User owner, Location location) {
        return new IHome(name, owner, location);
    }

    public static Warp createWarp(String name, User creator, Location location) {
        return new IWarp(name, creator, location);
    }

    public static Spawn createSpawn(Location location) {
        return new ESpawn(location);
    }

    public static Kit createKitObject(String name, long cooldown, HashMap<String, ItemStack> items) {
        return new IKit(name, cooldown, items);
    }

    /*
    //
    //            Managers
    //
     */

    public static KitManager getKitManager() {
        return kitManager;
    }

    public static KitCooldownManager getKitCooldownManager() {
        return kitCooldownManager;
    }
}
