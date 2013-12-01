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

package de.minestar.SinCity.Units;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import de.minestar.SinCity.Units.blocks.ChestBlock;
import de.minestar.SinCity.Units.blocks.DispenserBlock;
import de.minestar.SinCity.Units.blocks.FurnaceBlock;
import de.minestar.SinCity.Units.blocks.ISpecialBlock;
import de.minestar.SinCity.Units.blocks.JukeBlock;
import de.minestar.SinCity.Units.blocks.NoteBlock;
import de.minestar.SinCity.Units.blocks.SignBlock;
import de.minestar.SinCity.Units.blocks.SkullBlock;

public class OldChunk {
    private final ChunkCoordinates coordinates;
    private final ChunkSnapshot snapshot;

    private ArrayList<ISpecialBlock> specialBlocks = new ArrayList<ISpecialBlock>();

    private static HashSet<Integer> blockList = new HashSet<Integer>(Arrays.asList(Material.CHEST.getId(), Material.SKULL.getId(), Material.JUKEBOX.getId(), Material.NOTE_BLOCK.getId(), Material.DISPENSER.getId(), Material.WALL_SIGN.getId(), Material.SIGN_POST.getId(), Material.BURNING_FURNACE.getId(), Material.FURNACE.getId()));

    public OldChunk(Chunk chunk) {
        this.coordinates = new ChunkCoordinates(chunk.getX(), chunk.getZ());
        this.snapshot = chunk.getChunkSnapshot(true, true, true);

        Block block;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    block = chunk.getBlock(x, y, z);
                    if (!blockList.contains(block.getTypeId())) {
                        continue;
                    }
                    Material mat = block.getType();
                    switch (mat) {
                        case CHEST :
                            this.specialBlocks.add(new ChestBlock(block));
                            break;
                        case WALL_SIGN :
                        case SIGN_POST :
                            this.specialBlocks.add(new SignBlock(block));
                            break;
                        case BURNING_FURNACE :
                        case FURNACE :
                            this.specialBlocks.add(new FurnaceBlock(block));
                            break;
                        case DISPENSER :
                            this.specialBlocks.add(new DispenserBlock(block));
                            break;
                        case NOTE_BLOCK :
                            this.specialBlocks.add(new NoteBlock(block));
                            break;
                        case JUKEBOX :
                            this.specialBlocks.add(new JukeBlock(block));
                            break;
                        case SKULL :
                            this.specialBlocks.add(new SkullBlock(block));
                            break;
                        default :
                            // do nothing
                    }
                    block.setType(Material.AIR);
                }
            }
        }
    }

    public void update(World world, int blockX, int blockY, int blockZ) {
        Chunk chunk = world.getChunkAt(this.getX(), this.getZ());
        Block block = chunk.getBlock(blockX, blockY, blockZ);
        if (block.getTypeId() != this.getTypeId(blockX, blockY, blockZ) || block.getData() != (byte) this.getSubId(blockX, blockY, blockZ)) {
            block.setTypeIdAndData(this.getTypeId(blockX, blockY, blockZ), (byte) this.getSubId(blockX, blockY, blockZ), false);
        }
        if (!block.getBiome().equals(this.getBiome(blockX, blockZ))) {
            block.setBiome(this.getBiome(blockX, blockZ));
        }
    }

    public void pasteSpecialBlocks(World world) {
        for (ISpecialBlock block : this.specialBlocks) {
            block.paste(world);
        }
    }

    private int getTypeId(int x, int y, int z) {
        return this.snapshot.getBlockTypeId(x, y, z);
    }

    private int getSubId(int x, int y, int z) {
        return this.snapshot.getBlockData(x, y, z);
    }

    private Biome getBiome(int x, int z) {
        return this.snapshot.getBiome(x, z);
    }

    public ChunkCoordinates getCoordinates() {
        return this.coordinates;
    }

    private int getX() {
        return this.coordinates.getX();
    }

    private int getZ() {
        return this.coordinates.getZ();
    }

    public int getBlockX() {
        return this.getBlockX(0);
    }

    public int getBlockX(int offset) {
        return (this.getX() << 4) + offset;
    }

    public int getBlockZ() {
        return this.getBlockZ(0);
    }

    public int getBlockZ(int offset) {
        return (this.getZ() << 4) + offset;
    }

    @Override
    public int hashCode() {
        return this.coordinates.hashCode();
    }
}
