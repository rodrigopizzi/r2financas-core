package br.com.rodas.r2financas.core;

import java.io.IOException;
import java.util.logging.LogManager;
import org.flywaydb.core.Flyway;
import br.com.rodas.r2financas.core.service.IncomeService;
import io.helidon.config.Config;
import io.helidon.health.HealthSupport;
import io.helidon.health.checks.HealthChecks;
import io.helidon.media.jackson.server.JacksonSupport;
import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.metrics.MetricsSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;

/**
 * Entrypoint for core application.
 * @author Rodrigo Pizzi Argentato
 */
public final class Main {

    /** Cannot be instantiated. */
    private Main() {
    }

    /**
     * Application main entry point.
     * @param args command line arguments.
     * @throws IOException if there are problems reading logging properties
     */
    public static void main(final String[] args) throws IOException {
        startServer();
    }

    /**
     * Run databasemigration with Flyway.
     * @param config application.yaml inside resources package
     */
    private static void startDataBaseMigration(final Config config) {
        String jdbcUrl = config.get("database.url").asString().get();
        String databaseUsername =
                config.get("database.username").asString().get();
        String databasePassword =
                config.get("database.password").asString().get();

        Flyway flyway = Flyway.configure()
                .dataSource(jdbcUrl, databaseUsername, databasePassword)
                .schemas("rodrigopizzi@gmail.com").load();

        flyway.migrate();
    }

    /**
     * Start the server.
     * @return the created {@link WebServer} instance
     * @throws IOException if there are problems reading logging properties
     */
    static WebServer startServer() throws IOException {
        LogManager.getLogManager().readConfiguration(
                Main.class.getResourceAsStream("/logging.properties"));

        Config config = Config.create();

        startDataBaseMigration(config);

        ServerConfiguration serverConfig =
                ServerConfiguration.create(config.get("server"));

        WebServer server =
                WebServer.create(serverConfig, createRouting(config));

        server.start().thenAccept(ws -> {
            System.out.println("WEB server is up! http://localhost:" + ws.port()
                    + "/greet");
            ws.whenShutdown().thenRun(
                    () -> System.out.println("WEB server is DOWN. Good bye!"));
        }).exceptionally(t -> {
            System.err.println("Startup failed: " + t.getMessage());
            t.printStackTrace(System.err);
            return null;
        });

        return server;
    }

    /**
     * Creates new {@link Routing}.
     * @return routing configured with JSON support, a health check, and a
     *         service
     * @param config configuration of this server
     */
    private static Routing createRouting(final Config config) {

        MetricsSupport metrics = MetricsSupport.create();
        HealthSupport health = HealthSupport.builder()
                .add(HealthChecks.healthChecks()).build();
        JacksonSupport jacksonSupport = JacksonSupport.create();

        /* Business Services */
        IncomeService incomeService = new IncomeService();

        return Routing.builder().register(JsonSupport.create()).register(health)
                .register(metrics).register(jacksonSupport)
                .register("/income", incomeService).build();
    }

}
