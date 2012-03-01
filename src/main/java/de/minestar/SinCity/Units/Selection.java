package de.minestar.SinCity.Units;

import org.bukkit.Location;

public class Selection {
    private Location corner1, corner2;

    public void setCorner1(Location location) {
        if (corner2 == null) {
            this.corner1 = location.clone();
            return;
        }

        int minX, maxX, minY, maxY, minZ, maxZ;
        minX = Math.min(location.getBlockX(), corner2.getBlockX());
        maxX = Math.max(location.getBlockX(), corner2.getBlockX());

        minY = Math.min(location.getBlockY(), corner2.getBlockY());
        maxY = Math.max(location.getBlockY(), corner2.getBlockY());

        minZ = Math.min(location.getBlockZ(), corner2.getBlockZ());
        maxZ = Math.max(location.getBlockZ(), corner2.getBlockZ());
        this.corner1 = new Location(location.getWorld(), minX, minY, minZ);
        this.corner2 = new Location(location.getWorld(), maxX, maxY, maxZ);
    }

    public ChunkCoordinates getChunkOfCorner1() {
        return this.getChunkCoordinates(corner1);
    }

    public ChunkCoordinates getChunkOfCorner2() {
        return this.getChunkCoordinates(corner2);
    }

    private ChunkCoordinates getChunkCoordinates(Location location) {
        if (location == null)
            return null;
        return new ChunkCoordinates(location);
    }

    public void setCorner2(Location location) {
        if (corner1 == null) {
            this.corner2 = location.clone();
            return;
        }

        int minX, maxX, minY, maxY, minZ, maxZ;
        minX = Math.min(location.getBlockX(), corner1.getBlockX());
        maxX = Math.max(location.getBlockX(), corner1.getBlockX());

        minY = Math.min(location.getBlockY(), corner1.getBlockY());
        maxY = Math.max(location.getBlockY(), corner1.getBlockY());

        minZ = Math.min(location.getBlockZ(), corner1.getBlockZ());
        maxZ = Math.max(location.getBlockZ(), corner1.getBlockZ());
        this.corner1 = new Location(location.getWorld(), minX, minY, minZ);
        this.corner2 = new Location(location.getWorld(), maxX, maxY, maxZ);
    }

    public boolean isValid() {
        return this.corner1 != null && this.corner2 != null;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }
}
