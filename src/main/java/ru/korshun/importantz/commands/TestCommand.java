package ru.korshun.importantz.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.database.parent.HomesDBManager;
import ru.korshun.importantz.api.home.Home;
import ru.korshun.importantz.api.kit.Kit;
import ru.korshun.importantz.api.kit.KitManager;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.OfflineUser;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.api.warp.Warp;
import ru.korshun.importantz.kit.IKit;
import ru.korshun.importantz.utils.NMSUtils;
import ru.korshun.importantz.utils.WarpUtils;

import java.io.FileWriter;
import java.io.IOException;

public class TestCommand extends CommandHandler {

    @HandleCommand(permission = "", argCount = 1)
    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
        KitManager kitManager = ImportantZ.getKitManager();
        User user = ImportantZ.getUser(sender);
        if(args[0].equalsIgnoreCase("create")) {
            kitManager.createKit("start", 10, user.getInventory());
        }
        else if(args[0].equalsIgnoreCase("give")) {
            kitManager.giveKit("start", user);
        }
        return true;
    }
}
