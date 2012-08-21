package de.minestar.SinCity.Commands;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Listener.SelectListener;
import de.minestar.SinCity.Units.BiomeData;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class BiomeCommand extends AbstractCommand {

    private SelectListener selectListener;

    public BiomeCommand(String syntax, String arguments, String node, SelectListener selectListener) {
        super(syntax, arguments, node);
        this.description = "Toggle biomemode";
        this.selectListener = selectListener;
    }

    @Override
    public void execute(String[] args, Player player) {
        Biome biome = null;

        for (Biome biomes : Biome.values()) {
            if (biomes.name().equalsIgnoreCase(args[0])) {
                biome = biomes;
                break;
            }
        }

        if (biome == null || args.length < 2) {
            this.selectListener.removeBiomeData(player.getName());
            PlayerUtils.sendSuccess(player, Core.NAME, "Biomemode removed!");
            for (Biome biomes : Biome.values()) {
                PlayerUtils.sendInfo(player, biomes.name());
            }
        } else {
            int radius = 5;
            try {
                radius = Integer.valueOf(args[1]);
            } catch (Exception e) {

            }
            BiomeData data = new BiomeData(biome, radius);
            this.selectListener.setBiomeData(player.getName(), data);
            PlayerUtils.sendSuccess(player, Core.NAME, "Biomemode set to biome: " + biome.name() + " - radius: " + radius);
        }
    }
}
