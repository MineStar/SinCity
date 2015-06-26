package de.minestar.SinCity.Threads;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Manager.DataManager;
import de.minestar.SinCity.Manager.PlayerManager;
import de.minestar.SinCity.Units.SinCityPlayer;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class AFKThread implements Runnable {

    private DataManager dataManager;
    private PlayerManager playerManager;

    public AFKThread(DataManager dataManager, PlayerManager playerManager) {
        this.dataManager = dataManager;
        this.playerManager = playerManager;
    }

    @Override
    public void run() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        long maxAFKTime;
        SinCityPlayer thisPlayer;
        for (Player player : players) {
            if (!player.isOnline() || player.isDead())
                continue;
            thisPlayer = this.playerManager.getPlayer(player);
            maxAFKTime = this.dataManager.getMaxAFKTime(thisPlayer.getGroup());

            // WHEN AFK TIMER IS REACHED AND (PLAYER HAS NOT MOVED OR IS INSIDE
            // A VEHICLE) (PREVENT ABUSE!)
            if (maxAFKTime >= 0) {
                if (player.isInsideVehicle() || thisPlayer.hasMoved(player.getLocation())) {
                    thisPlayer.setLastLocation(player.getLocation());
                    return;
                }
                if (thisPlayer.isTooLongAFK(maxAFKTime)) {
                    ConsoleUtils.printInfo(Core.NAME, "Kicked '" + player.getName() + "' for being AFK!");
                    player.kickPlayer("Kicked being AFK!");
                }
            } else {
                thisPlayer.setLastLocation(player.getLocation());
            }
        }
    }
}
