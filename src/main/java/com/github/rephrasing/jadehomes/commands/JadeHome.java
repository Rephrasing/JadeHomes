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

@GalaxyCommandParams(name = "jadehome", aliases = "home", description = "allows home teleportations")
@GalaxyCommandPermissions(source = GalaxyCommandSource.IN_GAME)
public class JadeHome extends AbstractGalaxyCommand {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public JadeHome() throws GalaxyInitializationException {
        super();
    }

    @Override
    protected Component execute(@NotNull CommandSender sender, @Nullable Player playerSender, @NotNull String[] args) {
        if (playerSender == null) return null; // means console is the one executing
        if (args.length != 1) return null;

        if (cooldowns.containsKey(playerSender.getUniqueId())) {
            Long secondsLeft = getCooldownByTimestamp(cooldowns.get(playerSender.getUniqueId()));
            if (secondsLeft != null) {
                return JadeHomesPlugin.parseMessage("<red>You are still cooling down. You may use this command in " + secondsLeft + " seconds");
            }
        }
        JadeResponse response = JadeCacheHandler.getInstance().teleportToJadeHome(playerSender, args[0]);
        if (response == JadeResponse.HOME_NOT_FOUND) {
            return JadeHomesPlugin.parseMessage("<red>You do not have a JadeHome with the name </red><yellow>" + args[0]);
        } else if (response == JadeResponse.DECLINED_DIMENSION) {
            return JadeHomesPlugin.parseMessage("<red>Teleporting through dimensions is disabled by the server.");
        }
        cooldowns.put(playerSender.getUniqueId(), System.currentTimeMillis());
        return JadeHomesPlugin.parseMessage("You were teleported to your home with the name <yellow>" + args[0]);
    }

    private Long getCooldownByTimestamp(Long lastTime) {
        long secondsLeft = ((lastTime/1000)+JadeHomesPlugin.getInstance().getConfig().getInt("cooldowns.jhome")) - (System.currentTimeMillis()/1000);
        if(secondsLeft>0) return secondsLeft;
        return null;
    }
}
