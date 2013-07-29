package de.minestar.SinCity.Listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.util.Vector;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Manager.AreaManager;
import de.minestar.SinCity.Manager.DataManager;
import de.minestar.SinCity.Manager.PlayerManager;
import de.minestar.SinCity.Units.SinCityPlayer;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class GriefListener implements Listener {

    private final PlayerManager playerManager;
    private final DataManager dataManager;
    private final AreaManager areaManager;

    public GriefListener(DataManager dataManager, PlayerManager playerManager, AreaManager areaManager) {
        this.dataManager = dataManager;
        this.playerManager = playerManager;
        this.areaManager = areaManager;
    }

    private boolean denyBlockAction(Player player, Block block) {
        SinCityPlayer thisPlayer = this.playerManager.getPlayer(player);

        boolean insideArea = this.areaManager.isInside(block.getWorld().getName(), block.getX(), block.getZ());

        // CHECK FOR DENIAL
        if (!insideArea && this.dataManager.isInDenyAll(thisPlayer.getGroup(), block.getWorld().getName())) {
            ChatUtils.writeError(player, Core.NAME, "Du kannst hier nicht abbauen.");
            return true;
        }

        // CHECK FOR PARTIAL DENIAL
        if (!insideArea && this.dataManager.isInDenyPartial(thisPlayer.getGroup(), block.getWorld().getName())) {
            ChatUtils.writeError(player, Core.NAME, "Du kannst hier nicht abbauen.");
            return true;
        }

        return false;
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        InventoryView inventoryView = player.getOpenInventory();
        if (inventoryView != null) {
            if (inventoryView.getType().equals(InventoryType.MERCHANT)) {
                event.setCancelled(true);
                return;
            }
            Inventory inventory = inventoryView.getTopInventory();
            if (inventory != null) {
                if (inventory.getType().equals(InventoryType.MERCHANT)) {
                    event.setCancelled(true);
                    return;
                }
            }
            inventory = inventoryView.getBottomInventory();
            if (inventory != null) {
                if (inventory.getType().equals(InventoryType.MERCHANT)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
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

        boolean insideArea = this.areaManager.isInside(block.getWorld().getName(), block.getX(), block.getZ());

        // CHECK FOR DENIAL
        if (!insideArea && this.dataManager.isInDenyAll(thisPlayer.getGroup(), player.getWorld().getName())) {
            // CHECK BLOCK
            int ID = block.getTypeId();
            if (ID == Material.DIODE_BLOCK_OFF.getId() || ID == Material.DIODE_BLOCK_ON.getId() || ID == Material.CHEST.getId() || ID == Material.DISPENSER.getId() || ID == Material.FURNACE.getId() || ID == Material.BURNING_FURNACE.getId()) {
                ChatUtils.writeError(player, Core.NAME, "Du kannst hier nicht verändern.");
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
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        // UPDATE EVENT-STATE
        if (event.getDamager().getType().equals(EntityType.PLAYER)) {
            Player attacker = (Player) event.getDamager();
            // ATTACKING PLAYER?
            if (event.getEntity().getType().equals(EntityType.PLAYER)) {
                Player defender = (Player) event.getEntity();
                // SEND MESSAGE, IF EVENT IS CANCELLED AND DEAL DAMAGE
                if (this.denyPlayerDamage(attacker)) {
                    attacker.damage(7.0);
                    event.setCancelled(true);
                    ChatUtils.writeError(attacker, "Nicht die Mama!");
                    this.playerManager.sendToOps("'" + attacker.getName() + "' betreibt PVP an '" + defender.getName() + "'.");
                }
            } else {
                if (event.getEntity().getType().equals(EntityType.VILLAGER)) {
                    ChatUtils.writeError(attacker, "I AM LEGEND!");
                    event.setCancelled(true);
                    return;
                } else if (event.getEntity().getType().equals(EntityType.COW) || event.getEntity().getType().equals(EntityType.CHICKEN) || event.getEntity().getType().equals(EntityType.SHEEP) || event.getEntity().getType().equals(EntityType.IRON_GOLEM) || event.getEntity().getType().equals(EntityType.MUSHROOM_COW) || event.getEntity().getType().equals(EntityType.OCELOT) || event.getEntity().getType().equals(EntityType.SNOWMAN) || event.getEntity().getType().equals(EntityType.PIG) || event.getEntity().getType().equals(EntityType.VILLAGER) || event.getEntity().getType().equals(EntityType.WOLF)) {
                    if (this.denyPlayerDamage(attacker)) {
                        attacker.damage(7.0);
                        attacker.setVelocity(new Vector(0, 5, 0));
                        event.setCancelled(true);
                        ChatUtils.writeError(attacker, "Eine wilde Faaaaaahhhhrrrrrttt.....");
                        this.playerManager.sendToOps("'" + attacker.getName() + "' betreibt PVP an '" + event.getEntity().getType().name() + "'.");
                    }
                }
            }
        }
    }
}
