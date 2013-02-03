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

import org.bukkit.block.Biome;

public class BlockContainer {
    private int[][][] typeIDs, subIDs;
    private Biome[][] biomes;

    public BlockContainer(int dX, int dY, int dZ) {
        this.typeIDs = new int[dX][dY][dZ];
        this.subIDs = new int[dX][dY][dZ];
        this.biomes = new Biome[dX][dZ];
    }

    public void set(int x, int y, int z, int typeID, int subID, Biome biome) {
        this.typeIDs[x][y][z] = typeID;
        this.subIDs[x][y][z] = subID;
        this.biomes[x][z] = biome;
    }

    public int getTypeID(int x, int y, int z) {
        return this.typeIDs[x][y][z];
    }

    public int getSubID(int x, int y, int z) {
        return this.subIDs[x][y][z];
    }

    public Biome getBiome(int x, int z) {
        return this.biomes[x][z];
    }
}
