package com.cosine.serverwarp.bungee;

import com.cosine.serverwarp.main.ServerWarp;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {

    private final HikariCP cp;

    public MySQL(ServerWarp plugin) {
        cp = plugin.cp();
    }

    public boolean checkWarpExist(String name) {
        String select = "select 이름 from 워프목록 where 이름 = '" + name + "';";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(select);
             ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void insertWarp(String server, String name, String location) {
        String insert = "insert into 워프목록 (서버, 이름, 위치) " +
                "select '" + server + "', '" + name + "', '" + location + "' from dual where not exists (select 이름 from 워프목록 where 이름 = '" + name + "')";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(insert);
        ) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteWarp(String server) {
        String insert = "delete from 워프목록 where 이름 = '" + server + "'";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(insert);
        ) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getWarpServer(String name) {
        String select = "select 서버 from 워프목록 where 이름 = '" + name + "';";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(select);
             ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getString("서버");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    public String getWarpList(Player player) {
        String select = "select * from 워프목록;";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(select);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                String warpName = rs.getString("이름");
                player.sendMessage(" §f[ " + warpName + " ]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "X";
    }
    public String getLocation(String server, String name) {
        String select = "select 위치 from 워프목록 where 서버 = '" + server + "' and 이름 = '" + name + "';";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(select);
             ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getString("위치");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "world, 0, 100, 0, 0, 0";
    }
    public String getPlayerServer(Player player) {
        String select = "select 서버 from 플레이어 where UUID = '" + player.getUniqueId() + "';";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(select);
             ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getString("서버");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "lobby";
    }
    public void updatePlayerServer(Player player, String server, String name) {
        String update = "update 플레이어 set 서버 = '" + server + "', 이름 = '" + name + "' where UUID = '" + player.getUniqueId() + "';";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(update);
        ) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deletePlayerServer(Player player) {
        String update = "update 플레이어 set 이름 = '' where UUID = '" + player.getUniqueId() + "';";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(update);
        ) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
