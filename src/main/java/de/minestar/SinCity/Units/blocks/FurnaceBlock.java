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
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

public class FurnaceBlock implements ISpecialBlock {

    private final ItemStack[] contents;
    private final int x, y, z;
    private short burnTime, cookTime;

    public FurnaceBlock(Block block) {
        x = block.getX();
        y = block.getY();
        z = block.getZ();

        if (!block.getType().equals(Material.FURNACE) && !block.getType().equals(Material.BURNING_FURNACE)) {
            this.contents = null;
            return;
        }

        Furnace furnace = (Furnace) block.getState();
        this.contents = new ItemStack[furnace.getInventory().getSize()];
        this.burnTime = furnace.getBurnTime();
        this.cookTime = furnace.getCookTime();
        ItemStack stack;
        for (int i = 0; i < this.contents.length; i++) {
            stack = furnace.getInventory().getItem(i);
            if (stack == null) {
                this.contents[i] = null;
                continue;
            } else {
                this.contents[i] = stack.clone();
            }
        }
        furnace.getInventory().clear();
    }

    @Override
    public void paste(World world) {
        if (this.contents == null) {
            return;
        }

        Block block = world.getBlockAt(x, y, z);
        if (!block.getType().equals(Material.FURNACE) && !block.getType().equals(Material.BURNING_FURNACE)) {
            return;
        }

        Furnace furnace = (Furnace) block.getState();
        ItemStack stack;
        for (int i = 0; i < this.contents.length; i++) {
            stack = this.contents[i];
            if (stack == null) {
                continue;
            } else {
                furnace.getInventory().setItem(i, stack.clone());
            }
        }
        furnace.setBurnTime(this.burnTime);
        furnace.setCookTime(this.cookTime);
    }
}
