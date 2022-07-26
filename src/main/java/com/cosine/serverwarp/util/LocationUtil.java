package com.cosine.serverwarp.util;

import org.bukkit.Location;

public class LocationUtil {

    public static String locationToString(Location location) {
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float pitch = location.getPitch();
        float yaw = location.getYaw();
        return world + "," + x + "," + y + "," + z + "," + pitch + "," + yaw;
    }
    public static String getLocationToString(String location, String choice) {
        String[] array = location.split(",");
        if (choice.equals("world")) {
            return array[0];
        }
        if (choice.equals("x")) {
            return array[1];
        }
        if (choice.equals("y")) {
            return array[2];
        }
        if (choice.equals("z")) {
            return array[3];
        }
        if (choice.equals("pitch")) {
            return array[4];
        }
        if (choice.equals("yaw")) {
            return array[5];
        }
        return "";
    }
}
