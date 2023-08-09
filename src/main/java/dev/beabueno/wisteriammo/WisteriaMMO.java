package dev.beabueno.wisteriammo;

import dev.beabueno.wisteriammo.eventlisteners.TestEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WisteriaMMO extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("[WMMO] onEnable");
        Bukkit.getPluginManager().registerEvents(new TestEvents(), this);
        System.out.println("[WMMO] enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[WMMO] onDisable");
    }
}
