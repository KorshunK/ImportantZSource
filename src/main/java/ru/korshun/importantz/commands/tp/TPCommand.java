package ru.korshun.importantz.commands.tp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.utils.ChatUtil;

import java.util.HashMap;

@Module(name = "tp")
public class TPCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 1)
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        User target = ImportantZ.getUser(args[0]);
        if(target == null) {
            this.sendPlayerNotFound(sender);
            return;
        }
        if(user == target) {
            user.sendMessage("tp", "sender-is-target", true, true);
            return;
        }
        user.teleport(target);
        user.sendMessage("tp", "tp-success", true, true, new HashMap<String, String>() {{
            put("{target_name}", target.getName());
        }});
    }

    @HandleCommand(permission = "", argCount = 2, isConsole = true)
    @IgnoreReturnType
    public void twoArgs(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(args[0]);
        User target = ImportantZ.getUser(args[1]);
        if(target == null) {
            this.sendPlayerNotFound(sender);
            return;
        }
        if(user == target) {
            ChatUtil.sendMessage(sender, "tp.sender-is-target-player", true, true);
            return;
        }
        user.teleport(target);
        ChatUtil.sendMessage(sender, "tp.tp-success-player", true, true, new HashMap<String, String>() {{
            put("{player_name}", user.getName());
            put("{target_name}", target.getName());
        }});
    }
}
