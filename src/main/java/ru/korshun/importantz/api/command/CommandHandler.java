package ru.korshun.importantz.api.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.module.ModuleManager;
import ru.korshun.importantz.api.user.User;
import ru.korshun.importantz.utils.ChatUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandHandler implements TabExecutor {

    protected String permission;
    protected String commandPermission;

    private Method tabCompleterMethod;

    public CommandHandler() {
        initTabCompleter();
    }

    private void initTabCompleter() {
        for(Method method : this.getClass().getDeclaredMethods()) {
            if(method.isAnnotationPresent(TabCompleterMethod.class)) {
                method.setAccessible(true);
                this.tabCompleterMethod = method;
                break;
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Method methodToExecute = null;
        String fullPermission = null;
        boolean isConsole = false;

        if(this.getClass().isAnnotationPresent(Module.class)) {
            Module moduleAnnotation = this.getClass().getAnnotation(Module.class);
            String moduleName = moduleAnnotation.name();
            if(ModuleManager.isModuleDisabled(moduleName)) {
                sender.sendMessage(ChatUtil.translate(ChatUtil.getMessage("module-is-disabled")));
                return false;
            }
        }

        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(HandleCommand.class)) {
                HandleCommand permAnnotation = method.getAnnotation(HandleCommand.class);
                if (permAnnotation.argCount() == args.length) {
                    methodToExecute = method;
                    method.setAccessible(true);
                    isConsole = permAnnotation.isConsole();
                    if((method.getModifiers() == Modifier.PUBLIC || method.isAnnotationPresent(IgnoreModifier.class)) && (method.getReturnType().getName().equals("boolean") || method.isAnnotationPresent(IgnoreReturnType.class))) {
                        commandPermission = "importantz.command." + command.getName();
                        if(permAnnotation.permission().isEmpty()) {
                            fullPermission = "importantz.command." + command.getName();
                        } else {
                            fullPermission = "importantz.command." + command.getName() + "." + permAnnotation.permission();
                        }
                    }
                    break;
                } else if(method.isAnnotationPresent(IgnoreArgCount.class)) {
                    if(permAnnotation.argCount() <= args.length) {
                        methodToExecute = method;
                        method.setAccessible(true);
                        isConsole = permAnnotation.isConsole();
                        if((method.getModifiers() == Modifier.PUBLIC || method.isAnnotationPresent(IgnoreModifier.class)) && (method.getReturnType().getName().equals("boolean") || method.isAnnotationPresent(IgnoreReturnType.class))) {
                            if(permAnnotation.permission().isEmpty()) {
                                fullPermission = "importantz.command." + command.getName();
                            } else {
                                fullPermission = "importantz.command." + command.getName() + "." + permAnnotation.permission();
                            }
                        }
                        break;
                    }
                }
            }
        }

        if (methodToExecute == null) {
            ChatUtil.sendMessage(sender, "enter_command_right", true, true);
            return false;
        }
        if(!methodToExecute.isAnnotationPresent(NotRequirePermission.class)) {
            if (isConsole) {
                if (!sender.hasPermission(fullPermission)) {
                    ChatUtil.sendMessage(sender, "dont_have_permission", true, true);
                    return false;
                }
            } else {
                if (!(sender instanceof Player)) {
                    ChatUtil.sendMessage(sender, "command-not-console", true, false);
                    return false;
                }
                User user = ImportantZ.getUser(sender);
                if (!user.hasPermission(fullPermission)) {
                    user.sendMessage("dont_have_permission", true, true);
                    return false;
                }
            }
        }
        if(methodToExecute.getReturnType().getName().equals("boolean")) {
            try {
                permission = fullPermission;
                return (boolean) methodToExecute.invoke(this, sender, command, label, args);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                permission = fullPermission;
                methodToExecute.invoke(this, sender, command, label, args);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(tabCompleterMethod != null) {
            try {
                return (List<String>) tabCompleterMethod.invoke(this, sender, command, label, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    protected void sendDontHavePermission(CommandSender sender) {
        ChatUtil.sendMessage(sender, "dont_have_permission", true, true);
    }

    protected void sendPlayerNotFound(CommandSender sender) {
        ChatUtil.sendMessage(sender, "player-not-found", true, true);
    }

    protected void sendEnterCommandRight(CommandSender sender) {
        ChatUtil.sendMessage(sender, "enter_command_right", true, true);
    }
}
