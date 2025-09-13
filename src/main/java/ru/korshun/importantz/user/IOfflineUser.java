package ru.korshun.importantz.user;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.database.parent.UserDBManager;
import ru.korshun.importantz.api.user.OfflineUser;
import ru.korshun.importantz.api.user.User;

import java.net.InetSocketAddress;
import java.util.UUID;

public class IOfflineUser implements OfflineUser {
    private String name;
    private UUID uuid;
    private OfflinePlayer player;
    private YamlConfiguration userData;
    private long lastLoginTime = -1;
    private long lastLogoffTime = -1;

    public IOfflineUser(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getOfflinePlayer(uuid);
        this.name = player.getName();
        ImportantZ.loadUserData(this);
        this.userData = ImportantZ.getUserDataFile(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
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
    public InetSocketAddress getIPAddress() {
        return new InetSocketAddress(userData.getString("ipAddress"), 8080);
    }

    @Override
    public User getUser() {
        return ImportantZ.getUser(uuid);
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }
}
