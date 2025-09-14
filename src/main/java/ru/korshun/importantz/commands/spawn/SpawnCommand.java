package ru.korshun.importantz.commands.spawn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.spawn.Spawn;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.utils.ChatUtil;

import java.util.HashMap;

@Module(name = "spawn")
public class SpawnCommand extends CommandHandler {

    @HandleCommand(permission = "")
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        Spawn spawn = ImportantZ.getSpawn();
        if(spawn == null) {
            user.sendMessage("spawn", "spawn-not-found", true, false);
            return;
        }
        spawn.teleport(user);
        user.sendMessage("spawn", "success-teleported", true, false);
    }

    @HandleCommand(permission = ".others", argCount = 1, isConsole = true)
    @IgnoreReturnType
    public void oneArg(CommandSender sender, Command command, String label, String[] args) {
        User target = ImportantZ.getUser(args[0]);
        Spawn spawn = ImportantZ.getSpawn();
        if(target == null) {
            ChatUtil.sendMessage(sender, "player-not-found", true, true);
            return;
        }
        if(spawn == null) {
            ChatUtil.sendMessage(sender, "spawn.spawn-not-found", true, false);
            return;
        }
        ChatUtil.sendMessage(sender, "spawn.you-teleported-player", true, false);
    }
}
