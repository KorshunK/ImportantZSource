package ru.korshun.importantz.api.spawn;

import org.bukkit.Location;
import ru.korshun.importantz.api.user.User;

public interface Spawn {
    Location getLocation();
    void teleport(User user);
}
