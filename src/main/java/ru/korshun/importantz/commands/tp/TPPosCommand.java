package ru.korshun.importantz.commands.tp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

@Module(name = "tp")
public class TPPosCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 3)
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        double x = args[0].startsWith("~") ? user.getLocation().getX() : Double.parseDouble(args[0]);
        double z = args[2].startsWith("~") ? user.getLocation().getZ() : Double.parseDouble(args[2]);
        double y = args[1].startsWith("~") ? user.getLocation().getWorld().getHighestBlockYAt((int) x, (int) z) : Double.parseDouble(args[1]);
        user.teleport(new Location(user.getLocation().getWorld(), x + .5, y + 1, z + .5, user.getLocation().getYaw(), user.getLocation().getPitch()));
    }

    @HandleCommand(permission = "", argCount = 4, isConsole = true)
    @IgnoreReturnType
    public void fourArgs(CommandSender sender, Command command, String label, String[] args) {
        User target = ImportantZ.getUser(args[0]);
        double x = args[1].startsWith("~") ? target.getLocation().getX() : Double.parseDouble(args[1]);
        double z = args[3].startsWith("~") ? target.getLocation().getZ() : Double.parseDouble(args[3]);
        double y = args[2].startsWith("~") ? target.getLocation().getWorld().getHighestBlockYAt((int) x, (int) z) : Double.parseDouble(args[2]);
        target.teleport(new Location(target.getLocation().getWorld(), x + .5, y + 1, z + .5, target.getLocation().getYaw(), target.getLocation().getPitch()));
    }
}
