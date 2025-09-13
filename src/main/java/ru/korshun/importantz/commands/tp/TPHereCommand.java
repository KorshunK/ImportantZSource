package ru.korshun.importantz.commands.tp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

import java.util.HashMap;

@Module(name = "tp")
public class TPHereCommand extends CommandHandler {

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
        target.teleport(user);
        user.sendMessage("tp", "tphere-success", true, true, new HashMap<String, String>() {{
            put("{target_name}", target.getName());
        }});
    }
}
