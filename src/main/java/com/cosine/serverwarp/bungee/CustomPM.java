package com.cosine.serverwarp.bungee;

import com.cosine.serverwarp.main.ServerWarp;
import com.cosine.serverwarp.util.LocationUtil;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class CustomPM implements PluginMessageListener {

    private final MySQL sql;
    private final CustomConfig message;

    public CustomPM(ServerWarp plugin) {
        sql = plugin.sql();
        message = plugin.message();
    }

    private final String option = "§6[ 워프 ] §f";

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("Bungee:Warp")) return;

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String subChannel = input.readUTF();

        if (subChannel.equals("Server")) {
            String data = input.readUTF();
            createWarp(data);
        }
        if (subChannel.equals("Warp")) {
            String data = input.readUTF();
            teleportPlayer(data);
        }
    }
    public static void getPlayerServer(Player player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Server");
        output.writeUTF(player.getName());
        player.sendPluginMessage(ServerWarp.getInstance(), "Bungee:Warp", output.toByteArray());
    }
    private void createWarp(String data) {
        String[] array = data.split(",");
        Player player = Bukkit.getPlayer(array[0]);
        sql.insertWarp(array[1], array[0], LocationUtil.locationToString(player.getLocation()));
        player.sendMessage(option + "워프를 생성하였습니다.");
    }
    private void teleportPlayer(String data) {
        String[] array = data.split(",");
        Player player = Bukkit.getPlayer(array[0]);
        String server = array[1];
        String name = array[2];

        String location = sql.getLocation(server, name);

        World world = Bukkit.getWorld(LocationUtil.getLocationToString(location, "world"));
        double x = Double.parseDouble(LocationUtil.getLocationToString(location, "x"));
        double y = Double.parseDouble(LocationUtil.getLocationToString(location, "y"));
        double z = Double.parseDouble(LocationUtil.getLocationToString(location, "z"));
        float pitch = Float.parseFloat(LocationUtil.getLocationToString(location, "pitch"));
        float yaw = Float.parseFloat(LocationUtil.getLocationToString(location, "yaw"));

        player.teleport(new Location(world, x, y, z, yaw, pitch));
        sql.deletePlayerServer(player);
        player.sendMessage(message.getConfig().getString("이동.완료"));
    }
}
