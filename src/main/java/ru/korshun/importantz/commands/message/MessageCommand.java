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
import java.util.HashMap;
import java.util.List;

@Module(name = "message")
public class MessageCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 2)
    @IgnoreReturnType
    @IgnoreArgCount
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        User target = ImportantZ.getUser(args[0]);
        if(target == null) {
            user.sendMessage("player-not-found", true, true);
            return;
        }
        if (user == target) {
            user.sendMessage("message", "message-player-is-target", true, true);
            return;
        }
        List<String> messageList = new ArrayList<>();
        for(int i = 1; i < args.length; i++) {
            messageList.add(args[i]);
        }
        String[] messageArray = new String[messageList.size()];
        messageArray = messageList.toArray(messageArray);
        String message = String.join(" ", messageArray);
        HashMap<String, String> params = new HashMap<>();
        params.put("{sender_name}", user.getName());
        params.put("{target_name}", target.getName());
        params.put("{message}", message);
        target.sendPrivateMessage(user, message);
        user.sendMessage("message", "message-format", true, false, params);
        target.sendMessage("message", "message-format", true, false, params);
    }
}
