package ru.korshun.importantz.commands.death;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

@Module(name = "death")
public class DeathCommand extends CommandHandler {

    @HandleCommand(permission = "")
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        if(user.getDeathLocation() == null) {
            user.sendMessage("death", "you-dont-have-death-location", true, false);
            return;
        }
        user.teleportToDeathLocation();
        user.sendMessage("death", "success-teleport", true, false);
    }
}
