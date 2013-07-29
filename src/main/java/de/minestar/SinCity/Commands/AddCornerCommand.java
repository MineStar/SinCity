package de.minestar.SinCity.Commands;

import org.bukkit.entity.Player;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Listener.SelectListener;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class AddCornerCommand extends AbstractCommand {

    private SelectListener selectListener;

    public AddCornerCommand(String syntax, String arguments, String node, SelectListener selectListener) {
        super(syntax, arguments, node);
        this.description = "Add a corner to your list.";
        this.selectListener = selectListener;
    }

    @Override
    public void execute(String[] args, Player player) {
        int amount = this.selectListener.addAreaCorner(player, player.getLocation().getBlockX(), player.getLocation().getBlockZ());
        PlayerUtils.sendInfo(player, Core.NAME, "Corners in current selection: " + amount);
    }
}
