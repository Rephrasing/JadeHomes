package com.github.rephrasing.jadehomes.database;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Setter
public class JadeHomesData {

    private String identifier;
    @Getter private List<JadeHome> playerHomes;

    public JadeHomesData(Player player) {
        this.identifier = player.getUniqueId().toString();
        this.playerHomes = new ArrayList<>();
    }

   public JadeHomesData() {
       if (playerHomes == null) playerHomes = new ArrayList<>();
   } // reflection


    public JadeHome getJadeHomeByName(String name) {
        if (playerHomes == null) playerHomes = new ArrayList<>();
        return playerHomes.stream().filter(jadeHome -> jadeHome.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public String getIdentifier() {
        return identifier;
    }

}
