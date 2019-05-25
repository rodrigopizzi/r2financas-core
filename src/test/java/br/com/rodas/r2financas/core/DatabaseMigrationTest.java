package br.com.rodas.r2financas.core;

import org.flywaydb.core.Flyway;
import io.helidon.config.Config;

/** Responsible for execute database migration for test scope. */
public final class DatabaseMigrationTest {

    /** Cannot be instantiated. */
    private DatabaseMigrationTest() {
    }

    /** Performs the database migration. */
    public static void migrate() {
        Config config = Config.create();

        String jdbcUrl = config.get("database.url").asString().get();
        String databaseUsername = config.get("database.username").asString().get();
        String databasePassword = config.get("database.password").asString().get();
        String schema = config.get("database.schema").asString().get();

        Flyway flyway = Flyway.configure().dataSource(jdbcUrl, databaseUsername, databasePassword)
                .locations("filesystem:.\\src\\main\\resources\\db").schemas(schema).load();

        flyway.migrate();
    }
}
