/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of SinCity.
 * 
 * SinCity is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * SinCity is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SinCity.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.SinCity.Listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        for (Block block : event.getBlocks()) {
            if (block.getTypeId() == Material.GRAVEL.getId() || block.getTypeId() == Material.SAND.getId()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        Block block = event.getRetractLocation().getBlock();
        if (block.getTypeId() == Material.GRAVEL.getId() || block.getTypeId() == Material.SAND.getId()) {
            event.setCancelled(true);
            return;
        }
    }
}
