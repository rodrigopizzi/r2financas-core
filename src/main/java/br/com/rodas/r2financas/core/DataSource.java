package br.com.rodas.r2financas.core;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.helidon.config.Config;

/**
 * This class is responsible for managment of connection pool.
 */
public final class DataSource {

    /** That is connection pool configuration. */
    private static HikariConfig config = new HikariConfig();
    /** That is datasource will be used by application. */
    private static HikariDataSource ds;

    /** Cannot be instantiate. */
    private DataSource() {
    }

    /**
     * Request connection with database for connection pool.
     * @return {@Connection} Connection for database
     * @throws SQLException Occurs when cannot establish connection
     */
    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            Config appConfig = Config.create();

            String jdbcUrl = appConfig.get("database.url").asString().get();
            String databaseUsername =
                    appConfig.get("database.username").asString().get();
            String databasePassword =
                    appConfig.get("database.password").asString().get();

            config.setJdbcUrl(jdbcUrl);
            config.setUsername(databaseUsername);
            config.setPassword(databasePassword);
            config.setSchema("rodrigopizzi@gmail.com");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            ds = new HikariDataSource(config);
        }

        return ds.getConnection();
    }
}
