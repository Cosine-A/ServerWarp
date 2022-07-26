package com.cosine.serverwarp.command;

import com.cosine.serverwarp.bungee.BungeePM;
import com.cosine.serverwarp.bungee.CustomConfig;
import com.cosine.serverwarp.bungee.MySQL;
import com.cosine.serverwarp.bungee.CustomPM;
import com.cosine.serverwarp.main.ServerWarp;
import com.cosine.serverwarp.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpCommand implements CommandExecutor {

    private final MySQL sql;
    private final CustomConfig message;

    public OpCommand(ServerWarp plugin) {
        sql = plugin.sql();
        message = plugin.message();
    }

    private final String option = "§6[ 워프 ] §f";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (player.isOp()) {
                    help(player);
                    return false;
                }
                return false;
            }
            switch (args[0]) {
                case "저장": {
                    if (player.isOp()) {
                        if (args.length == 1) {
                            player.sendMessage(option + "이름을 입력해주세요.");
                            return false;
                        }
                        if (sql.checkWarpExist(args[1])) {
                            player.sendMessage(option + "이미 존재하는 워프입니다.");
                            return false;
                        }
                        CustomPM.getPlayerServer(player);
                    }
                    break;
                }
                case "삭제": {
                    if (player.isOp()) {
                        if (args.length == 1) {
                            player.sendMessage(option + "이름을 입력해주세요.");
                            return false;
                        }
                        if (!sql.checkWarpExist(args[1])) {
                            player.sendMessage(option + "존재하지 않는 워프입니다.");
                            return false;
                        }
                        sql.deleteWarp(args[1]);
                        player.sendMessage(option + "해당 워프를 삭제하였습니다.");
                    }
                    break;
                }
                case "이동": {
                    if (args.length == 1) {
                        player.sendMessage(option + "이름을 입력해주세요.");
                        return false;
                    }
                    if (!sql.checkWarpExist(args[1])) {
                        player.sendMessage(option + "존재하지 않는 워프입니다.");
                        return false;
                    }
                    String playerServer = sql.getPlayerServer(player);
                    String warpServer = sql.getWarpServer(args[1]);
                    if (playerServer.equals(warpServer)) {
                        teleportPlayer(player, warpServer, args[1]);
                    } else {
                        sql.updatePlayerServer(player, warpServer, args[1]);
                        BungeePM.connect(player, warpServer);
                    }
                    break;
                }
                case "목록": {
                    if (player.isOp()) {
                        player.sendMessage(option + "워프 목록");
                        player.sendMessage("");
                        sql.getWarpList(player);
                    }
                    break;
                }
                default: return false;
            }
        }
        return false;
    }
    private void help(Player player) {
        player.sendMessage(option + "워프 설정 시스템 도움말");
        player.sendMessage("");
        player.sendMessage(option + "/워프 저장 [이름]");
        player.sendMessage(option + "/워프 삭제 [이름]");
        player.sendMessage(option + "/워프 이동 [이름]");
        player.sendMessage(option + "/워프 목록");
    }
    private void teleportPlayer(Player player, String server, String name) {
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
