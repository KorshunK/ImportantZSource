package ru.korshun.importantz.commands.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.command.TabCompleterMethod;
import ru.korshun.importantz.api.home.Home;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Module(name = "home")
public class HomeCommand extends CommandHandler {

    @HandleCommand(permission = "")
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        teleportHome(sender, "home");
    }

    @HandleCommand(permission = "", argCount = 1)
    @IgnoreReturnType
    public void oneArg(CommandSender sender, Command command, String label, String[] args) {
        teleportHome(sender, args[0]);
    }

    @TabCompleterMethod
    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        List<String> tab = new ArrayList<>();
        if(args.length == 1) {
            for(Home home : user.getHomes()) {
                tab.add(home.getName());
            }
        }
        tab.removeIf((arg) -> !arg.startsWith(args[0]));
        return tab;
    }

    private void teleportHome(CommandSender sender, String name) {
        User user = ImportantZ.getUser(sender);
        Home home = user.getHome(name);
        if(!user.isHomeExists(name)) {
            user.sendMessage("home", "home-not-found", true, false, new HashMap<String, String>() {{
                put("{home_name}", name);
            }});
            return;
        }
        home.teleport();
        user.sendMessage("home", "success-teleport-home", true, false, new HashMap<String, String>() {{
            put("{home_name}", home.getName());
        }});
    }
}
