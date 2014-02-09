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
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.ItemStack;

public class DispenserBlock implements ISpecialBlock {

    private final ItemStack[] contents;
    private final int x, y, z;

    public DispenserBlock(Block block) {
        x = block.getX();
        y = block.getY();
        z = block.getZ();
        if (!block.getType().equals(Material.DISPENSER)) {
            this.contents = null;
            return;
        }

        Dispenser dispenser = (Dispenser) block.getState();
        this.contents = new ItemStack[dispenser.getInventory().getSize()];
        ItemStack stack;
        for (int i = 0; i < this.contents.length; i++) {
            stack = dispenser.getInventory().getItem(i);
            if (stack == null) {
                this.contents[i] = null;
                continue;
            } else {
                this.contents[i] = stack.clone();
            }
        }
        dispenser.getInventory().clear();
    }

    @Override
    public void paste(World world) {
        if (this.contents == null) {
            return;
        }

        Block block = world.getBlockAt(x, y, z);
        if (!block.getType().equals(Material.DISPENSER)) {
            return;
        }

        Dispenser dispenser = (Dispenser) block.getState();
        ItemStack stack;
        for (int i = 0; i < this.contents.length; i++) {
            stack = this.contents[i];
            if (stack == null) {
                continue;
            } else {
                dispenser.getInventory().setItem(i, stack.clone());
            }
        }
    }
}
