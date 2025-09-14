package ru.korshun.importantz.commands.kit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.command.NotRequirePermission;
import ru.korshun.importantz.api.kit.KitManager;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

import java.util.HashMap;

@Module(name = "kit")
public class CreateKitCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 2)
    @IgnoreReturnType
    public void createKit(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        KitManager kitManager = ImportantZ.getKitManager();
        if (!kitManager.createKit(args[0], Long.parseLong(args[1]), user.getInventory())) {
            user.sendMessage("kit", "kit-already-exists", true, true, new HashMap<String, String>() {{
                put("{kit_name}", args[0]);
            }});
        } else {
            user.sendMessage("kit", "kit-success-created", true, true, new HashMap<String, String>() {{
                put("{kit_name}", args[0]);
            }});
        }
//        else if (args[0].equalsIgnoreCase("give")) {
//            if (!user.hasPermission(commandPermission + ".give." + args[2]) || !user.hasPermission(commandPermission + ".give")) {
//                this.sendDontHavePermission(sender);
//                return;
//            }
//            if (!kitManager.kitExists(args[2])) {
//                user.sendMessage("kit", "kit-not-exists", true, true, new HashMap<String, String>() {{
//                    put("{kit_name}", args[2]);
//                }});
//                return;
//            } else {
//                kitManager.giveKit(args[2], user);
//                user.sendMessage("kit", "kit-success-gave", true, true, new HashMap<String, String>() {{
//                    put("{player_name}", args[1]);
//                    put("{kit_name}", args[2]);
//                }});
//            }
//        } else {
//            this.sendEnterCommandRight(sender);
//        }
    }
}
