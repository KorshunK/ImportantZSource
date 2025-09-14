package ru.korshun.importantz.user;

import ru.korshun.importantz.api.user.TeleportRequest;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.utils.ConfigUtils;

public class ITeleportRequest implements TeleportRequest {
    private User sender;
    private User target;
    private long expiredAt;

    public ITeleportRequest(User sender, User target, long time) {
        this.sender = sender;
        this.target = target;
        this.expiredAt = time + Integer.parseInt(ConfigUtils.getCommand("tpa", "requestExpiredTime"));
    }

    @Override
    public User getSender() {
        return sender;
    }

    @Override
    public User getTarget() {
        return target;
    }

    @Override
    public long getExpiryAt() {
        return expiredAt;
    }

    @Override
    public void accept() {
        target.getTeleportRequests().remove(this);
        sender.teleport(target);
    }

    @Override
    public void deny() {
        target.getTeleportRequests().remove(this);
    }
}
