package de.minestar.SinCity;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

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
import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.commands.CommandList;

public class Core extends AbstractCore {

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

    public Core() {
        this("SinCity");
    }

    public Core(String name) {
        super(name);
    }

    @Override
    public boolean createManager() {
        this.dataManager = new DataManager(this.getDataFolder());
        this.playerManager = new PlayerManager();
        return true;
    }

    @Override
    public boolean createListener() {
        this.afkListener = new AFKListener(this.playerManager);
        this.adminListener = new AdminListener();
        this.griefListener = new GriefListener(this.dataManager, this.playerManager);
        this.connectionListener = new ConnectionListener(this.playerManager);
        this.selectListener = new SelectListener();
        return true;
    }

    @Override
    public boolean createCommands() {
        //@formatter:off
        AbstractCommand[] commands = new AbstractCommand[] {
                new SelectCommand("/ngselect", "", "sincity.select", this.selectListener),
                new RegenCommand("/ngregen", "", "sincity.regen", this.selectListener)
        };
        //@formatter:on
        this.cmdList = new CommandList(Core.NAME, commands);
        return true;
    }

    @Override
    public boolean createThreads() {
        this.afkThread = new AFKThread(this.dataManager, this.playerManager);
        return true;
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {
        Bukkit.getPluginManager().registerEvents(this.afkListener, this);
        Bukkit.getPluginManager().registerEvents(this.adminListener, this);
        Bukkit.getPluginManager().registerEvents(this.griefListener, this);
        Bukkit.getPluginManager().registerEvents(this.connectionListener, this);
        Bukkit.getPluginManager().registerEvents(this.selectListener, this);
        return true;
    }

    @Override
    protected boolean startThreads(BukkitScheduler scheduler) {
        scheduler.scheduleSyncRepeatingTask(this, this.afkThread, 15 * 20, 15 * 20);
        return true;
    }
}
