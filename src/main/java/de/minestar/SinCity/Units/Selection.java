package de.minestar.SinCity.Units;

import org.bukkit.Location;

public class Selection {
    private Location corner1, corner2;

    public void setCorner1(Location location) {
        this.corner1 = location.clone();
    }

    public void setCorner2(Location location) {
        this.corner2 = location.clone();
    }

    public Location getMinLocation() {
        if (!this.isValid())
            return null;

        int minX, minY, minZ;
        minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        return new Location(corner1.getWorld(), minX, minY, minZ);
    }

    public Location getMaxLocation() {
        if (!this.isValid())
            return null;

        int maxX, maxY, maxZ;
        maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
        return new Location(corner1.getWorld(), maxX, maxY, maxZ);
    }

    public ChunkCoordinates getChunkOfCorner1() {
        return this.getChunkCoordinates(this.getMinLocation());
    }

    public ChunkCoordinates getChunkOfCorner2() {
        return this.getChunkCoordinates(this.getMaxLocation());
    }

    private ChunkCoordinates getChunkCoordinates(Location location) {
        if (location == null)
            return null;
        return new ChunkCoordinates(location);
    }

    public boolean isValid() {
        return this.corner1 != null && this.corner2 != null && this.corner1.getWorld().getName().equalsIgnoreCase(this.corner2.getWorld().getName());
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }
}
