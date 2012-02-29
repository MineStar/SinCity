package de.minestar.SinCity.Units;

import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.SinCity.Exceptions.PlayerOfflineException;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class SinCityPlayer {
    private final String playerName;
    private String nickName;
    private String listName;
    private String group;
    private long lastPlayed;

    public SinCityPlayer(Player player) {
        this.playerName = player.getName().toLowerCase();
        this.update();
    }

    public void update() {
        // GET THE PLAYER
        Player player = PlayerUtils.getOnlinePlayer(this.playerName);
        if (player == null)
            throw new PlayerOfflineException(this.playerName);

        // UPDATE THE PLAYER
        this.nickName = (player.getDisplayName() == null ? player.getName() : player.getDisplayName());
        this.listName = (player.getPlayerListName() == null ? player.getName() : player.getPlayerListName());
        this.lastPlayed = player.getLastPlayed();
        this.group = UtilPermissions.getGroupName(player).toLowerCase();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getListName() {
        return listName;
    }

    public String getGroup() {
        return group;
    }

    public long getLastPlayed() {
        return lastPlayed;
    }
}
