package ru.korshun.importantz.commands.home;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.home.Home;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.utils.ChatUtil;

import java.util.HashMap;
import java.util.List;

@Module(name = "home")
public class HomesCommand extends CommandHandler {

    @HandleCommand(permission = "")
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        int count = 1;
        if(!user.hasHomes()) {
            user.sendMessage("home", "user-hasnt-homes", true, false, new HashMap<String, String>() {{
                put("{player_name}", user.getName());
            }});
            return;
        }
        user.sendMessage("home", "homes-player", true, false, new HashMap<String, String>() {{
            put("{player_name}", user.getName());
        }});
        for(Home home : user.getHomes()) {
            int finalCount = count;
            Location homeLocation = home.getLocation();
            ChatUtil.messageTranslatedList(sender, "home.homes", new HashMap<String, String>() {{
                put("{index}", String.valueOf(finalCount));
                put("{home_name}", home.getName());
                put("{x_pos}", String.valueOf(homeLocation.getX()));
                put("{y_pos}", String.valueOf(homeLocation.getY()));
                put("{z_pos}", String.valueOf(homeLocation.getZ()));
            }});
            count++;
        }
    }

    @HandleCommand(permission = ".others", argCount = 1)
    @IgnoreReturnType
    public void oneArg(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        User target = ImportantZ.getUser(args[0]);
        int count = 1;
        if(!target.hasHomes()) {
            user.sendMessage("home", "user-hasnt-homes", true, false, new HashMap<String, String>() {{
                put("{player_name}", target.getName());
            }});
            return;
        }
        user.sendMessage("home", "homes-player", true, false, new HashMap<String, String>() {{
            put("{player_name}", target.getName());
        }});
        for(Home home : target.getHomes()) {
            int finalCount = count;
            Location homeLocation = home.getLocation();
            ChatUtil.messageTranslatedList(sender, "home.homes", new HashMap<String, String>() {{
                put("{index}", String.valueOf(finalCount));
                put("{home_name}", home.getName());
                put("{x_pos}", String.valueOf(homeLocation.getX()));
                put("{y_pos}", String.valueOf(homeLocation.getY()));
                put("{z_pos}", String.valueOf(homeLocation.getZ()));
            }});
            count++;
        }
    }
}
