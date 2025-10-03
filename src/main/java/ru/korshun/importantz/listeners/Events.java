package ru.korshun.importantz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.database.parent.UserDBManager;
import ru.korshun.importantz.api.user.OfflineUser;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.utils.UserUtils;

public class Events implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        User joinedUser = ImportantZ.createNewUser(e.getPlayer().getUniqueId());
        long loginTime = System.currentTimeMillis();
        UserDBManager.INSTANCE.setLastLoginTime(joinedUser, loginTime);
        ImportantZ.getOnlineUsers().add(joinedUser);
        loadUserData(joinedUser);
        for(User user : ImportantZ.getVanishedUsers()) {
            if(e.getPlayer().hasPermission("importantz.vanish.see")) {
                continue;
            }
            user.hideUser(e.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        User user = ImportantZ.getUser(e.getPlayer());
        long logoffTime = System.currentTimeMillis();
        UserDBManager.INSTANCE.setLastLogoffTime(user, logoffTime);
        ImportantZ.getOnlineUsers().remove(user);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        User user = ImportantZ.getUser(e.getPlayer());
        user.setLastLocation(e.getFrom());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        User user = ImportantZ.getUser(e.getEntity());
        user.setDeathLocation(e.getEntity().getLocation());
    }

    private void loadUserData(User user) {
        user.setHomeLimit(UserUtils.getHomeLimit(user));
        if(!user.hasPlayedBefore()) {
            UserDBManager.INSTANCE.setup(user);
        }
        OfflineUser offlineUser = ImportantZ.createOfflineUser(user);
        if(!ImportantZ.getOfflineUsers().contains(user)) {
            ImportantZ.getOfflineUsers().add(offlineUser);
        }
        ImportantZ.createUserData(user);
        ImportantZ.getUserDataFile(user).set("ipAddress", user.getIPAddress().toString().split(":")[0].replace("/", ""));
        ImportantZ.saveUserData(user);
        ImportantZ.loadUserData(user);
        user.reloadHomes();
    }
}
