/*
 * Copyright (C) 2013 MineStar.de 
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

package de.minestar.SinCity.Units;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import de.minestar.SinCity.Core;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class SinCityArea {
    private final String areaName, worldName;
    private final Polygon polygon;

    public static SinCityArea load(String text) {
        String split[] = text.split(";");
        if (split.length < 5) {
            return null;
        }

        String worldName = split[0];
        String name = split[1];
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 2; i < split.length; i += 2) {
            try {
                int x, y;
                x = Integer.valueOf(split[i]);
                y = Integer.valueOf(split[i + 1]);
                points.add(new Point(x, y));
            } catch (Exception e) {
                ConsoleUtils.printError(Core.NAME, "Could not load Area: '" + name + "'!");
                return null;
            }
        }

        return new SinCityArea(worldName, name, points);
    }

    public SinCityArea(String worldName, String areaName, ArrayList<Point> points) {
        this.worldName = worldName;
        this.areaName = areaName;
        this.polygon = new Polygon();
        for (Point point : points) {
            this.polygon.addPoint(point.x, point.y);
        }
    }

    public boolean isInside(String worldName, int x, int z) {
        return this.polygon.contains(x, z) && this.worldName.equalsIgnoreCase(worldName);
    }

    @Override
    public String toString() {
        String value = this.worldName + ";" + this.areaName;
        if (this.polygon.npoints < 3) {
            return null;
        }
        for (int i = 0; i < this.polygon.npoints; i++) {
            value += ";" + this.polygon.xpoints[i] + ";" + this.polygon.ypoints[i];
        }
        return value;
    }

    public String getAreaName() {
        return areaName;
    }

}
