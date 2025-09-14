package ru.korshun.importantz.message;

import ru.korshun.importantz.api.message.PrivateMessage;
import ru.korshun.importantz.api.user.User;

public class IPrivateMessage implements PrivateMessage {
    private User sender;
    private User target;
    private String content;

    public IPrivateMessage(User sender, User target, String content) {
        this.sender = sender;
        this.target = target;
        this.content = content;
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
    public String getContent() {
        return content;
    }
}
