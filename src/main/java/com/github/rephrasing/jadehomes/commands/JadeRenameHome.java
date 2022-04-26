package com.github.rephrasing.jadehomes.commands;

import com.github.rephrasing.galaxy.api.commands.AbstractGalaxyCommand;
import com.github.rephrasing.galaxy.api.commands.GalaxyCommandParams;
import com.github.rephrasing.galaxy.api.commands.GalaxyCommandPermissions;
import com.github.rephrasing.galaxy.api.commands.GalaxyCommandSource;
import com.github.rephrasing.galaxy.api.exceptions.GalaxyInitializationException;
import com.github.rephrasing.jadehomes.JadeHomesPlugin;
import com.github.rephrasing.jadehomes.cache.JadeCacheHandler;
import com.github.rephrasing.jadehomes.cache.JadeResponse;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

@GalaxyCommandParams(name = "jaderenamehome", aliases = "renamehome", description = "allows home renaming")
@GalaxyCommandPermissions(source = GalaxyCommandSource.IN_GAME)
public class JadeRenameHome extends AbstractGalaxyCommand {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public JadeRenameHome() throws GalaxyInitializationException {
        super();
    }

    @Override
    protected Component execute(@NotNull CommandSender sender, @Nullable Player playerSender, @NotNull String[] args) {
        if (playerSender == null) return null; // means console is the one executing
        if (args.length != 2) return null;

        if (cooldowns.containsKey(playerSender.getUniqueId())) {
            Long secondsLeft = getCooldownByTimestamp(cooldowns.get(playerSender.getUniqueId()));
            if (secondsLeft != null) {
                return JadeHomesPlugin.parseMessage("<red>You are still cooling down. You may use this command in " + secondsLeft + " seconds");
            }
        }
        JadeResponse response = JadeCacheHandler.getInstance().renameJadeHome(playerSender, args[0], args[1]);

        if (response == JadeResponse.HOME_NOT_FOUND) {
            return JadeHomesPlugin.parseMessage(String.format("<red>You do not have a home with the name </red><yellow>%s</yellow>", args[0]));
        }
        cooldowns.put(playerSender.getUniqueId(), System.currentTimeMillis());
        return JadeHomesPlugin.parseMessage(String.format("<gray>You have successfully renamed your JadeHome from</gray> <aqua>%s</aqua> to <yellow>%s</yellow>", args[0], args[1]));
    }

    private Long getCooldownByTimestamp(Long lastTime) {
        long secondsLeft = ((lastTime/1000)+ JadeHomesPlugin.getInstance().getConfig().getInt("cooldowns.jsethome")) - (System.currentTimeMillis()/1000);
        if(secondsLeft>0) return secondsLeft;
        return null;
    }
}
