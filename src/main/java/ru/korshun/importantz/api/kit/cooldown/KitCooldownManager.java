package ru.korshun.importantz.api.kit.cooldown;

import java.util.UUID;

public interface KitCooldownManager {
    void load();
    void save();
    long getSecondsUntilAvailable(String kitName, UUID playerUUID, long cooldown);
    void markClaimed(String kitName, UUID playerUUID);
}
