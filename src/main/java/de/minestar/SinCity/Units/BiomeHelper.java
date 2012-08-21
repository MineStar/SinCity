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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BiomeHelper {
    private static final double lengthSq(double x, double z) {
        return x * x + z * z;
    }

    public static void convertBiome(Player player, Location loc, Biome biome, double radius) {
        int height = 1;

        radius += 0.5D;

        if (height == 0)
            return;
        if (height < 0) {
            height = -height;
            loc = loc.subtract(0, height, 0);
        }

        if (loc.getBlockY() < 0)
            loc.setY(0d);
        else if (loc.getBlockY() + height - 1 > loc.getWorld().getMaxHeight()) {
            height = loc.getWorld().getMaxHeight() - loc.getBlockY() + 1;
        }

        double invRadiusX = 1.0D / radius;
        double invRadiusZ = 1.0D / radius;

        int ceilRadiusX = (int) Math.ceil(radius);
        int ceilRadiusZ = (int) Math.ceil(radius);

        double nextXn = 0.0D;
        for (int x = 0; x <= ceilRadiusX; x++) {
            double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextZn = 0.0D;
            for (int z = 0; z <= ceilRadiusZ; z++) {
                double zn = nextZn;
                nextZn = (z + 1) * invRadiusZ;

                double distanceSq = lengthSq(xn, zn);
                if (distanceSq > 1.0D) {
                    if (z != 0)
                        break;
                    return;
                }

                setBiome(loc.getBlock().getRelative(x, 0, z), biome, player);
                setBiome(loc.getBlock().getRelative(-x, 0, z), biome, player);
                setBiome(loc.getBlock().getRelative(x, 0, -z), biome, player);
                setBiome(loc.getBlock().getRelative(-x, 0, -z), biome, player);
            }
        }
    }

    private static void setBiome(Block block, Biome biome, Player player) {
        block.getWorld().setBiome(block.getX(), block.getZ(), biome);
        player.sendBlockChange(block.getRelative(0, -5, 0).getLocation(), Material.GLASS.getId(), (byte) 0);
    }
}
