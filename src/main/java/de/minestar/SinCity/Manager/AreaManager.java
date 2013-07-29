/*
 * Copyright (C) 2013 MineStar.de tj
 * 
 * This file is part of SinCity.
 * 
 * SinCity is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * SinCity is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SinCity.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.SinCity.Manager;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import de.minestar.SinCity.Core;
import de.minestar.SinCity.Units.SinCityArea;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class AreaManager {
    private ArrayList<SinCityArea> areaList = new ArrayList<SinCityArea>();

    private final String fileName = "probezonen.dat";
    private Core core;

    public AreaManager(Core core) {
        this.core = core;
    }

    public boolean addArea(String worldName, String areaName, ArrayList<Point> points) {
        // we need at least 3 points
        if (points == null || points.size() < 3) {
            return false;
        }

        // check double existance of areanames
        for (SinCityArea area : this.areaList) {
            if (area.getAreaName().equalsIgnoreCase(areaName)) {
                return false;
            }
        }

        this.areaList.add(new SinCityArea(worldName, areaName, points));
        this.saveAreas();
        return true;
    }

    private void saveAreas() {
        File file = new File(core.getDataFolder(), this.fileName);
        if (file.exists()) {
            file.delete();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (SinCityArea area : this.areaList) {
                String string = area.toString();
                if (string != null) {
                    try {
                        writer.write(string);
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public boolean isInside(String worldName, int x, int z) {
        for (SinCityArea area : this.areaList) {
            if (area.isInside(worldName, x, z)) {
                return true;
            }
        }
        return false;
    }

    public int loadAreas() {
        areaList = new ArrayList<SinCityArea>();
        File file = new File(core.getDataFolder(), fileName);
        if (!file.exists()) {
            return 0;
        }

        int count = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String inLine = null;
            while ((inLine = reader.readLine()) != null) {
                inLine = inLine.trim();
                SinCityArea area = SinCityArea.load(inLine);
                if (area != null) {
                    this.areaList.add(area);
                    count++;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean deleteArea(String areaName) {
        int index = -1;
        int counter = 0;
        for (SinCityArea area : this.areaList) {
            if (area.getAreaName().equalsIgnoreCase(areaName)) {
                index = counter;
                break;
            }
            counter++;
        }

        if (index != -1) {
            this.areaList.remove(index);
            this.saveAreas();
        }

        return index != -1;
    }

    public void listAreas(Player player) {
        PlayerUtils.sendInfo(player, Core.NAME, "List of Areas:");
        for (SinCityArea area : this.areaList) {
            PlayerUtils.sendInfo(player, "-> " + area.getAreaName());
        }
    }
}
