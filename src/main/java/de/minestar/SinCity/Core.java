package de.minestar.SinCity;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.SinCity.Commands.RegenCommand;
import de.minestar.SinCity.Commands.SelectCommand;
import de.minestar.SinCity.Listener.AFKListener;
import de.minestar.SinCity.Listener.AdminListener;
import de.minestar.SinCity.Listener.ConnectionListener;
import de.minestar.SinCity.Listener.GriefListener;
import de.minestar.SinCity.Listener.SelectListener;
import de.minestar.SinCity.Manager.DataManager;
import de.minestar.SinCity.Manager.PlayerManager;
import de.minestar.SinCity.Threads.AFKThread;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class Core extends JavaPlugin {

    public static String pluginName = "SinCity";

    /**
     * Commands
     */
    private CommandList commandList;

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
    private AFKListener afkListener;
    private AdminListener adminListener;
    private GriefListener griefListener;
    private ConnectionListener connectionListener;
    private SelectListener selectListener;

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
        this.afkListener = new AFKListener(this.playerManager);
        this.adminListener = new AdminListener();
        this.griefListener = new GriefListener(this.dataManager, this.playerManager);
        this.connectionListener = new ConnectionListener(this.playerManager);
        this.selectListener = new SelectListener();
    }

    private void createCommands() {
        //@formatter:off
        AbstractCommand[] commands = new AbstractCommand[] {
                new SelectCommand("/ngselect", "", "sincity.select", this.selectListener),
                new RegenCommand("/ngregen", "", "sincity.regen", this.selectListener)
        };
        //@formatter:on
        this.commandList = new CommandList(commands);
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this.afkListener, this);
        Bukkit.getPluginManager().registerEvents(this.adminListener, this);
        Bukkit.getPluginManager().registerEvents(this.griefListener, this);
        Bukkit.getPluginManager().registerEvents(this.connectionListener, this);
        Bukkit.getPluginManager().registerEvents(this.selectListener, this);
    }

    private void createThreads() {
        this.afkThread = new AFKThread(this.dataManager, this.playerManager);
    }

    private void startThreads() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.afkThread, 15 * 20, 15 * 20);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.commandList.handleCommand(sender, label, args);
        return true;
    }
}
