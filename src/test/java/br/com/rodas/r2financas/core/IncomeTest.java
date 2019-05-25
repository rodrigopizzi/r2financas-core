package br.com.rodas.r2financas.core;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import br.com.rodas.r2financas.core.domain.Income;
import io.helidon.common.http.Http;
import io.helidon.webserver.WebServer;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Testing all operations for income.
 * 
 * @author Rodrigo Pizzi Argentato
 */
public class IncomeTest {

    /** Webserver for testing. */
    private static WebServer webServer;
    /** Json converter. */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Started webserver and keeped alive until testing done.
     * 
     * @throws Exception Occurs when webserver cannot start
     */
    @BeforeAll
    public static void startTheServer() throws Exception {
        DatabaseMigrationTest.migrate();

        webServer = Main.startServer();

        final long timeout = 2000;
        long now = System.currentTimeMillis();

        while (!webServer.isRunning()) {
            final long sleep = 100;
            Thread.sleep(sleep);
            if ((System.currentTimeMillis() - now) > timeout) {
                Assertions.fail("Failed to start webserver");
            }
        }
    }

    /**
     * Shut downed webserver.
     * 
     * @throws Exception Occurs when webserver cannot stop
     */
    @AfterAll
    public static void stopServer() throws Exception {
        if (webServer != null) {
            final int seconds = 10;
            webServer.shutdown().toCompletableFuture().get(seconds, TimeUnit.SECONDS);
        }
    }

    /**
     * Ensures that income operation is created.
     * 
     * @throws Exception Occurs when income operation get abnormal execution
     */
    @Test
    public void testCreate() throws Exception {
        HttpURLConnection conn = getURLConnection(HttpMethod.POST, "/income");

        Income income = new Income(0L, "Salary", new BigDecimal("3517.99"));

        OutputStream os = conn.getOutputStream();
        os.write(mapper.writeValueAsString(income).getBytes());
        os.close();

        String result = new String(conn.getInputStream().readAllBytes());
        Income newIncome = mapper.readValue(result, Income.class);
        income.setIdIncome(newIncome.getIdIncome());
        Assertions.assertEquals(Http.Status.OK_200.code(), conn.getResponseCode(),
                "Created new income record");
        Assertions.assertTrue(newIncome.getIdIncome() > 0, "New income must have informed id");

        conn = getURLConnection(HttpMethod.GET, "/income/" + newIncome.getIdIncome());
        result = new String(conn.getInputStream().readAllBytes());
        newIncome = mapper.readValue(result, Income.class);
        Assertions.assertEquals(income, newIncome,
                "Load this instance of income and compares with the previous");
    }

    /**
     * Connects to a specified URL.
     * 
     * @param method HTTP method like POST, GET, PUT, etc
     * @param path   Path of operation
     * @return {@link HttpURLConnection} for HTTP connection
     * @throws Exception Occurs when something is wrong with the URL and method
     */
    private HttpURLConnection getURLConnection(final HttpMethod method, final String path)
            throws Exception {
        URL url = new URL("http://localhost:" + webServer.port() + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method.name());
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        System.out.println("Connecting: " + method + " " + url);
        return conn;
    }
}
