package de.minestar.SinCity.Manager;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Units.SinCityPlayer;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class PlayerManager {

    private ConcurrentHashMap<String, SinCityPlayer> playerList;

    public PlayerManager() {
        this.updateAll();
    }

    // /////////////////////////////////////////////
    //
    // MISC METHODS
    //
    // /////////////////////////////////////////////

    public void sendToOps(String message) {
        Player[] players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            // ONLY ONLINE
            if (!UtilPermissions.playerCanUseCommand(player, "sincity.isAdmin"))
                continue;

            ChatUtils.writeError(player, Core.NAME, message);
        }
    }

    // /////////////////////////////////////////////
    //
    // UPDATE ALL
    //
    // /////////////////////////////////////////////

    private void updateAll() {
        playerList = new ConcurrentHashMap<String, SinCityPlayer>();
        Player[] players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            this.addPlayer(player);
        }
    }

    // /////////////////////////////////////////////
    //
    // ADD A PLAYER
    //
    // /////////////////////////////////////////////

    public SinCityPlayer addPlayer(Player player) {
        SinCityPlayer thisPlayer = new SinCityPlayer(player);
        this.playerList.put(player.getName(), thisPlayer);
        return thisPlayer;
    }

    // /////////////////////////////////////////////
    //
    // REMOVE A PLAYER
    //
    // /////////////////////////////////////////////

    public SinCityPlayer removePlayer(Player player) {
        return this.removePlayer(player.getName());
    }

    public SinCityPlayer removePlayer(String playerName) {
        return this.playerList.remove(playerName);
    }

    // /////////////////////////////////////////////
    //
    // GET A PLAYER
    //
    // /////////////////////////////////////////////

    public SinCityPlayer getPlayer(Player player) {
        SinCityPlayer thisPlayer = this.playerList.get(player.getName());
        return (thisPlayer != null ? thisPlayer : this.addPlayer(player));
    }
}
