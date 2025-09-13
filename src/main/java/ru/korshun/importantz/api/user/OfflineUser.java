package ru.korshun.importantz.api.user;

import org.bukkit.OfflinePlayer;

import java.net.InetSocketAddress;
import java.util.UUID;

public interface OfflineUser {
    String getName();
    UUID getUUID();
    boolean isOnline();
    long getLastLoginTime();
    long getLastLogoffTime();
    InetSocketAddress getIPAddress();
    User getUser();
    OfflinePlayer getPlayer();
}
