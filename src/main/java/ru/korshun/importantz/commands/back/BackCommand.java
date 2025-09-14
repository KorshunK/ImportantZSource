package ru.korshun.importantz.commands.back;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

@Module(name = "back")
public class BackCommand extends CommandHandler {

    @HandleCommand(permission = "")
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        if(user.getLastLocation() == null) {
            user.sendMessage("back", "you-dont-have-back-location", true, false);
            return;
        }
        user.teleportToLastLocation();
        user.sendMessage("back", "success-teleport", true, false);
    }
}
