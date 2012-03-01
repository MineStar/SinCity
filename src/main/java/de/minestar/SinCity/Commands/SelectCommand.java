package de.minestar.SinCity.Commands;

import org.bukkit.entity.Player;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Listener.SelectListener;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class SelectCommand extends AbstractCommand {

    private SelectListener selectListener;

    public SelectCommand(String syntax, String arguments, String node, SelectListener selectListener) {
        super(syntax, arguments, node);
        this.description = "Toggle selectmode";
        this.selectListener = selectListener;
    }

    @Override
    public void execute(String[] args, Player player) {
        if (this.selectListener.toggleSelectMode(player)) {
            PlayerUtils.sendInfo(player, Core.pluginName, "You are now in selectmode!");
        } else {
            PlayerUtils.sendInfo(player, Core.pluginName, "You are no longer in selectmode!");
        }
    }
}
