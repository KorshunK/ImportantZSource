package ru.korshun.importantz.commands.message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreArgCount;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Module(name = "message")
public class ReplyCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 1)
    @IgnoreReturnType
    @IgnoreArgCount
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        if(!user.hasPrivateMessages()) {
            user.sendMessage("message", "you_dont_have_messages", true, true);
            return;
        }
        User target = user.getLastPrivateMessage().getSender();
        if(target == null) {
            user.sendMessage("player-not-found", true, false);
            return;
        }
        if (user == target) {
            user.sendMessage("message", "message-player-is-target", true, false);
            return;
        }
        String message = String.join(" ", args);
        HashMap<String, String> params = new HashMap<>();
        params.put("{sender_name}", user.getName());
        params.put("{target_name}", target.getName());
        params.put("{message}", message);
        target.sendPrivateMessage(user, message);
        user.sendMessage("message", "message-format", true, false, params);
        target.sendMessage("message", "message-format", true, false, params);
    }
}
