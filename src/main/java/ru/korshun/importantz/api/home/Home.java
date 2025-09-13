package ru.korshun.importantz.api.home;

import org.bukkit.Location;
import ru.korshun.importantz.api.user.User;

public interface Home {
    String getName();
    User getOwner();
    Location getLocation();
    void teleport();
}
