package com.github.rephrasing.jadehomes;

import com.github.rephrasing.galaxy.GalaxyAPI;
import com.github.rephrasing.galaxy.api.exceptions.GalaxyInitializationException;
import com.github.rephrasing.jadehomes.database.JadeDatabase;
import com.github.rephrasing.jadehomes.cache.JadeCacheHandler;
import com.github.rephrasing.jadehomes.commands.core.JadeCommandHandler;
import com.github.rephrasing.stardust.MongoHolder;
import com.github.rephrasing.stardust.StardustPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;


public class JadeHomesPlugin extends JavaPlugin {

    private JadeDatabase jadeDatabase;
    private GalaxyAPI api;
    private MongoHolder stardustHolder;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.stardustHolder = StardustPlugin.getInstance().getMongoHolder();
        this.api = new GalaxyAPI(this);
        try {
            jadeDatabase = new JadeDatabase(stardustHolder); // init db
            new JadeCommandHandler(); // handles commands
        } catch (GalaxyInitializationException e) {
            e.printStackTrace();
        }
        JadeCacheHandler.setCachedData(jadeDatabase.getJadeHomesData()); // cache data from db
        sendConsoleMessage("<green>Enabled " + getName());
    }

    @Override
    public void onDisable() {
        jadeDatabase.saveJadeHomesData(JadeCacheHandler.getCachedData()); // save cached data to db
        sendConsoleMessage("<red>Disabled " + getName());
    }

    public GalaxyAPI getGalaxy() {
        return api;
    }

    public MongoHolder getStardustHolder() {
        return stardustHolder;
    }

    public void sendConsoleMessage(String message) {
        getServer().getConsoleSender().sendMessage(parseMessage(message));
    }

    public static Component parseMessage(String message) {
        return MiniMessage.miniMessage().deserialize("<white>[</white><green>JadeHomes</green><white>]</white> <gray>" + message);
    }
    public static JadeHomesPlugin getInstance() {
        return getPlugin(JadeHomesPlugin.class);
    }
}
