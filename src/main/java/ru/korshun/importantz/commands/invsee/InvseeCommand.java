package ru.korshun.importantz.commands.invsee;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.command.CommandHandler;
import ru.korshun.importantz.api.command.HandleCommand;
import ru.korshun.importantz.api.command.IgnoreModifier;
import ru.korshun.importantz.api.command.IgnoreReturnType;
import ru.korshun.importantz.api.module.Module;
import ru.korshun.importantz.api.user.User;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Module(name = "invsee")
public class InvseeCommand extends CommandHandler implements Listener {
    private static HashMap<User, User> viewingInventories = new HashMap<>();

    @HandleCommand(permission = "", argCount = 1)
    @IgnoreModifier
    @IgnoreReturnType
    private void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ImportantZ.getUser(sender);
        User target = ImportantZ.getUser(args[0]);
        if(target == null) {
            user.sendMessage("player-not-found", true, true);
            return;
        }
        if(user == target) {
            user.sendMessage("invsee", "invsee-sender-is-target", true, true);
            return;
        }
        invsee(user, target);
    }

    private void invsee(User user, User target) {
        Inventory menu = Bukkit.createInventory(null, 54, "Invsee " + target.getName());
        Player pTarget = target.getPlayer().getPlayer();
        menu.setItem(0, pTarget.getInventory().getHelmet());
        menu.setItem(1, pTarget.getInventory().getChestplate());
        menu.setItem(2, pTarget.getInventory().getItemInOffHand());
        menu.setItem(9, pTarget.getInventory().getLeggings());
        menu.setItem(10, pTarget.getInventory().getBoots());
        int count = 0;
        for (ItemStack is : target.getInventory().getContents()) {
            if (is == null) {
                count++;
                continue;
            }
            if (count > 8) break;
            menu.setItem(count + 45, is);
            count++;
        }
        count = 0;
        for (ItemStack is : target.getInventory().getContents()) {
            if (is == null) {
                count++;
                continue;
            }
            if (count < 9) {
                count++;
                continue;
            }
            if (count + 9 > 44) {
                break;
            }
            menu.setItem(count + 9, is);
            count++;
        }
        viewingInventories.put(user, target);
        user.openInventory(menu);
    }

    private void updateInventory(Inventory inventory, User user) {
        if (inventory != null) {
            inventory.clear();
            PlayerInventory playerInventory = user.getInventory();
            inventory.setItem(0, playerInventory.getHelmet());
            inventory.setItem(1, playerInventory.getChestplate());
            inventory.setItem(9, playerInventory.getLeggings());
            inventory.setItem(10, playerInventory.getBoots());
            int count = 0;
            for(int i = 18; i < 45; i++) {
                ItemStack is = inventory.getStorageContents()[count];
                if(is == null) continue;
                if(count < 9) {
                    count++;
                    continue;
                }
                System.out.println(is.getType().name());
                inventory.setItem(i, is);
                count++;
            }
        }
    }

//    @EventHandler
//    public void onClick(InventoryClickEvent e) {
//        if(!e.getClickedInventory().getTitle().startsWith("Invsee ")) {
//            return;
//        }
//        Player player = (Player) e.getWhoClicked();
//        User target = viewingInventories.get(ImportantZ.getUser(e.getWhoClicked().getUniqueId()));
//        Inventory clickedInventory = e.getInventory();
//
//        if (clickedInventory.getTitle().startsWith("Invsee ")) {
//            e.setCancelled(true); // Отменяем стандартное поведение клика в инвентаре
//
//            ItemStack item = e.getCurrentItem();
//            if (item == null || item.getType() == Material.AIR) {
//                return; // Игнорировать клики по пустым слотам
//            }
//
//            // Проверяем, есть ли предмет в инвентаре игрока
//            if (target.getInventory().contains(item)) {
//                // Удаляем предмет из инвентаря игрока и меню
//                target.getInventory().remove(item);
//            } else {
//                // Добавляем предмет в инвентарь игрока
//                target.getInventory().addItem(item);
//            }
//            updateInventory(target); // обновляем меню
//        }
//    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().startsWith("Invsee ")) {
            if(!e.getWhoClicked().hasPermission("importantz.command.invsee.edit")) {
                return;
            }
            if(!e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                e.setCancelled(true);
            }
            User target = viewingInventories.get(ImportantZ.getUser(e.getWhoClicked().getUniqueId()));
            PlayerInventory targetInventory = target.getInventory();
            ItemStack cursor = e.getCursor();
            ItemStack currentItem = e.getCurrentItem();
            int slot = e.getSlot();
            CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
            if (slot == 0 && !e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                future = future.thenRun(() -> {
                    ItemStack itemInSlot = targetInventory.getHelmet();
                    e.getWhoClicked().setItemOnCursor(itemInSlot);
                    targetInventory.setHelmet(cursor);
                    e.getClickedInventory().setItem(slot, cursor);

                    target.updateInventory();
                });

            } else if (slot == 1 && !e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                future = future.thenRun(() -> {
                    ItemStack itemInSlot = targetInventory.getChestplate();
                    e.getWhoClicked().setItemOnCursor(itemInSlot);
                    targetInventory.setChestplate(cursor);
                    e.getClickedInventory().setItem(slot, cursor);

                    target.updateInventory();
                });
            } else if (slot == 2 && !e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                future = future.thenRun(() -> {
                    ItemStack itemInSlot = targetInventory.getItemInOffHand();
                    e.getWhoClicked().setItemOnCursor(itemInSlot);
                    targetInventory.setItemInOffHand(cursor);
                    e.getClickedInventory().setItem(slot, cursor);

                    target.updateInventory();
                });
            } else if (slot == 9 && !e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                future = future.thenRun(() -> {
                    ItemStack itemInSlot = targetInventory.getLeggings();
                    e.getWhoClicked().setItemOnCursor(itemInSlot);
                    targetInventory.setLeggings(cursor);
                    e.getClickedInventory().setItem(slot, cursor);

                    target.updateInventory();
                });
            } else if (slot == 10 && !e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                future = future.thenRun(() -> {
                    ItemStack itemInSlot = targetInventory.getBoots();
                    e.getWhoClicked().setItemOnCursor(itemInSlot);
                    targetInventory.setBoots(cursor);
                    e.getClickedInventory().setItem(slot, cursor);

                    target.updateInventory();
                });
            }
            else if (slot >= 18 && slot <= 44 && !e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                int targetSlot = slot - 18 + 9;
                if (targetSlot >= 9 && targetSlot <= 35) {
                    final int inventorySlot = targetSlot;

                    future = future.thenRun(() -> {
                        ItemStack itemInSlot = targetInventory.getItem(inventorySlot);
                        e.getWhoClicked().setItemOnCursor(itemInSlot);
                        targetInventory.setItem(inventorySlot, cursor);
                        e.getClickedInventory().setItem(slot, cursor);

                        target.updateInventory();
                    });
                }
            }
            else if (slot >= 45 && slot <= 53 && !e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                int hotbarSlot = slot - 45;
                if (hotbarSlot >= 0 && hotbarSlot <= 8) {
                    final int hotBarFinal = hotbarSlot;

                    future = future.thenRun(() -> {
                        ItemStack itemInSlot = targetInventory.getItem(hotBarFinal);
                        e.getWhoClicked().setItemOnCursor(itemInSlot);
                        targetInventory.setItem(hotBarFinal, cursor);
                        e.getClickedInventory().setItem(slot, cursor);

                        target.updateInventory();
                    });
                }
            }
            future.join();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(!e.getView().getTitle().startsWith("Invsee ")) {
            return;
        }
        viewingInventories.remove(ImportantZ.getUser(e.getPlayer()));
    }
}
