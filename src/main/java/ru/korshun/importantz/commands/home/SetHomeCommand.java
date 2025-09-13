package ru.korshun.importantz.commands.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.command.NotRequirePermission;
import ru.korshun.importantz.api.home.callback.SetHomeCallback;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.utils.UserUtils;

import java.util.HashMap;

@Module(name = "home")
public class SetHomeCommand extends CommandHandler {

    @HandleCommand(permission = "")
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        setHome(sender, "home");
    }

    @HandleCommand(permission = "", argCount = 1)
    @IgnoreReturnType
    @NotRequirePermission
    public void oneArg(CommandSender sender, Command command, String label, String[] args) {
        setHome(sender, args[0]);
    }

    private void setHome(CommandSender sender, String name) {
        if(!UserUtils.hasPermissionStartsWith(sender, this.permission + ".") && !sender.hasPermission(this.permission)) {
            this.sendDontHavePermission(sender);
            return;
        }
        User user = ImportantZ.getUser(sender);
        SetHomeCallback callback = user.setHome(name, user.getLocation());
        switch (callback) {
            case HOME_LIMIT:
                user.sendMessage("home", "home-limit", true, true, new HashMap<String, String>() {{
                    put("{homes_limit}", String.valueOf(user.getHomeLimit()));
                }});
                break;
            case HOME_EXISTS:
                user.sendMessage("home", "home-already-exists", true, true, new HashMap<String, String>() {{
                    put("{home_name}", name);
                }});
                break;
            case SUCCESS:
                user.sendMessage("home", "success-set-home", true, true, new HashMap<String, String>() {{
                    put("{home_name}", name);
                }});
                break;
        }
    }
}
