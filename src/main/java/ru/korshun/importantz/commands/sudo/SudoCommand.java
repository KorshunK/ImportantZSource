package ru.korshun.importantz.commands.sudo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreArgCount;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Module(name = "sudo")
public class SudoCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 2)
    @IgnoreArgCount
    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        User target = ImportantZ.getUser(args[0]);
        Player pTarget = target.getPlayer().getPlayer();
        if(pTarget == null) {
            user.sendMessage("player-not-found", true, true);
            return false;
        }
        if(args.length > 2) {
            List<String> commandLineList = new ArrayList<>();
            for(int i = 2; i < args.length; i++) {
                commandLineList.add(args[i]);
            }
            String[] commandLineArray = new String[commandLineList.size()];
            commandLineArray = commandLineList.toArray(commandLineArray);
            String commandLine = String.join(" ", commandLineArray);
            user.sendMessage("sudo", "sudo-success", true, true, new HashMap<String, String>(){{
                put("{player_name}", target.getName());
                put("{command_name}", "/" + args[1]);
            }});
            Bukkit.dispatchCommand(pTarget, args[1] + " " + commandLine);
            System.out.println(args[1] + " " + commandLine);
        } else {
            user.sendMessage("sudo", "sudo-success", true, true, new HashMap<String, String>(){{
                put("{player_name}", target.getName());
                put("{command_name}", "/" + args[1]);
            }});
            Bukkit.dispatchCommand(pTarget, args[1]);
        }
        return true;
    }
}
