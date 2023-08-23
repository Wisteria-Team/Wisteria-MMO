package dev.beabueno.wisteriammo.commands;

import dev.beabueno.wisteriammo.WisteriaMMO;
import dev.beabueno.wisteriammo.custom.CustomItemSonicScrewdriver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WMMOCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.sendMessage("CMD executed");
        } else {
            System.out.println("Console cmd");
        }

        if (args.length >= 1) {
            if (args[0].equals("reload")) {
                FileConfiguration config = WisteriaMMO.getPluginInstance().getConfig();
                ConfigurationSection a = config.getConfigurationSection("ability.quick_sword_attack");
                System.out.println(a.getString("name"));
            } else if (args[0].equals("custom") && args.length >= 2) {
                if (args[1].equals("SonicScrewdriver")) {
                    ItemStack itemStack = (new CustomItemSonicScrewdriver()).createItemStack();
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        player.getInventory().addItem(itemStack);
                    }
                }
            }
        }

        return false;
    }
}
