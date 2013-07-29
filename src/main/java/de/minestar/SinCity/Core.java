package de.minestar.SinCity;

import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import de.minestar.SinCity.Commands.AddCornerCommand;
import de.minestar.SinCity.Commands.BiomeCommand;
import de.minestar.SinCity.Commands.ClearAreaCommand;
import de.minestar.SinCity.Commands.DeleteAreaCommand;
import de.minestar.SinCity.Commands.PartRegenCommand;
import de.minestar.SinCity.Commands.RegenCommand;
import de.minestar.SinCity.Commands.SaveAreaCommand;
import de.minestar.SinCity.Commands.SelectCommand;
import de.minestar.SinCity.Listener.AFKListener;
import de.minestar.SinCity.Listener.AdminListener;
import de.minestar.SinCity.Listener.ConnectionListener;
import de.minestar.SinCity.Listener.GriefListener;
import de.minestar.SinCity.Listener.PistonListener;
import de.minestar.SinCity.Listener.SelectListener;
import de.minestar.SinCity.Manager.AreaManager;
import de.minestar.SinCity.Manager.DataManager;
import de.minestar.SinCity.Manager.PlayerManager;
import de.minestar.SinCity.Threads.AFKThread;
import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.minestarlibrary.commands.CommandList;

public class Core extends AbstractCore {

    public static String NAME = "SinCity";

    /**
     * Manager
     */
    private DataManager dataManager;
    private AreaManager areaManager;
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
    private PistonListener pistonListener;

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
        this.areaManager = new AreaManager(this);
        this.areaManager.loadAreas();
        return true;
    }

    @Override
    public boolean createListener() {
        this.afkListener = new AFKListener(this.playerManager);
        this.adminListener = new AdminListener();
        this.griefListener = new GriefListener(this.dataManager, this.playerManager, this.areaManager);
        this.connectionListener = new ConnectionListener(this.playerManager);
        this.selectListener = new SelectListener(this.areaManager);
        this.pistonListener = new PistonListener();
        return true;
    }

    @Override
    public boolean createCommands() {
        //@formatter:off
        this.cmdList = new CommandList(NAME,
                new SelectCommand               ("/ngselect",       "",                     "sincity.select",   this.selectListener),
                new RegenCommand                ("/ngregen",        "",                     "sincity.regen",    this.selectListener),
                new PartRegenCommand            ("/ngpartregen",    "",                     "sincity.regen",    this.selectListener),
                new BiomeCommand                ("/ngbiome",        "<BIOME> <RADIUS>",     "sincity.setbiome", this.selectListener),
                
                new AddCornerCommand            ("/ngaddcorner",    "",                     "sincity.setarea",  this.selectListener),
                new ClearAreaCommand            ("/ngcleararea",    "",                     "sincity.setarea",  this.selectListener),
                new SaveAreaCommand             ("/ngsavearea",     "<AREANAME>",           "sincity.setarea",  this.selectListener),
                new DeleteAreaCommand           ("/ngdeletearea",   "<AREANAME>",           "sincity.setarea",  this.selectListener)
        );
        //@formatter:on
        return true;
    }

    @Override
    public boolean createThreads() {
        this.afkThread = new AFKThread(this.dataManager, this.playerManager);
        return true;
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {
        pm.registerEvents(this.afkListener, this);
        pm.registerEvents(this.adminListener, this);
        pm.registerEvents(this.griefListener, this);
        pm.registerEvents(this.connectionListener, this);
        pm.registerEvents(this.selectListener, this);
        pm.registerEvents(this.pistonListener, this);
        return true;
    }

    @Override
    protected boolean startThreads(BukkitScheduler scheduler) {
        scheduler.scheduleSyncRepeatingTask(this, this.afkThread, 15 * 20, 15 * 20);
        return true;
    }
}
