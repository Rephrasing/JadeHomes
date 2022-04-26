package com.github.rephrasing.jadehomes.database;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class JadeHome {

    private String name;

    private double x, y, z;
    private float yaw, pitch;
    private String worldUniqueId;

    public JadeHome(Player player, String name) {
        this.name = name;
        Location loc = player.getLocation();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.worldUniqueId = loc.getWorld().getUID().toString();
    }

    public JadeHome() {} // for reflections


}
