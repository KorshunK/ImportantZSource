package ru.korshun.importantz.commands.warp;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.home.Home;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.api.warp.Warp;
import ru.korshun.importantz.utils.ChatUtil;

import java.util.HashMap;

public class WarpsCommand extends CommandHandler {

    @HandleCommand(permission = "")
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        int count = 1;
        if(ImportantZ.getWarps().isEmpty()) {
            user.sendMessage("warp", "warps-not-exists", true, false);
            return;
        }
        for(Warp warp : ImportantZ.getWarps().values()) {
            int finalCount = count;
            Location warpLocation = warp.getLocation();
            ChatUtil.messageTranslatedList(sender, "warp.warps", new HashMap<String, String>() {{
                put("{index}", String.valueOf(finalCount));
                put("{warp_name}", warp.getName());
                put("{x_pos}", String.valueOf(warpLocation.getX()));
                put("{y_pos}", String.valueOf(warpLocation.getY()));
                put("{z_pos}", String.valueOf(warpLocation.getZ()));
            }});
            count++;
        }
    }
}
