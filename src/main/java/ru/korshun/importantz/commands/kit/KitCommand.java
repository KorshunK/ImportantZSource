package ru.korshun.importantz.commands.kit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.command.NotRequirePermission;
import ru.korshun.importantz.api.kit.KitManager;
import ru.korshun.importantz.api.kit.cooldown.KitCooldownManager;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.kit.cooldown.IKitCooldownManager;

import java.util.HashMap;

@Module(name = "kit")
public class KitCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 1)
    @NotRequirePermission
    @IgnoreReturnType
    public void oneArg(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        KitManager kitManager = ImportantZ.getKitManager();
        KitCooldownManager kitCooldownManager = ImportantZ.getKitCooldownManager();
        if(!user.hasPermission(commandPermission + "." + args[0])) {
            this.sendDontHavePermission(sender);
            return;
        }
        if(kitManager.kitExists(args[0])) {
            long remaining = kitCooldownManager.getSecondsUntilAvailable(args[0], user.getUUID(), kitManager.getKit(args[0]).getCooldown());
            if(remaining > 0) {
                if(!user.hasPermission(commandPermission + "." + args[0] + ".cooldown.bypass")) {
                    user.sendMessage("kit", "kit-cooldown", true, false, new HashMap<String, String>() {{
                        put("{cooldown_remained}", String.valueOf((int) remaining));
                    }});
                    return;
                }
            }
            kitManager.giveKit(args[0], user);
            kitCooldownManager.markClaimed(args[0], user.getUUID());
            user.sendMessage("kit", "kit-claimed", true, false, new HashMap<String, String>() {{
                put("{kit_name}", args[0]);
            }});
        } else {
            user.sendMessage("kit", "kit-not-exists", true, true, new HashMap<String, String>() {{
                put("{kit_name}", args[0]);
            }});
        }
    }
}
