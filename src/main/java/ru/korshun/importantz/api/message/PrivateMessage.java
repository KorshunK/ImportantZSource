package ru.korshun.importantz.api.message;

import ru.korshun.importantz.api.user.User;

public interface PrivateMessage {
    User getSender();
    User getTarget();
    String getContent();
}
