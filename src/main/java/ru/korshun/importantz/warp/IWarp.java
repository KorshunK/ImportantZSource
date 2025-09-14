package ru.korshun.importantz.warp;

import org.bukkit.Location;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.api.warp.Warp;

public class IWarp implements Warp {
    private String name;
    private User creator;
    private Location location;

    public IWarp(String name, User creator, Location location) {
        this.name = name;
        this.creator = creator;
        this.location = location;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public User getCreator() {
        return creator;
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
