package com.github.rephrasing.jadehomes.database;

import com.github.rephrasing.galaxy.api.database.AbstractGalaxyDatabase;
import com.github.rephrasing.galaxy.api.exceptions.GalaxyInitializationException;
import com.github.rephrasing.jadehomes.JadeHomesPlugin;
import com.github.rephrasing.stardust.MongoHolder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;
import java.util.List;


public class JadeDatabase extends AbstractGalaxyDatabase {

    public JadeDatabase(MongoHolder holder) throws GalaxyInitializationException {
        super(holder);
    }

    public MongoCollection<JadeHomesData> getCollection() {
        return getDatabase().getCollection("PlayerJadeHomes", JadeHomesData.class);
    }


    public List<JadeHomesData> getJadeHomesData() {
        List<JadeHomesData> list = new ArrayList<>();
        for (JadeHomesData data : getCollection().find()) {
            list.add(data);
        }
        if (list.size() != 0) {
            int homesSize = 0;
            for (JadeHomesData dataFromList : list) {
                if (dataFromList.getPlayerHomes() != null) homesSize += dataFromList.getPlayerHomes().size();
            }
            JadeHomesPlugin.getInstance().sendConsoleMessage(String.format("<green>Successfully pulled data from <dark_purple>Stardust</dark_purple> (Total: %s JadeHomes)", homesSize));
            return list;
        }
        JadeHomesPlugin.getInstance().sendConsoleMessage("<red>Pulled 0 data from Stardust");
        return list;
    }

    public void saveJadeHomesData(List<JadeHomesData> data) {
        int homesUpdated = 0;
        for (JadeHomesData provided : data) {
            getCollection().deleteOne(Filters.eq("identifier", provided.getIdentifier()));
            getCollection().insertOne(provided);
            homesUpdated += provided.getPlayerHomes().size();
        }
        if (homesUpdated > 0) JadeHomesPlugin.getInstance().sendConsoleMessage(String.format("<green>Successfully pushed %s JadeHomes to <dark_purple>Stardust", homesUpdated));
    }


}
