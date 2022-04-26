package com.github.rephrasing.jadehomes.cache;

import com.github.rephrasing.jadehomes.JadeHomesPlugin;
import com.github.rephrasing.jadehomes.database.JadeHome;
import com.github.rephrasing.jadehomes.database.JadeHomesData;
import com.github.rephrasing.jadehomes.util.JadeHomesUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JadeCacheHandler {

    private static List<JadeHomesData> cachedData;
    private static JadeCacheHandler instance;

    private JadeCacheHandler() {}

    public static JadeCacheHandler getInstance() {
        if (instance == null) instance = new JadeCacheHandler();
        return instance;
    }
    public static void setCachedData(List<JadeHomesData> cachedData) {
        JadeCacheHandler.cachedData = cachedData;
    }
    public static List<JadeHomesData> getCachedData() {
        return cachedData;
    }

    @NotNull
    public JadeHomesData getPlayerHomesData(Player player) {
        for (JadeHomesData data : cachedData) {
            if (data.getIdentifier().equals(player.getUniqueId().toString())) return data;
        }
        JadeHomesData toReturn = new JadeHomesData(player);
        cachedData.add(toReturn);
        return toReturn;
    }

    /**
     * Replaces a JadeHomesData by its identifier with a new one
     * @param identifier the old data identifier
     * @param newData the new data
     * @return true if replaced, otherwise false. and it gets added
     */
    private boolean replaceHomesData(String identifier, JadeHomesData newData) {
        boolean isRemoved = cachedData.removeIf(data -> data.getIdentifier().equalsIgnoreCase(identifier));
        cachedData.add(newData);
        return isRemoved;
    }

    public JadeResponse createJadeHome(Player player, String homeName) {
        JadeHomesData data = getPlayerHomesData(player);
        JadeHome home = data.getJadeHomeByName(homeName);
        if (home != null) return JadeResponse.HOME_EXISTS;
        int max = JadeHomesPlugin.getInstance().getConfig().getInt("max-homes-per-player");
        if (data.getPlayerHomes().size() >= max) return JadeResponse.MAX_VALUE_EXCEEDED;
        data.getPlayerHomes().add(new JadeHome(player, homeName));
        replaceHomesData(data.getIdentifier(), data);
        return JadeResponse.APPROVED;
    }

    public JadeResponse deleteJadeHome(Player player, String homeName) {
        JadeHome home = getPlayerHomesData(player).getJadeHomeByName(homeName);
        if (home == null) {
            return JadeResponse.HOME_NOT_FOUND;
        }
        JadeHomesData data = getPlayerHomesData(player);
        data.getPlayerHomes().remove(home);
        replaceHomesData(data.getIdentifier(), data);
        return JadeResponse.APPROVED;
    }
    public JadeResponse renameJadeHome(Player player, String oldName, String homeName) {
        JadeHome home = getPlayerHomesData(player).getJadeHomeByName(oldName);
        if (home == null) {
            return JadeResponse.HOME_NOT_FOUND;
        }
        JadeHomesData data = getPlayerHomesData(player);
        data.getJadeHomeByName(oldName).setName(homeName);
        replaceHomesData(data.getIdentifier(), data);
        return JadeResponse.APPROVED;
    }
    public JadeResponse teleportToJadeHome(Player player, String homeName) {
        JadeHome home = getPlayerHomesData(player).getJadeHomeByName(homeName);
        if (home == null) return JadeResponse.HOME_NOT_FOUND;
        boolean tpThruDimensions = JadeHomesPlugin.getInstance().getConfig().getBoolean("teleport-through-dimensions");
        if (!tpThruDimensions) {
            if (!player.getWorld().getUID().toString().equals(home.getWorldUniqueId())) return JadeResponse.DECLINED_DIMENSION;
        }
        player.teleport(JadeHomesUtil.fetchLocationFromHome(home));
        return JadeResponse.APPROVED;
    }
}
