package ru.korshun.importantz.commands.kit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.*;
import ru.korshun.importantz.api.kit.KitManager;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Module(name = "kit")
public class DeleteKitCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 1)
    @NotRequirePermission
    @IgnoreReturnType
    public void oneArg(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        KitManager kitManager = ImportantZ.getKitManager();
        if (!user.hasPermission(commandPermission) || !user.hasPermission(commandPermission + "." + args[0])) {
            this.sendDontHavePermission(sender);
            return;
        }
        if (kitManager.deleteKit(args[0])) {
            user.sendMessage("kit", "kit-deleted", true, true, new HashMap<String, String>() {{
                put("{kit_name}", args[0]);
            }});
        } else {
            user.sendMessage("kit", "kit-not-exists", true, true, new HashMap<String, String>() {{
                put("{kit_name}", args[0]);
            }});
        }
    }

    @TabCompleterMethod
    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1) {
            for(String s : ImportantZ.getKits().keySet()) {
                if(sender.hasPermission(commandPermission + "." + s)) {
                    list.add(s);
                }
            }
            list.removeIf((arg) -> !arg.startsWith(args[0]));
            return list;
        }
        return null;
    }
}
