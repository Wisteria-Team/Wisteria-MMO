package dev.beabueno.wisteriammo.eventlisteners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerLoadEvent;

public class TestEvents implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        //e.setCancelled(true);
        //e.getPlayer().sendMessage(ChatColor.AQUA + "MSG!");
        System.out.println(String.format("Player %s with UUID %s logging in: %s at IP: %s", e.getName(), e.getUniqueId().toString(), e.getLoginResult().name(), e.getAddress().toString()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        System.out.println(String.format("Player %s with UUID %s logged in at IP: %s", player.getName(), player.getUniqueId().toString(), player.getAddress().toString()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        System.out.println(String.format("Player %s with UUID %s quit", player.getName(), player.getUniqueId().toString()));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        //e.setCancelled(true);
        //e.getPlayer().sendMessage(ChatColor.RED + "Movement forbidden");
    }

    @EventHandler
    public void onPlayerEggThrow(PlayerEggThrowEvent e) {
        e.getPlayer().sendMessage(ChatColor.RED + "Egg thrown!");
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        System.out.println(String.format("Server loaded: %s", e.getType().name()));
    }
}
