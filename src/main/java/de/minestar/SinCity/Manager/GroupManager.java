package de.minestar.SinCity.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class GroupManager {
    private HashSet<String> groupsDenyAll, worldsDenyAll;
    private HashSet<String> groupsDenyPartial, worldsDenyPartial;

    public GroupManager(File dataFolder) {
        dataFolder.mkdirs();
        this.groupsDenyAll = this.loadData(dataFolder, "denyAll.yml");
        this.groupsDenyPartial = this.loadData(dataFolder, "denyPartial.yml");
        this.worldsDenyAll = this.loadData(dataFolder, "worldsDenyAll.yml");
        this.worldsDenyPartial = this.loadData(dataFolder, "worldsDenyPartial.yml");
    }

    private boolean isInList(String groupName, String worldName, HashSet<String> groupList, HashSet<String> worldList) {
        return groupList.contains(groupName.toLowerCase()) && worldList.contains(worldName.toLowerCase());
    }

    public boolean isInDenyAll(String groupName, String worldName) {
        return this.isInList(groupName, worldName, this.groupsDenyAll, this.worldsDenyAll);
    }

    public boolean isInDenyPartial(String groupName, String worldName) {
        return this.isInList(groupName, worldName, this.groupsDenyPartial, this.worldsDenyPartial);
    }

    private HashSet<String> loadData(File dataFolder, String fileName) {
        HashSet<String> dataList = new HashSet<String>();
        try {
            File file = new File(dataFolder, fileName);
            if (!file.exists()) {
                this.createFreshList(dataFolder, fileName);
            }

            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            List<String> stringList = config.getStringList("data");
            if (stringList != null) {
                for (String group : stringList) {
                    dataList.add(group.trim().toLowerCase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private void createFreshList(File dataFolder, String fileName) {
        try {
            File file = new File(dataFolder, fileName);
            if (file.exists()) {
                return;
            }

            YamlConfiguration config = new YamlConfiguration();
            List<String> stringList = new ArrayList<String>();
            stringList.add("x");
            stringList.add("probe");
            stringList.add("default");
            config.set("data", stringList);
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
