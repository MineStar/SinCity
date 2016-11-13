/*
 * Copyright (C) 2013 MineStar.de 
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

package de.minestar.SinCity.Units.blocks;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class ChestBlock implements ISpecialBlock {

    private final ItemStack[] contents;
    private final int x, y, z;

    public ChestBlock(Block block) {
        x = block.getX();
        y = block.getY();
        z = block.getZ();
        if (!block.getType().equals(Material.CHEST) && !block.getType().equals(Material.TRAPPED_CHEST)) {
            this.contents = null;
            return;
        }

        Chest chest = (Chest) block.getState();
        this.contents = new ItemStack[chest.getBlockInventory().getSize()];
        ItemStack stack;
        for (int i = 0; i < this.contents.length; i++) {
            stack = chest.getBlockInventory().getItem(i);
            if (stack == null) {
                this.contents[i] = null;
                continue;
            } else {
                this.contents[i] = stack.clone();
            }
        }
        chest.getBlockInventory().clear();
    }

    @Override
    public void paste(World world) {
        if (this.contents == null) {
            return;
        }

        Block block = world.getBlockAt(x, y, z);
        if (!block.getType().equals(Material.CHEST) && !block.getType().equals(Material.TRAPPED_CHEST)) {
            return;
        }

        Chest chest = (Chest) block.getState();
        ItemStack stack;
        for (int i = 0; i < this.contents.length; i++) {
            stack = this.contents[i];
            if (stack == null) {
                continue;
            } else {
                chest.getBlockInventory().setItem(i, stack.clone());
            }
        }
    }
}
