package de.minestar.SinCity.Units;

import org.bukkit.Location;

public class ChunkCoordinates {
    private int x, z;

    public ChunkCoordinates(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public ChunkCoordinates(Location location) {
        this(location.getChunk().getX(), location.getChunk().getZ());
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        long bits = java.lang.Double.doubleToLongBits(getX());
        bits ^= java.lang.Double.doubleToLongBits(getZ()) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }
}
