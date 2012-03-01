package de.minestar.SinCity.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class DataManager {
    private HashSet<String> groupsDenyAll, worldsDenyAll;
    private HashSet<String> groupsDenyPartial, worldsDenyPartial;
    private HashMap<String, Long> maxAFKTimes;

    public DataManager(File dataFolder) {
        dataFolder.mkdirs();
        this.groupsDenyAll = this.loadData(dataFolder, "denyAll.yml");
        this.groupsDenyPartial = this.loadData(dataFolder, "denyPartial.yml");
        this.worldsDenyAll = this.loadData(dataFolder, "worldsDenyAll.yml");
        this.worldsDenyPartial = this.loadData(dataFolder, "worldsDenyPartial.yml");
        this.maxAFKTimes = this.loadAFKTimes(dataFolder, "AFKTimes.yml");
    }

    public Long getMaxAFKTime(String groupName) {
        if (this.maxAFKTimes.containsKey(groupName))
            return this.maxAFKTimes.get(groupName);
        else
            return -1l;
    }

    private boolean isInList(String groupName, String worldName, HashSet<String> groupList, HashSet<String> worldList) {
        return groupList.contains(groupName) && worldList.contains(worldName.toLowerCase());
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
                this.createFreshList(dataFolder, fileName, false);
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

    private HashMap<String, Long> loadAFKTimes(File dataFolder, String fileName) {
        HashMap<String, Long> dataList = new HashMap<String, Long>();
        try {
            File file = new File(dataFolder, fileName);
            if (!file.exists()) {
                this.createFreshList(dataFolder, fileName, true);
            }

            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            List<String> stringList = config.getStringList("data");
            if (stringList != null) {
                for (String group : stringList) {
                    String[] split = group.split(":");
                    if (split.length != 2)
                        continue;

                    dataList.put(split[0], Long.valueOf(split[1]) * 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private void createFreshList(File dataFolder, String fileName, boolean AFKTimes) {
        try {
            File file = new File(dataFolder, fileName);
            if (file.exists()) {
                return;
            }

            YamlConfiguration config = new YamlConfiguration();
            List<String> stringList = new ArrayList<String>();
            if (!AFKTimes) {
                stringList.add("x");
                stringList.add("probe");
                stringList.add("default");
            } else {
                stringList.add("x:5");
                stringList.add("probe:10");
                stringList.add("default:5");
                stringList.add("vip:10");
                stringList.add("pay:10");
            }
            config.set("data", stringList);
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
