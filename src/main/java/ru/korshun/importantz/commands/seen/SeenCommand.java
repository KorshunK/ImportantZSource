package ru.korshun.importantz.commands.seen;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.OfflineUser;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.utils.ChatUtil;
import ru.korshun.importantz.utils.DateUtil;
import ru.korshun.importantz.utils.TimeUtil;

import java.util.HashMap;

@Module(name = "seen")
public class SeenCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 1, isConsole = true)
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        OfflineUser target = ImportantZ.getOfflineUser(args[0]);
        if(target.isOnline()) {
            ChatUtil.sendMessage(sender, "seen.player-online", true, false, new HashMap<String, String>() {{
                put("{player_name}", target.getName());
                put("{last_login_time}", DateUtil.formatDateDiff(target.getLastLoginTime()));
            }});
        } else {
            ChatUtil.sendMessage(sender, "seen.player-offline", true, false, new HashMap<String, String>() {{
                put("{player_name}", target.getName());
                put("{last_logoff_time}", DateUtil.formatDateDiff(target.getLastLogoffTime()));
            }});
        }
        ChatUtil.messageTranslatedList(sender, "seen.seen", new HashMap<String, String>() {{
            put("{player_uuid}", target.getUUID().toString());
            put("{ip_address}", target.getIPAddress().toString().split(":")[0].replace("/", ""));
        }});
    }
}
