package de.minestar.SinCity.Listener;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import de.minestar.core.MinestarCore;
import de.minestar.core.units.MinestarGroup;
import de.minestar.core.units.MinestarPlayer;

public class AdminListener implements Listener {

    private Random randomizer = new Random();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        MinestarPlayer thisPlayer = MinestarCore.getPlayer(event.getEntity().getName());
        if (thisPlayer.getMinestarGroup().equals(MinestarGroup.ADMIN) && event.getEntity().getGameMode() != GameMode.ADVENTURE) {
            // 25 % Drop chance
            if (randomizer.nextInt(4) == 0) {
                ItemStack bedrockStack = new ItemStack(Material.BEDROCK.getId());
                bedrockStack.setAmount(1);
                event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), bedrockStack);
            }
        }        
    }
}
