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
 * 
 * @author Rodrigo Pizzi Argentato
 */
public final class Main {

        /** Cannot be instantiated. */
        private Main() {
        }

        /**
         * Application main entry point.
         * 
         * @param args command line arguments.
         * @throws IOException if there are problems reading logging properties
         */
        public static void main(final String[] args) throws IOException {
                configLogManager();
                startDataBaseMigration();
                startServer();
        }

        /**
         * Configuring LogManager through loggind.properties.
         * 
         * @throws IOException Occurs when logging.properties can not be loaded
         */
        private static void configLogManager() throws IOException {
                LogManager.getLogManager().readConfiguration(
                                Main.class.getResourceAsStream("/logging.properties"));
        }

        /** Run databasemigration with Flyway. */
        private static void startDataBaseMigration() {
                Config config = Config.create();

                String jdbcUrl = config.get("database.url").asString().get();
                String databaseUsername = config.get("database.username").asString().get();
                String databasePassword = config.get("database.password").asString().get();
                String schema = config.get("database.schema").asString().get();

                Flyway flyway = Flyway.configure()
                                .dataSource(jdbcUrl, databaseUsername, databasePassword)
                                .schemas(schema).load();

                flyway.migrate();
        }

        /**
         * Start the server.
         * 
         * @return the created {@link WebServer} instance
         * @throws IOException if there are problems reading logging properties
         */
        static WebServer startServer() throws IOException {
                Config config = Config.create();

                ServerConfiguration serverConfig = ServerConfiguration.create(config.get("server"));

                WebServer server = WebServer.create(serverConfig, createRouting());

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
         * 
         * @return routing configured with JSON support, a health check, and a service
         */
        private static Routing createRouting() {

                MetricsSupport metrics = MetricsSupport.create();
                HealthSupport health =
                                HealthSupport.builder().add(HealthChecks.healthChecks()).build();
                JacksonSupport jacksonSupport = JacksonSupport.create();

                /* Business Services */
                IncomeService incomeService = new IncomeService();

                return Routing.builder().register(JsonSupport.create()).register(health)
                                .register(metrics).register(jacksonSupport)
                                .register("/income", incomeService).build();
        }

}
