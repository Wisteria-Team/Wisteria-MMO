package dev.beabueno.wisteriammo;

import dev.beabueno.wisteriammo.commands.WMMOCommand;
import dev.beabueno.wisteriammo.eventlisteners.TestEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WisteriaMMO extends JavaPlugin {

    private static WisteriaMMO plugin = null;

    @Override
    public void onEnable() {
        System.out.println("[WMMO] onEnable");
        WisteriaMMO.plugin = this;
        Bukkit.getPluginManager().registerEvents(new TestEvents(), this);
        getCommand("wmmo").setExecutor(new WMMOCommand());

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        System.out.println("[WMMO] enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[WMMO] onDisable");
    }

    public static WisteriaMMO getPluginInstance() {
        return WisteriaMMO.plugin;
    }
}
