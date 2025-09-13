package ru.korshun.importantz.commands.tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.TeleportRequest;
import ru.korshun.importantz.api.user.User;

import java.util.HashMap;

@Module(name = "tpa")
public class TPADenyCommand extends CommandHandler {
    @HandleCommand(permission = "")
    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        if (user.hasTeleportRequests()) {
            TeleportRequest teleportRequest = user.getLastTeleportRequest();
            teleportRequest.deny();
            teleportRequest.getTarget().sendMessage("tpa", "tpa-deny", true, true);
            teleportRequest.getSender().sendMessage("tpa", "player-tpa-deny", true, true, new HashMap<String, String>() {{
                put("{target_name}", teleportRequest.getTarget().getName());
            }});
            return true;
        } else {
            user.sendMessage("tpa", "you-dont-have-tpa-requests", true, true);
            return false;
        }
    }
}
