package de.minestar.SinCity.Listener;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Units.BiomeData;
import de.minestar.SinCity.Units.BiomeHelper;
import de.minestar.SinCity.Units.Selection;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class SelectListener implements Listener {

    private final HashSet<String> inSelectMode;
    private final HashMap<String, Selection> selections;

    private final HashMap<String, BiomeData> biomes;

    public SelectListener() {
        this.inSelectMode = new HashSet<String>();
        this.selections = new HashMap<String, Selection>();
        this.biomes = new HashMap<String, BiomeData>();
    }

    public void setBiomeData(String playerName, BiomeData data) {
        this.biomes.put(playerName, data);
    }

    public void removeBiomeData(String playerName) {
        this.biomes.remove(playerName);
    }

    public BiomeData getBiomeData(String playerName) {
        return this.biomes.get(playerName);
    }

    public boolean toggleSelectMode(Player player) {
        if (this.isInSelectMode(player)) {
            this.inSelectMode.remove(player.getName());
            return false;
        } else {
            this.inSelectMode.add(player.getName());
            return true;
        }
    }

    public boolean isInSelectMode(Player player) {
        return this.inSelectMode.contains(player.getName());
    }

    public Selection getSelection(Player player) {
        return this.selections.get(player.getName());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {

        BiomeData data = this.getBiomeData(event.getPlayer().getName());
        if (data != null && event.getPlayer().isOp()) {
            HashSet<Byte> hashset = new HashSet<Byte>();
            hashset.add((byte) 0);
            Location location = event.getPlayer().getLineOfSight(hashset, 200).get(0).getLocation();
            if (location != null) {
                BiomeHelper.convertBiome(event.getPlayer(), location, data.getBiome(), data.getRadius());
            } else {
                PlayerUtils.sendBlankMessage(event.getPlayer(), "No target in sight!");
            }
            return;
        }

        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        // WE NEED A BLOCK
        if (!event.hasBlock())
            return;

        // ONLY OPS!
        if (!event.getPlayer().isOp())
            return;

        // IS PLAYER IN SELECTMODE?
        if (!this.isInSelectMode(event.getPlayer()))
            return;

        // GET PLAYER
        Player player = event.getPlayer();

        // WE NEED AN WOOD-PICKAXE IN OUR HANDS
        if (player.getItemInHand() == null || player.getItemInHand().getTypeId() != Material.WOOD_PICKAXE.getId())
            return;

        // CANCEL THE EVENT
        event.setCancelled(true);

        // GET THE CURRENT SELECTION
        Selection thisSelection = this.selections.get(player.getName());

        // ADD IT, IF NOT FOUND
        if (thisSelection == null) {
            thisSelection = new Selection();
            this.selections.put(player.getName(), thisSelection);
        }

        // UPDATE THE LOCATION
        // LEFT-CLICK : Corner 1
        // RIGHT-CLICK : Corner 2
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            thisSelection.setCorner1(event.getClickedBlock().getLocation());
            PlayerUtils.sendSuccess(player, Core.NAME, "Position 1 set.");
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            thisSelection.setCorner2(event.getClickedBlock().getLocation());
            PlayerUtils.sendSuccess(player, Core.NAME, "Position 2 set.");
        }
    }
}
