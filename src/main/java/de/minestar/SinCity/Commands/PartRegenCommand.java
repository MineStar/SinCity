package de.minestar.SinCity.Commands;

import org.bukkit.World;
import org.bukkit.entity.Player;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Listener.SelectListener;
import de.minestar.SinCity.Units.ChunkCoordinates;
import de.minestar.SinCity.Units.ChunkHolder;
import de.minestar.SinCity.Units.OldChunk;
import de.minestar.SinCity.Units.Selection;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class PartRegenCommand extends AbstractCommand {

    private SelectListener selectListener;

    public PartRegenCommand(String syntax, String arguments, String node, SelectListener selectListener) {
        super(syntax, arguments, node);
        this.description = "Regenerate the selected blocks";
        this.selectListener = selectListener;
    }

    @Override
    public void execute(String[] args, Player player) {
        Selection thisSelection = this.selectListener.getSelection(player);
        // WE NEED A SELECTION
        if (thisSelection == null) {
            PlayerUtils.sendError(player, Core.NAME, "You must make a selection first!");
            return;
        }

        // SELECTION MUST BE VALID
        if (!thisSelection.isValid()) {
            PlayerUtils.sendError(player, Core.NAME, "The current selection is not valid!");
            PlayerUtils.sendInfo(player, "Left click = Corner 1");
            PlayerUtils.sendInfo(player, "Right click = Corner 2");
            return;
        }

        // init vars
        ChunkHolder holder = new ChunkHolder();
        ChunkCoordinates minChunk = thisSelection.getChunkOfCorner1();
        ChunkCoordinates maxChunk = thisSelection.getChunkOfCorner2();
        World world = thisSelection.getCorner1().getWorld();
        int blockCount = 0;

        // save the current blockData
        int minX = thisSelection.getMinLocation().getBlockX();
        int minY = thisSelection.getMinLocation().getBlockY();
        int minZ = thisSelection.getMinLocation().getBlockZ();

        int maxX = thisSelection.getMaxLocation().getBlockX();
        int maxY = thisSelection.getMaxLocation().getBlockY();
        int maxZ = thisSelection.getMaxLocation().getBlockZ();

        // save data
        for (int x = minChunk.getX(); x <= maxChunk.getX(); x++) {
            for (int z = minChunk.getZ(); z <= maxChunk.getZ(); z++) {
                holder.addChunk(world.getChunkAt(x, z));
            }
        }

        // regenerate chunks
        for (int x = minChunk.getX(); x <= maxChunk.getX(); x++) {
            for (int z = minChunk.getZ(); z <= maxChunk.getZ(); z++) {
                world.regenerateChunk(x, z);
            }
        }

        // reset data, not in the selection
        OldChunk current;
        for (int x = minChunk.getX(); x <= maxChunk.getX(); x++) {
            for (int z = minChunk.getZ(); z <= maxChunk.getZ(); z++) {
                current = holder.getChunk(x, z);
                if (current == null) {
                    continue;
                }
                for (int bX = 0; bX < 16; bX++) {
                    for (int bZ = 0; bZ < 16; bZ++) {
                        for (int bY = 0; bY < 0256; bY++) {
                            if (!this.shouldBeRegenerated(current.getBlockX(bX), bY, current.getBlockZ(bZ), minX, maxX, minY, maxY, minZ, maxZ)) {
                                current.update(world, bX, bY, bZ);
                            } else {
                                blockCount++;
                            }
                        }
                    }
                }
            }
        }

        // regenerate chunks
        for (int x = minChunk.getX(); x <= maxChunk.getX(); x++) {
            for (int z = minChunk.getZ(); z <= maxChunk.getZ(); z++) {
                current = holder.getChunk(x, z);
                if (current == null) {
                    continue;
                }
                current.pasteSpecialBlocks(world);
            }
        }

        PlayerUtils.sendSuccess(player, Core.NAME, "Regenerated " + blockCount + " blocks!");
    }

    private boolean shouldBeRegenerated(int x, int y, int z, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }
}
