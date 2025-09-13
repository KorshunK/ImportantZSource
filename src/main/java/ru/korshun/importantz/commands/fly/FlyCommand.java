package ru.korshun.importantz.commands.fly;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

@Module(name = "fly")
public class FlyCommand extends CommandHandler {

    @HandleCommand(permission = "")
    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        boolean fly = user.flySwitch();
        if (fly) {
            user.sendMessage("fly", "fly-enabled", true, true);
        } else {
            user.sendMessage("fly", "fly-disabled", true, true);
        }
        return true;
    }

    @HandleCommand(permission = ".others", argCount = 1)
    public boolean oneArg(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        User target = ImportantZ.getUser(args[0]);
        boolean fly = target.flySwitch();
        if (fly) {
            user.sendMessage("fly", "fly-enabled", true, true);
        } else {
            user.sendMessage("fly", "fly-disabled", true, true);
        }
        return false;
    }
}
