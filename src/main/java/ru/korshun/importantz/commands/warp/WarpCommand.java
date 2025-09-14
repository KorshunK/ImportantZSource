package ru.korshun.importantz.commands.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.command.TabCompleterMethod;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.api.warp.Warp;
import ru.korshun.importantz.utils.UserUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Module(name = "warp")
public class WarpCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 1)
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        Warp warp = ImportantZ.getWarps().get(args[0]);
        if(warp == null) {
            user.sendMessage("warp", "warp-not-found", true, false, new HashMap<String, String>() {{
                put("{warp_name}", args[0]);
            }});
            return;
        }
        if(!user.hasPermission(this.permission + "." + warp.getName()) || !user.hasPermission(this.permission)) {
            this.sendDontHavePermission(sender);
            return;
        }
        warp.teleport(user);
        user.sendMessage("warp", "success-teleport-warp", true, false, new HashMap<String, String>() {{
            put("{warp_name}", warp.getName());
        }});
    }

    @TabCompleterMethod
    public List<String> tab(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1) {
            list.addAll(ImportantZ.getWarps().keySet());
        }
        list.removeIf((arg) -> !arg.startsWith(args[0]));
        return list;
    }
}
