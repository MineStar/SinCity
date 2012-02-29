package de.minestar.SinCity.Manager;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.SinCity.Units.SinCityPlayer;

public class PlayerManager {

    private ConcurrentHashMap<String, SinCityPlayer> playerList;

    public PlayerManager() {
        this.updateAll();
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
