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
public class TPACancelCommand extends CommandHandler {

    @HandleCommand(permission = "")
    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        if (!user.getSentTeleportRequests().isEmpty()) {
            TeleportRequest teleportRequest = user.getLastSentTeleportRequest();
            user.getLastSentTeleportRequest().deny();
            user.sendMessage("tpa", "tpa-cancelled", true, true);
            teleportRequest.getTarget().sendMessage("tpa", "player-tpa-cancelled", true, true, new HashMap<String, String>() {{
                put("{sender_name}", user.getName());
            }});
            return true;
        }
        user.sendMessage("tpa", "you-not-sent-tpa-requests", true, true);
        return false;
    }
}
