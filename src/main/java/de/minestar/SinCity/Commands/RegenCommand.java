package de.minestar.SinCity.Commands;

import org.bukkit.World;
import org.bukkit.entity.Player;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Listener.SelectListener;
import de.minestar.SinCity.Units.ChunkCoordinates;
import de.minestar.SinCity.Units.Selection;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class RegenCommand extends AbstractCommand {

    private SelectListener selectListener;

    public RegenCommand(String syntax, String arguments, String node, SelectListener selectListener) {
        super(syntax, arguments, node);
        this.description = "Regenerate the selected chunks";
        this.selectListener = selectListener;
    }

    @Override
    public void execute(String[] args, Player player) {
        Selection thisSelection = this.selectListener.getSelection(player);
        // WE NEED A SELECTION
        if (thisSelection == null) {
            PlayerUtils.sendError(player, Core.pluginName, "You must make a selection first!");
            return;
        }

        // SELECTION MUST BE VALID
        if (!thisSelection.isValid()) {
            PlayerUtils.sendError(player, Core.pluginName, "The current selection is not valid!");
            PlayerUtils.sendInfo(player, "Left click = Corner 1");
            PlayerUtils.sendInfo(player, "Right click = Corner 2");
            return;
        }

        // FINALLY REGENERATE THE CHUNKS
        ChunkCoordinates minChunk = thisSelection.getChunkOfCorner1();
        ChunkCoordinates maxChunk = thisSelection.getChunkOfCorner2();
        World world = thisSelection.getCorner1().getWorld();
        int chunkCount = 0;
        for (int x = minChunk.getX(); x <= maxChunk.getX(); x++) {
            for (int z = minChunk.getZ(); z <= maxChunk.getZ(); z++) {
                world.regenerateChunk(x, z);
                chunkCount++;
            }
        }
        PlayerUtils.sendSuccess(player, Core.pluginName, "Regenerated " + chunkCount + " chunks!");
    }
}
