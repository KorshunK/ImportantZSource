package ru.korshun.importantz.home;

import org.bukkit.Location;
import ru.korshun.importantz.api.home.Home;
import ru.korshun.importantz.api.user.User;

public class IHome implements Home {
    private String name;
    private User owner;
    private Location location;

    public IHome(String name, User owner, Location location) {
        this.name = name;
        this.owner = owner;
        this.location = location;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void teleport() {
        this.owner.teleport(this.location);
    }
}
