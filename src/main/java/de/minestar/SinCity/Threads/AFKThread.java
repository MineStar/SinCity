package de.minestar.SinCity.Threads;

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
        Player[] players = Bukkit.getOnlinePlayers();
        long maxAFKTime;
        SinCityPlayer thisPlayer;
        for (Player player : players) {
            if (!player.isOnline() || player.isDead())
                continue;
            thisPlayer = this.playerManager.getPlayer(player);
            maxAFKTime = this.dataManager.getMaxAFKTime(thisPlayer.getGroup());
            if (maxAFKTime >= 0 && !thisPlayer.hasMoved(player.getLocation())) {
                if (thisPlayer.isTooLongAFK(maxAFKTime)) {
                    ConsoleUtils.printInfo(Core.pluginName, "Kicked '" + player.getName() + "' for being AFK!");
                    player.kickPlayer("Kicked being AFK!");
                }
            } else {
                thisPlayer.setLastLocation(player.getLocation());
            }
        }
    }
}
