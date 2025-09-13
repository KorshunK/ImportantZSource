package ru.korshun.importantz.api.warp;

import org.bukkit.Location;
import ru.korshun.importantz.api.user.User;

public interface Warp {
    String getName();
    User getCreator();
    Location getLocation();
    void teleport(User user);
}
