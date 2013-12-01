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
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.block.Block;

public class NoteBlock implements ISpecialBlock {

    private final int x, y, z;
    private Note note;

    public NoteBlock(Block block) {
        x = block.getX();
        y = block.getY();
        z = block.getZ();
        if (!block.getType().equals(Material.NOTE_BLOCK))
            return;

        org.bukkit.block.NoteBlock noteBlock = (org.bukkit.block.NoteBlock) block.getState();
        this.note = noteBlock.getNote();
    }

    @Override
    public void paste(World world) {
        Block block = world.getBlockAt(x, y, z);
        if (!block.getType().equals(Material.NOTE_BLOCK))
            return;

        org.bukkit.block.NoteBlock noteBlock = (org.bukkit.block.NoteBlock) block.getState();
        noteBlock.setNote(this.note);
    }
}
