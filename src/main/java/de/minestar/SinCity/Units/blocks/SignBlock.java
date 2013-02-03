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
import org.bukkit.block.Sign;

public class SignBlock implements ISpecialBlock {

    private final String[] contents;
    private final int x, y, z;

    public SignBlock(Block block) {
        x = block.getX();
        y = block.getY();
        z = block.getZ();
        if (block.getTypeId() != Material.WALL_SIGN.getId() && block.getTypeId() != Material.SIGN_POST.getId()) {
            this.contents = null;
            return;
        }

        Sign sign = (Sign) block.getState();
        this.contents = new String[4];
        for (int i = 0; i < this.contents.length; i++) {
            this.contents[i] = sign.getLine(i);
        }
    }

    @Override
    public void paste(World world) {
        if (this.contents == null) {
            return;
        }

        Block block = world.getBlockAt(x, y, z);
        if (block.getTypeId() != Material.WALL_SIGN.getId() && block.getTypeId() != Material.SIGN_POST.getId()) {
            return;
        }

        Sign sign = (Sign) block.getState();
        String line;
        for (int i = 0; i < this.contents.length; i++) {
            line = this.contents[i];
            if (line == null) {
                continue;
            } else {
                sign.setLine(i, line);
            }
        }
        sign.update(true);
    }
}
