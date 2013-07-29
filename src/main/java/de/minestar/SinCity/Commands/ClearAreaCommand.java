package de.minestar.SinCity.Commands;

import org.bukkit.entity.Player;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Listener.SelectListener;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class ClearAreaCommand extends AbstractCommand {

    private SelectListener selectListener;

    public ClearAreaCommand(String syntax, String arguments, String node, SelectListener selectListener) {
        super(syntax, arguments, node);
        this.description = "Reset the current corners";
        this.selectListener = selectListener;
    }

    @Override
    public void execute(String[] args, Player player) {
        this.selectListener.clearAreaCorners(player);
        PlayerUtils.sendInfo(player, Core.NAME, "Corners cleared!");
    }
}
