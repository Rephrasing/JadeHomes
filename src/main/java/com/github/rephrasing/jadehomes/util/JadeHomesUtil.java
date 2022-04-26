package com.github.rephrasing.jadehomes.util;

import com.github.rephrasing.jadehomes.database.JadeHome;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class JadeHomesUtil {

    public static Location fetchLocationFromHome(JadeHome home) {
        return new Location(Bukkit.getWorld(UUID.fromString(home.getWorldUniqueId())), home.getX(), home.getY(), home.getZ(), home.getYaw(), home.getPitch());
    }
}
