package com.cosine.serverwarp.main;

import com.cosine.serverwarp.bungee.*;
import com.cosine.serverwarp.command.OpCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class ServerWarp extends JavaPlugin {

    private CustomConfig config;
    private CustomConfig message;
    private HikariCP cp;
    private MySQL sql;

    @Override
    public void onEnable() {
        getLogger().info("서버 이동 플러그인 활성화");

        config = new CustomConfig(this, "config.yml");
        message = new CustomConfig(this, "message.yml");
        config.saveDefaultConfig();
        message.saveDefaultConfig();

        createDataBase();
        createTable();

        cp = new HikariCP(this);
        sql = new MySQL(this);

        getCommand("워프").setExecutor(new OpCommand(this));

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeePM());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "Bungee:Warp", new CustomPM(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "Bungee:Warp");
    }

    @Override
    public void onDisable() {
        getLogger().info("서버 이동 플러그인 비활성화");
        cp.closeConnection();
    }
    private static ServerWarp instance;
    public void onLoad() {
        instance = this;
    }
    public static ServerWarp getInstance() {
        return instance;
    }
    private void createDataBase() {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
             PreparedStatement ps = connection.prepareStatement(getUrl())
        ) {
            String country = "create database if not exists 번지워프";
            ps.executeUpdate(country);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void createTable() {
        try (Connection country_connection = DriverManager.getConnection(getUrl() + "/번지워프", getUser(), getPassword());
             PreparedStatement ps = country_connection.prepareStatement(getUrl());
        ) {
            String create = "create table if not exists 워프목록 (서버 varchar(20), 이름 varchar(20) not null, 위치 varchar(500) not null)";
            String create2 = "create table if not exists 플레이어 (UUID varchar(50), 서버 varchar(20) not null, 이름 varchar(20) not null)";
            ps.execute(create);
            ps.execute(create2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public CustomConfig message() {
        return this.message;
    }
    public String getUrl() {return "jdbc:mysql://" + config.getConfig().getString("MySQL.host") + ":" + config.getConfig().getString("MySQL.port");}
    public String getUser() {
        return config.getConfig().getString("MySQL.user");
    }
    public String getPassword() {
        return config.getConfig().getString("MySQL.password");
    }
    public HikariCP cp() {
        return this.cp;
    }
    public MySQL sql() {
        return this.sql;
    }
}
