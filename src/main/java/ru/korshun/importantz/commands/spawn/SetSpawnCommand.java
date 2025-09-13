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
import ru.korshun.importantz.utils.SpawnUtil;

@Module(name = "spawn")
public class SetSpawnCommand extends CommandHandler {
    private boolean isTyped = false;

    @HandleCommand(permission = "")
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        Spawn spawn = ImportantZ.getSpawn();
        if(spawn != null) {
            if(!isTyped) {
                user.sendMessage("spawn", "spawn-already-set", true, false);
                isTyped = true;
                return;
            } else {
                ImportantZ.setSpawn(ImportantZ.createSpawn(user.getLocation()));
                user.sendMessage("spawn", "spawn-set", true, false);
                return;
            }
        }
        SpawnUtil.setSpawn(user.getLocation());
        user.sendMessage("spawn", "spawn-set", true, false);
    }
}
