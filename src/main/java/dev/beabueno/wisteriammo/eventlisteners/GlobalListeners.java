package dev.beabueno.wisteriammo.eventlisteners;

import dev.beabueno.wisteriammo.persistence.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GlobalListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(player);

        // TODO Check for "tiredness" if implemented, and if necessary do:
        // player.kickPlayer("I am too tired, I should rest...");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        CustomPlayer customPlayer = CustomPlayer.getCustomPlayer(e.getPlayer());
        customPlayer.setLastKnownServer(e.getPlayer().getServer().getName());
        customPlayer.setLastKnownLocation(e.getPlayer().getLocation());
    }
}
