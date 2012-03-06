package de.minestar.SinCity.Units;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.minestar.SinCity.Exceptions.PlayerOfflineException;
import de.minestar.core.MinestarCore;
import de.minestar.core.units.MinestarPlayer;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class SinCityPlayer {
    private long lastPlayed;
    private Location lastLocation;
    private long lastMovedTime;

    private MinestarPlayer player;

    public SinCityPlayer(Player player) {
        this.player = MinestarCore.getPlayer(player);
        this.update();
    }

    public void update() {
        // GET THE PLAYER
        Player player = PlayerUtils.getOnlinePlayer(this.player.getPlayerName());
        if (player == null)
            throw new PlayerOfflineException(this.player.getPlayerName());

        // UPDATE THE PLAYER
        this.lastPlayed = player.getLastPlayed();
        this.lastLocation = player.getLocation();
        this.lastMovedTime = System.currentTimeMillis();
    }

    public String getGroup() {
        return this.player.getGroup();
    }

    public long getLastPlayed() {
        return lastPlayed;
    }

    public boolean hasMoved(Location location) {
        if (location.getBlockX() == this.lastLocation.getBlockX() && location.getBlockY() == this.lastLocation.getBlockY() && location.getBlockZ() == this.lastLocation.getBlockZ() && location.getWorld().getName().equalsIgnoreCase(this.lastLocation.getWorld().getName())) {
            return false;
        }
        return true;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
        this.lastMovedTime = System.currentTimeMillis();
    }

    public boolean isTooLongAFK(long maxAFKTime) {
        return (System.currentTimeMillis() - this.lastMovedTime >= maxAFKTime);
    }
}
