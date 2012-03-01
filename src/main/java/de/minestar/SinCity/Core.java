package de.minestar.SinCity;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.SinCity.Listener.ConnectionListener;
import de.minestar.SinCity.Listener.GriefListener;
import de.minestar.SinCity.Manager.DataManager;
import de.minestar.SinCity.Manager.PlayerManager;
import de.minestar.SinCity.Threads.AFKThread;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class Core extends JavaPlugin {

    public static String pluginName = "SinCity";

    /**
     * Manager
     */
    private DataManager dataManager;
    private PlayerManager playerManager;

    /**
     * Threads
     */
    private AFKThread afkThread;

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

        // CREATE THREADS
        this.createThreads();

        // START THREADS();
        this.startThreads();

        // PRINT INFO
        ConsoleUtils.printInfo(pluginName, "Enabled v" + this.getDescription().getVersion() + "!");
    }

    private void createManager() {
        this.dataManager = new DataManager(this.getDataFolder());
        this.playerManager = new PlayerManager();
    }

    private void createListener() {
        this.griefListener = new GriefListener(this.dataManager, this.playerManager);
        this.connectionListener = new ConnectionListener(this.playerManager);
    }

    private void createCommands() {
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this.connectionListener, this);
        Bukkit.getPluginManager().registerEvents(this.griefListener, this);
    }

    private void createThreads() {
        this.afkThread = new AFKThread(this.dataManager, this.playerManager);
    }

    private void startThreads() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.afkThread, 10 * 20, 10 * 20);
    }
}
