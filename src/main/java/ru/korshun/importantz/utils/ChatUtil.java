package ru.korshun.importantz.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatUtil {
    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String getMessage(String path) {
        String message = ImportantZ.getMessages().getString(path);
        if (message == null) {
            return path;
        }
        return message;
    }

    public static String getMessage(String command, String path) {
        String message = ImportantZ.getMessages().getString(command + "." + path);
        if (message == null) {
            return path;
        }
        return message;
    }

    public static void sendMessage(CommandSender sender, String path, boolean translated, boolean prefix) {
        String message = ChatUtil.getMessage(path);
        if (!prefix) {
            if (translated) {
                sender.sendMessage(ChatUtil.translate(ChatUtil.getMessage(message)));
            } else {
                sender.sendMessage(ChatUtil.getMessage(message));
            }
        } else {
            if (translated) {
                sender.sendMessage(ChatUtil.translate(ConfigUtils.getPrefix() + ChatUtil.getMessage(message)));
            } else {
                sender.sendMessage(ConfigUtils.getPrefix() + ChatUtil.getMessage(message));
            }
        }
    }

    public static void sendMessage(CommandSender sender, String path, boolean translated, boolean prefix, HashMap<String, String> replaces) {
        String message = ChatUtil.getMessage(path);
        for(String s : replaces.keySet()) {
            message = message.replace(s, replaces.get(s));
        }
        if (!prefix) {
            if (translated) {
                sender.sendMessage(ChatUtil.translate(ChatUtil.getMessage(message)));
            } else {
                sender.sendMessage(ChatUtil.getMessage(message));
            }
        } else {
            if (translated) {
                sender.sendMessage(ChatUtil.translate(ConfigUtils.getPrefix() + ChatUtil.getMessage(message)));
            } else {
                sender.sendMessage(ConfigUtils.getPrefix() + ChatUtil.getMessage(message));
            }
        }
    }
    public static List<String> getStringList(String path) {
        return ImportantZ.getMessages().getStringList(path);
    }
    public static void messageTranslatedList(CommandSender sender, String path, HashMap<String, String> replaces) {
        List<String> list = new ArrayList<>();
        for(String str : getStringList(path)) {
            for(String key: replaces.keySet()) {
                str = str.replace(key, replaces.get(key));
            }
            list.add(str);
        }
        for(String s : list) {
            sender.sendMessage(translate(s));
        }
    }
}
