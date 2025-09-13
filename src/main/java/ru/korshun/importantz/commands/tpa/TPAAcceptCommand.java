package ru.korshun.importantz.commands.tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

@Module(name = "tpa")
public class TPAAcceptCommand extends CommandHandler {

    @HandleCommand(permission = "")
    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        if (user.hasTeleportRequests()) {
            user.getLastTeleportRequest().accept();
            user.sendMessage("tpa", "tpa-accepted", true, true);
            return true;
        }
        user.sendMessage("tpa", "you-dont-have-tpa-requests", true, true);
        return false;
    }
}
