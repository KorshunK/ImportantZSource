package ru.korshun.importantz.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachmentInfo;
import ru.korshun.importantz.api.user.User;

import java.util.Set;

public class UserUtils {
    public static int getHomeLimit(User user) {
        if(user.isOp()) {
            return -1;
        }
        for(PermissionAttachmentInfo permission : user.getPlayer().getPlayer().getEffectivePermissions()) {
            System.out.println(permission.getPermission());
            if(permission.getPermission().startsWith("importantz.command.sethome.")) {
                String[] array = permission.getPermission().split("\\.");
                return Integer.parseInt(array[array.length - 1]);
            }
            if(permission.getPermission().equalsIgnoreCase("importantz.command.sethome")) {
                return -1;
            }
        }
        return 0;
    }

    public static boolean hasPermissionStartsWith(CommandSender sender, String startsWith) {
        for(PermissionAttachmentInfo permission : sender.getEffectivePermissions()) {
            if(permission.getPermission().startsWith(startsWith)) {
                return true;
            }
        }
        return false;
    }
}
