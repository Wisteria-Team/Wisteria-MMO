package dev.beabueno.wisteriammo;

import dev.beabueno.wisteriammo.commands.WMMOCommand;
import dev.beabueno.wisteriammo.config.Config;
import dev.beabueno.wisteriammo.custom.CustomItem;
import dev.beabueno.wisteriammo.eventlisteners.GlobalListeners;
import dev.beabueno.wisteriammo.eventlisteners.TestEvents;
import dev.beabueno.wisteriammo.persistence.Database;
import dev.beabueno.wisteriammo.persistence.PlayerManager;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Log
public final class WisteriaMMO extends JavaPlugin {

    private static WisteriaMMO plugin = null;
    private static Config config = null;

    @Override
    public void onEnable() {
        log.info("[WMMO] onEnable");
        WisteriaMMO.plugin = this;

        // Parse config
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        WisteriaMMO.config = new Config();

        // DB connection
        Database.getConnection();
        if (! Database.isConnected()) {
            log.severe("Error connecting to database!!!");
            Bukkit.getServer().shutdown();
            Bukkit.shutdown();
        }
        log.info(String.format("[WMMO] connected to DB at %s", config.getDatabaseConfig().getUri() ));

        // Register listeners
        Bukkit.getPluginManager().registerEvents(new TestEvents(), this);
        Bukkit.getPluginManager().registerEvents(new GlobalListeners(), this);
        CustomItem.registerCustomItems();

        // Create PlayerManager instance
        PlayerManager.getInstance();

        // Register commands
        getCommand("wmmo").setExecutor(new WMMOCommand());


        log.info("[WMMO] enabled");
    }

    @Override
    public void onDisable() {
        log.info("[WMMO] onDisable");

        // DB disconnection
        Database.disconnect();

        log.info("[WMMO] disabled");
    }

    public static WisteriaMMO getPluginInstance() {
        return WisteriaMMO.plugin;
    }

    public static Config getPluginConfig() {
        return WisteriaMMO.config;
    }
}
