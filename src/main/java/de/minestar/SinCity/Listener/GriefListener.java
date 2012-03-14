package de.minestar.SinCity.Listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Manager.DataManager;
import de.minestar.SinCity.Manager.PlayerManager;
import de.minestar.SinCity.Units.SinCityPlayer;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class GriefListener implements Listener {

    private final PlayerManager playerManager;
    private final DataManager dataManager;

    public GriefListener(DataManager dataManager, PlayerManager playerManager) {
        this.dataManager = dataManager;
        this.playerManager = playerManager;
    }

    private boolean denyBlockAction(Player player, Block block) {
        SinCityPlayer thisPlayer = this.playerManager.getPlayer(player);

        // CHECK FOR DENIAL
        if (this.dataManager.isInDenyAll(thisPlayer.getGroup(), block.getWorld().getName())) {
            ChatUtils.writeError(player, Core.NAME, "Du kannst hier nicht abbauen.");
            return true;
        }

        // CHECK FOR PARTIAL DENIAL
        if (this.dataManager.isInDenyPartial(thisPlayer.getGroup(), block.getWorld().getName())) {
            ChatUtils.writeError(player, Core.NAME, "Du kannst hier nicht abbauen.");
            return true;
        }

        return false;
    }

    private boolean denyPlayerDamage(Player player) {
        SinCityPlayer thisPlayer = this.playerManager.getPlayer(player);

        // CHECK FOR DENIAL
        if (this.dataManager.isInDenyAll(thisPlayer.getGroup(), player.getWorld().getName())) {
            return true;
        }

        return false;
    }

    private boolean denySpecialInteractAction(Player player, Block block) {
        SinCityPlayer thisPlayer = this.playerManager.getPlayer(player);

        // CHECK FOR DENIAL
        if (this.dataManager.isInDenyAll(thisPlayer.getGroup(), player.getWorld().getName())) {
            // CHECK BLOCK
            int ID = block.getTypeId();
            if (ID == Material.DIODE_BLOCK_OFF.getId() || ID == Material.DIODE_BLOCK_ON.getId() || ID == Material.CHEST.getId() || ID == Material.DISPENSER.getId() || ID == Material.FURNACE.getId() || ID == Material.BURNING_FURNACE.getId()) {
                ChatUtils.writeError(player, Core.NAME, "Du kannst hier nicht ver�ndern.");
                return true;
            }
        }

        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        // UPDATE EVENT-STATE
        event.setCancelled(this.denyBlockAction(event.getPlayer(), event.getBlock()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        // UPDATE EVENT-STATE
        event.setCancelled(this.denyBlockAction(event.getPlayer(), event.getBlock()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        // UPDATE EVENT-STATE
        event.setCancelled(this.denyBlockAction(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace())));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        // UPDATE EVENT-STATE
        event.setCancelled(this.denyBlockAction(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace())));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        // HAS BLOCK?
        if (!event.hasBlock())
            return;

        // UPDATE EVENT-STATE
        event.setCancelled(this.denySpecialInteractAction(event.getPlayer(), event.getClickedBlock()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        // UPDATE EVENT-STATE
        if (event instanceof org.bukkit.event.entity.EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent thisEvent = (EntityDamageByEntityEvent) event;
            if (thisEvent.getDamager() instanceof Player && thisEvent.getEntity() instanceof Player) {
                Player attacker = (Player) thisEvent.getDamager();
                Player defender = (Player) thisEvent.getEntity();
                event.setCancelled(this.denyPlayerDamage(attacker));
                // SEND MESSAGE, IF EVENT IS CANCELLED AND DEAL DAMAGE
                if (event.isCancelled()) {
                    attacker.damage(7);
                    ChatUtils.writeError(attacker, "Nicht die Mama!");
                    this.playerManager.sendToOps("'" + attacker.getName() + "' betreibt PVP an '" + defender.getName() + "'.");
                }
            }
        }
    }
}
