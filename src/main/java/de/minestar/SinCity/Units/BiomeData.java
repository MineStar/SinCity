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

package de.minestar.SinCity.Units;

import org.bukkit.block.Biome;

public class BiomeData {
    private final Biome biome;
    private final int radius;

    public BiomeData(Biome biome, int radius) {
        this.biome = biome;
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public Biome getBiome() {
        return biome;
    }

}
