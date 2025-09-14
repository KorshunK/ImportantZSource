package ru.korshun.importantz.spawn;

import org.bukkit.Location;
import ru.korshun.importantz.api.spawn.Spawn;
import ru.korshun.importantz.api.user.User;

public class ESpawn implements Spawn {
    private Location location;

    public ESpawn(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void teleport(User user) {
        user.teleport(location);
    }
}
