package ru.korshun.importantz.user;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.database.parent.HomesDBManager;
import ru.korshun.importantz.api.database.parent.UserDBManager;
import ru.korshun.importantz.api.home.Home;
import ru.korshun.importantz.api.home.callback.SetHomeCallback;
import ru.korshun.importantz.api.message.PrivateMessage;
import ru.korshun.importantz.api.user.TeleportRequest;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.utils.ChatUtil;
import ru.korshun.importantz.utils.ConfigUtils;
import ru.korshun.importantz.utils.UserUtils;
import ru.korshun.importantz.utils.config.Prefix;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class IUser implements User {
    private UUID uuid;
    private String name;
    private boolean isFlying;
    private List<TeleportRequest> teleportRequests = new ArrayList<>();
    private List<TeleportRequest> sentTeleportRequests = new ArrayList<>();
    private List<PrivateMessage> privateMessages = new ArrayList<>();
    private boolean isVanished;
    private List<Home> homes;
    private int homeLimit = 0;
    private Location lastLocation;
    private Location deathLocation;
    private long lastLoginTime = -1;
    private long lastLogoffTime = -1;
    private YamlConfiguration userData;

    public IUser(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
        this.isFlying = getPlayer().getPlayer().getAllowFlight();
        isVanished = ImportantZ.getVanishedUsers().contains(this);
        homeLimit = UserUtils.getHomeLimit(this);
        if(homes == null) {
            homes = HomesDBManager.INSTANCE.getHomes(this);
        }
        ImportantZ.loadUserData(this);
        this.userData = ImportantZ.getUserDataFile(this);
    }

    public IUser(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.isFlying = getPlayer().getPlayer().getAllowFlight();
        isVanished = ImportantZ.getVanishedUsers().contains(this);
        homeLimit = UserUtils.getHomeLimit(this);
        if(homes == null) {
            homes = HomesDBManager.INSTANCE.getHomes(this);
        }
        ImportantZ.loadUserData(this);
        this.userData = ImportantZ.getUserDataFile(this);
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.getPlayer().getPlayer().hasPermission(permission);
    }

    public String getName() {
        return name;
    }

    public void sendMessage(String path, boolean translated) {
        if(path.isEmpty()) {
            return;
        }
        if(getPlayer().isOnline()) {
            if(translated) {
                getPlayer().getPlayer().sendMessage(ChatUtil.translate(ChatUtil.getMessage(path)));
            } else {
                getPlayer().getPlayer().sendMessage(ChatUtil.getMessage(path));
            }
        }
    }

    @Override
    public void sendMessage(String path) {
        if(path.isEmpty()) {
            return;
        }
        if(getPlayer().isOnline()) {
            getPlayer().getPlayer().sendMessage(ChatUtil.translate(ChatUtil.getMessage(path)));
        }
    }

    @Override
    public void sendMessage(String command, String path, boolean translated, boolean isPrefix) {
        if(path.isEmpty()) {
            return;
        }
        Prefix p = new Prefix(command);
        String message = command + "." + path;
        if(command.isEmpty()) {
            message = path;
        }
        if(getPlayer().isOnline()) {
            if(!isPrefix) {
                if(translated) {
                    getPlayer().getPlayer().sendMessage(ChatUtil.translate(ChatUtil.getMessage(message)));
                } else {
                    getPlayer().getPlayer().sendMessage(ChatUtil.getMessage(message));
                }
            } else {
                if(translated) {
                    getPlayer().getPlayer().sendMessage(ChatUtil.translate(p.getPrefix() + ChatUtil.getMessage(message)));
                } else {
                    getPlayer().getPlayer().sendMessage(p.getPrefix() + ChatUtil.getMessage(message));
                }
            }
        }
    }

    @Override
    public void sendMessage(String command, String path, boolean translated, boolean isPrefix, HashMap<String, String> replaces) {
        if(path.isEmpty()) {
            return;
        }
        String message = ChatUtil.getMessage(command + "." + path);
        Prefix p = new Prefix(command);
        if(command.isEmpty()) {
            message = ChatUtil.getMessage(path);
        }
        if(getPlayer().isOnline()) {
            if(replaces != null) {
                for(String s : replaces.keySet()) {
                    message = message.replace(s, replaces.get(s));
                }
            }
            if(!isPrefix) {
                if(translated) {
                    getPlayer().getPlayer().sendMessage(ChatUtil.translate(message));
                } else {
                    getPlayer().getPlayer().sendMessage(message);
                }
            } else {
                if(translated) {
                    getPlayer().getPlayer().sendMessage(ChatUtil.translate(p.getPrefix() + message));
                } else {
                    getPlayer().getPlayer().sendMessage(p.getPrefix() + message);
                }
            }
        }
    }

    @Override
    public void sendMessage(String path, boolean translated, boolean isPrefix) {
        String message = ChatUtil.getMessage(path);
        Prefix p = new Prefix("");
        if(getPlayer().isOnline()) {
            if(!isPrefix) {
                if(translated) {
                    getPlayer().getPlayer().sendMessage(ChatUtil.translate(ChatUtil.getMessage(message)));
                } else {
                    getPlayer().getPlayer().sendMessage(ChatUtil.getMessage(message));
                }
            } else {
                if(translated) {
                    getPlayer().getPlayer().sendMessage(ChatUtil.translate(p.getPrefix() + ChatUtil.getMessage(message)));
                } else {
                    getPlayer().getPlayer().sendMessage(p.getPrefix() + ChatUtil.getMessage(message));
                }
            }
        }
    }

    @Override
    public void sendMessage(String path, boolean translated, boolean prefix, HashMap<String, String> replaces) {
        String message = ChatUtil.getMessage(path);
        if(getPlayer().isOnline()) {
            if(replaces != null) {
                for(String s : replaces.keySet()) {
                    message = message.replace(s, replaces.get(s));
                }
            }
            if(!prefix) {
                if(translated) {
                    getPlayer().getPlayer().sendMessage(ChatUtil.translate(message));
                } else {
                    getPlayer().getPlayer().sendMessage(message);
                }
            } else {
                if(translated) {
                    getPlayer().getPlayer().sendMessage(ChatUtil.translate(ConfigUtils.getPrefix() + message));
                } else {
                    getPlayer().getPlayer().sendMessage(ConfigUtils.getPrefix() + message);
                }
            }
        }
    }

    @Override
    public void teleport(User user) {
        if(this.getPlayer().isOnline()) {
            this.getPlayer().getPlayer().teleport(user.getPlayer().getPlayer());
        }
    }

    @Override
    public void teleport(Location location) {
        if(this.getPlayer().isOnline()) {
            this.getPlayer().getPlayer().teleport(location);
        }
    }

    @Override
    public Location getLocation() {
        return this.getPlayer().getPlayer().getLocation();
    }

    @Override
    public boolean isFlying() {
        return isFlying;
    }

    @Override
    public List<TeleportRequest> getTeleportRequests() {
        return teleportRequests;
    }

    @Override
    public List<TeleportRequest> getSentTeleportRequests() {
        return sentTeleportRequests;
    }

    @Override
    public TeleportRequest sendTeleportRequest(User sender) {
        TeleportRequest teleportRequest = ImportantZ.createTeleportRequestObject(sender, this, System.currentTimeMillis());
        this.teleportRequests.add(teleportRequest);
        sender.getSentTeleportRequests().add(teleportRequest);
        return teleportRequest;
    }

    @Override
    public TeleportRequest getLastTeleportRequest() {
        return teleportRequests.get(teleportRequests.size() - 1);
    }

    @Override
    public TeleportRequest getLastSentTeleportRequest() {
        return sentTeleportRequests.get(sentTeleportRequests.size() - 1);
    }

    @Override
    public boolean isSentTeleportRequestTo(User user) {
        boolean b = false;
        for(TeleportRequest request : user.getTeleportRequests()) {
            if(request.getSender() == this) {
                b = true;
                break;
            }
        }
        return b;
    }

    @Override
    public boolean hasTeleportRequests() {
        return !teleportRequests.isEmpty();
    }

    @Override
    public PlayerInventory getInventory() {
        return this.getPlayer().getPlayer().getInventory();
    }

    @Override
    public void openInventory(Inventory inventory) {
        this.getPlayer().getPlayer().openInventory(inventory);
    }

    @Override
    public boolean flySwitch() {
        this.isFlying = !isFlying;
        this.getPlayer().getPlayer().setAllowFlight(isFlying);
        return isFlying;
    }

    @Override
    public void updateInventory() {
        this.getPlayer().getPlayer().updateInventory();
    }

    @Override
    public List<PrivateMessage> getPrivateMessages() {
        return privateMessages;
    }

    @Override
    public PrivateMessage sendPrivateMessage(User sender, String content) {
        PrivateMessage message = ImportantZ.createMessage(sender, this, content);
        this.privateMessages.add(message);
        return message;
    }

    @Override
    public PrivateMessage getLastPrivateMessage() {
        return this.privateMessages.get(this.privateMessages.size() - 1);
    }

    @Override
    public boolean hasPrivateMessages() {
        return !this.privateMessages.isEmpty();
    }

    @Override
    public void hideUser() {
        ImportantZ.getVanishedUsers().add(this);
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission("importantz.vanish.see")) continue;
            player.hidePlayer(this.getPlayer().getPlayer());
        }
    }

    @Override
    public void hideUser(Player player) {
        if(player == null) {
            return;
        }
        player.hidePlayer(this.getPlayer().getPlayer());
    }

    @Override
    public void showUser() {
        ImportantZ.getVanishedUsers().remove(this);
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(this.getPlayer().getPlayer());
        }
    }

    @Override
    public void showUser(Player player) {
        player.showPlayer(this.getPlayer().getPlayer());
        ImportantZ.getVanishedUsers().remove(this);
    }

    @Override
    public boolean switchVanish() {
        isVanished = !isVanished;
        if(isVanished) hideUser();
        else showUser();
        return isVanished;
    }

    @Override
    public boolean isVanished() {
        return isVanished;
    }

    @Override
    public List<Home> getHomes() {
        return homes;
    }

    @Override
    public int getHomesCount() {
        return getHomes().size();
    }

    @Override
    public boolean isHomeExists(String name) {
        for(Home home : this.getHomes()) {
            if(home.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SetHomeCallback setHome(String name, Location location) {
        if(this.getHomeLimit() == this.getHomesCount()) {
            return SetHomeCallback.HOME_LIMIT;
        }
        if(isHomeExists(name)) {
            return SetHomeCallback.HOME_EXISTS;
        }
        HomesDBManager.INSTANCE.addHome(name, this, location);
        this.homes.add(ImportantZ.createHome(name, this, location));
        return SetHomeCallback.SUCCESS;
    }

    @Override
    public void setHomeLimit(int limit) {
        homeLimit = limit;
    }

    @Override
    public int getHomeLimit() {
        return homeLimit;
    }

    @Override
    public Home getHome(String name) {
        for(Home home : this.getHomes()) {
            if(home.getName().equalsIgnoreCase(name)) {
                return home;
            }
        }
        return null;
    }

    @Override
    public void deleteHome(Home home) {
        this.homes.remove(home);
        HomesDBManager.INSTANCE.removeHome(home);
    }

    @Override
    public void reloadHomes() {
        this.homes = HomesDBManager.INSTANCE.getHomes(this);
    }

    @Override
    public boolean hasHomes() {
        return !this.getHomes().isEmpty();
    }

    @Override
    public void setOp(boolean b) {
        this.getPlayer().setOp(b);
    }

    @Override
    public boolean isOp() {
        return this.getPlayer().isOp();
    }

    @Override
    public void setLastLocation(Location location) {
        this.lastLocation = location;
    }

    @Override
    public Location getLastLocation() {
        return lastLocation;
    }

    @Override
    public void teleportToLastLocation() {
        this.teleport(lastLocation);
    }

    @Override
    public void setDeathLocation(Location location) {
        this.deathLocation = location;
    }

    @Override
    public Location getDeathLocation() {
        return this.deathLocation;
    }

    @Override
    public boolean isPlayedBefore() {
        return this.getPlayer().hasPlayedBefore();
    }

    @Override
    public void teleportToDeathLocation() {
        this.teleport(deathLocation);
    }

    @Override
    public InetSocketAddress getIPAddress() {
        return new InetSocketAddress(userData.getString("ipAddress"), 8080);
    }

    @Override
    public boolean isOnline() {
        return getPlayer().isOnline();
    }

    @Override
    public long getLastLoginTime() {
        return UserDBManager.INSTANCE.getLastLoginTime(this);
    }

    @Override
    public long getLastLogoffTime() {
        return UserDBManager.INSTANCE.getLastLogoffTime(this);
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }
    @Override
    public User getUser() {
        return this;
    }
}
