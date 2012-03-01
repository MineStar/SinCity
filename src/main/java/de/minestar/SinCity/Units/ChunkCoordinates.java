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
}
