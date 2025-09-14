package ru.korshun.importantz.utils.config;

import ru.korshun.importantz.utils.ChatUtil;
import ru.korshun.importantz.utils.ConfigUtils;

public class Prefix {
    private String command;
    public Prefix(String command) {
        this.command = command;
    }

    public String getPrefix() {
        if(command.isEmpty()) {
            return ConfigUtils.getPrefix();
        }
        return ChatUtil.translate(ConfigUtils.getString("commands" + "." + command + ".prefix"));
    }
}
