package com.github.rephrasing.jadehomes.commands;

import com.github.rephrasing.galaxy.api.commands.AbstractGalaxyCommand;
import com.github.rephrasing.galaxy.api.commands.GalaxyCommandParams;
import com.github.rephrasing.galaxy.api.commands.GalaxyCommandPermissions;
import com.github.rephrasing.galaxy.api.commands.GalaxyCommandSource;
import com.github.rephrasing.galaxy.api.exceptions.GalaxyInitializationException;
import com.github.rephrasing.jadehomes.JadeHomesPlugin;
import com.github.rephrasing.jadehomes.cache.JadeCacheHandler;
import com.github.rephrasing.jadehomes.database.JadeHome;
import com.github.rephrasing.jadehomes.util.JadeHomesUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@GalaxyCommandParams(name = "jadehomes", aliases = "homes", description = "sends a list of homes if the executor has homes")
@GalaxyCommandPermissions(source = GalaxyCommandSource.IN_GAME)
public class JadeHomesList extends AbstractGalaxyCommand {

    public JadeHomesList() throws GalaxyInitializationException {
        super();
    }

    @Override
    protected Component execute(@NotNull CommandSender sender, @Nullable Player playerSender, @NotNull String[] args) {
        if (playerSender == null) return null; // means console is the one executing
        if (args.length != 0) return null;

        List<JadeHome> playerHomes = JadeCacheHandler.getInstance().getPlayerHomesData(playerSender).getPlayerHomes();
        if (playerHomes.size() == 0) {
            return JadeHomesPlugin.parseMessage("<red>You do not have any JadeHomes to display");
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Your homes list:\n");
        for (int i = 0; i < playerHomes.size(); i++) {
            JadeHome homeFromIndex = playerHomes.get(i);
            String enviro = "<gray>Unknown";
            switch (JadeHomesUtil.fetchLocationFromHome(homeFromIndex).getWorld().getEnvironment()) {
                case NORMAL -> enviro = "<green>Overworld";
                case NETHER -> enviro = "<red>Nether";
                case THE_END -> enviro = "<yellow>End";
            }

            builder.append(String.format("<gray>%d.</gray> <green>name:</green> <white>%s</white> <gold>x: <white>%d</white> <gold>y:</gold> <white>%d</white> <gold>z:</gold> <white>%d [<aqua>dimension</aqua>:</white> %s<white>]</white>" + (i != playerHomes.size() - 1 ? "\n" : ""),
                    i + 1, homeFromIndex.getName(), Math.round(homeFromIndex.getX()), Math.round(homeFromIndex.getY()), Math.round(homeFromIndex.getZ()), enviro));
        }

        return JadeHomesPlugin.parseMessage(builder.toString());
    }
}
