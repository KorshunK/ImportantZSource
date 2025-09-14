package ru.korshun.importantz.commands.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.api.warp.Warp;
import ru.korshun.importantz.utils.WarpUtils;

import java.util.HashMap;

@Module(name = "warp")
public class SetWarpCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 1)
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        if(WarpUtils.isWarpExists(args[0])) {
            user.sendMessage("warp", "warp-already-exists", true, false, new HashMap<String, String>() {{
                put("{warp_name}", args[0]);
            }});
            return;
        }
        WarpUtils.saveWarp(ImportantZ.createWarp(args[0], user, user.getLocation()));
        user.sendMessage("warp", "success-set-warp", true, false, new HashMap<String, String>() {{
            put("{warp_name}", args[0]);
        }});
    }
}
