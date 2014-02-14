package de.minestar.SinCity.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.minestar.SinCity.Manager.PlayerManager;
import de.minestar.SinCity.Units.SinCityPlayer;

public class AFKListener implements Listener {

    private final PlayerManager playerManager;

    public AFKListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        // WE NEED TEXTS!
        if (event.getMessage().length() < 1)
            return;

        // UPDATE EVENT-STATE
        this.playerManager.getPlayer(event.getPlayer()).setLastLocation(event.getPlayer().getLocation());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getType().equals(Material.FISHING_ROD) && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            SinCityPlayer thisPlayer = this.playerManager.getPlayer(player);
            thisPlayer.setLastLocation(player.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        // WE NEED TEXTS!
        if (event.getMessage().length() < 1)
            return;

        // UPDATE EVENT-STATE
        this.playerManager.getPlayer(event.getPlayer()).setLastLocation(event.getPlayer().getLocation());
    }
}
