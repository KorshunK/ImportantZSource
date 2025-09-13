package ru.korshun.importantz.commands.vanish;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreModifier;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

import java.util.HashMap;

@Module(name = "vanish")
public class VanishCommand extends CommandHandler {

    @HandleCommand(permission = "")
    @IgnoreReturnType
    public void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        boolean isVanished = user.switchVanish();
        if(isVanished) {
            user.sendMessage("vanish", "vanish-enabled", true, false);
            vanish(user);
        } else {
            user.sendMessage("vanish", "vanish-disabled", true, false);
            unvanish(user);
        }
    }

    @HandleCommand(permission = ".others", argCount = 1, isConsole = true)
    @IgnoreReturnType
    public void oneArg(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        User target = ImportantZ.getUser(args[0]);
        if(target.getPlayer().getPlayer() == null) {
            user.sendMessage("player-not-found", true, true);
            return;
        }
        boolean isVanished = target.switchVanish();
        if(isVanished) {
            user.sendMessage("vanish", "vanish-player-enabled", true, true, new HashMap<String, String>() {{
                put("{player_name}", target.getName());
            }});
            target.sendMessage("vanish", "vanish-enabled", true, false);
            vanish(target);
        } else {
            user.sendMessage("vanish", "vanish-player-disabled", true, true, new HashMap<String, String>() {{
                put("{player_name}", target.getName());
            }});
            target.sendMessage("vanish", "vanish-enabled", true, false);
            unvanish(target);
        }
    }

    @HandleCommand(permission = ".others", argCount = 2, isConsole = true)
    @IgnoreReturnType
    public void twoArgs(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        User target = ImportantZ.getUser(args[0]);
        if(target.getPlayer().getPlayer() == null) {
            user.sendMessage("player-not-found", true, true);
            return;
        }
        if(args[1].equalsIgnoreCase("enable")) {
            target.hideUser();
            user.sendMessage("vanish", "vanish-player-enabled", true, true, new HashMap<String, String>() {{
                put("{player_name}", target.getName());
            }});
            target.sendMessage("vanish", "vanish-enabled", true, false);
            vanish(target);
        } else if(args[1].equalsIgnoreCase("disable")) {
            target.showUser();
            user.sendMessage("vanish", "vanish-player-disabled", true, true, new HashMap<String, String>() {{
                put("{player_name}", target.getName());
            }});
            target.sendMessage("vanish", "vanish-enabled", true, false);
            unvanish(target);
        }
    }

    private void vanish(User user) {
        user.getPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 55555555, 3));
    }

    private void unvanish(User user) {
        user.getPlayer().getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
