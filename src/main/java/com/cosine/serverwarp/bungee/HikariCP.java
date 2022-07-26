package com.cosine.serverwarp.bungee;

import com.cosine.serverwarp.main.ServerWarp;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariCP {

    private final HikariDataSource ds;

    public HikariCP(ServerWarp plugin) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(plugin.getUrl() + "/번지워프");
        config.setUsername(plugin.getUser());
        config.setPassword(plugin.getPassword());
        config.setMaximumPoolSize(50);
        config.setMinimumIdle(20);
        ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    public void closeConnection() {
        ds.close();
    }
}
