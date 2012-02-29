package de.minestar.SinCity;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.SinCity.Listener.ConnectionListener;
import de.minestar.SinCity.Listener.GriefListener;
import de.minestar.SinCity.Manager.GroupManager;
import de.minestar.SinCity.Manager.PlayerManager;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class Core extends JavaPlugin {

    public static String pluginName = "SinCity";

    /**
     * Manager
     */
    private GroupManager groupManager;
    private PlayerManager playerManager;

    /**
     * Listener
     */
    private GriefListener griefListener;
    private ConnectionListener connectionListener;

    @Override
    public void onDisable() {
        // PRINT INFO
        ConsoleUtils.printInfo(pluginName, "Disabled v" + this.getDescription().getVersion() + "!");
    }

    @Override
    public void onEnable() {
        // CREATE MANAGER, LISTENER, COMMANDS
        this.createManager();
        this.createListener();
        this.createCommands();

        // REGISTER EVENTS
        this.registerEvents();

        // PRINT INFO
        ConsoleUtils.printInfo(pluginName, "Enabled v" + this.getDescription().getVersion() + "!");
    }

    private void createManager() {
        this.groupManager = new GroupManager(this.getDataFolder());
        this.playerManager = new PlayerManager();
    }

    private void createListener() {
        this.griefListener = new GriefListener(this.groupManager, this.playerManager);
        this.connectionListener = new ConnectionListener(this.playerManager);
    }

    private void createCommands() {
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this.connectionListener, this);
        Bukkit.getPluginManager().registerEvents(this.griefListener, this);
    }
}
