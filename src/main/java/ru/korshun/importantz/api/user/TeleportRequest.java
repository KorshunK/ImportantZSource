package ru.korshun.importantz.api.user;

public interface TeleportRequest {
    User getSender();
    User getTarget();
    long getExpiryAt();
    void accept();
    void deny();
}
