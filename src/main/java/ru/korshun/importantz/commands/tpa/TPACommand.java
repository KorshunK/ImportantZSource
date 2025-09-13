package ru.korshun.importantz.commands.tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

import java.util.HashMap;

@Module(name = "tpa")
public class TPACommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 1, isConsole = true)
    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        User target = ImportantZ.getUser(args[0]);
        if (target == null) {
            user.sendMessage("player-not-found", true, true);
            return false;
        }
        if (user == target) {
            user.sendMessage("tpa", "tpa-player-is-target", true, true);
            return false;
        }
        if (!user.isSentTeleportRequestTo(target)) {
            target.sendTeleportRequest(user);
            user.sendMessage("tpa", "you-sent-tpa", true, true, new HashMap<String, String>() {{
                put("{target_name}", target.getName());
            }});
            target.sendMessage("tpa", "you-have-tpa", true, true, new HashMap<String, String>() {{
                put("{player_name}", user.getName());
            }});
            return true;
        }
        user.sendMessage("tpa", "you-already-sent-tpa-request", true, true, new HashMap<String, String>() {{
            put("{target_name}", target.getName());
        }});
        return false;
    }
}
