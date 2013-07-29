package de.minestar.SinCity.Commands;

import org.bukkit.entity.Player;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Listener.SelectListener;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class SaveAreaCommand extends AbstractCommand {

    private SelectListener selectListener;

    public SaveAreaCommand(String syntax, String arguments, String node, SelectListener selectListener) {
        super(syntax, arguments, node);
        this.description = "Save the area";
        this.selectListener = selectListener;
    }

    @Override
    public void execute(String[] args, Player player) {
        if (this.selectListener.saveArea(player, args[0])) {
            PlayerUtils.sendInfo(player, Core.NAME, "Area saved!");
        } else {
            PlayerUtils.sendError(player, Core.NAME, "Could not save area!");
        }
    }
}
