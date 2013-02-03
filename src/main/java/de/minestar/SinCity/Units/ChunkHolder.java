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

import java.util.HashMap;

import org.bukkit.Chunk;

public class ChunkHolder {

    private HashMap<Integer, OldChunk> chunkMap = new HashMap<Integer, OldChunk>();

    public OldChunk addChunk(Chunk chunk) {
        OldChunk oldChunk = new OldChunk(chunk);
        this.chunkMap.put(oldChunk.getCoordinates().hashCode(), oldChunk);
        return oldChunk;
    }

    public OldChunk getChunk(int x, int z) {
        ChunkCoordinates coordinates = new ChunkCoordinates(x, z);
        return this.chunkMap.get(coordinates.hashCode());
    }
}
