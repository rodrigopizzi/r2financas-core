/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.rodas.r2financas.core;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.net.HttpURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.helidon.common.http.Http;
import io.helidon.webserver.WebServer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.rodas.r2financas.core.domain.Income;

public class IncomeTest {

    private static WebServer webServer;
    private ObjectMapper m = new ObjectMapper();
    
    @BeforeAll
    public static void startTheServer() throws Exception {
        webServer = Main.startServer();

        long timeout = 2000; // 2 seconds should be enough to start the server
        long now = System.currentTimeMillis();

        while (!webServer.isRunning()) {
            Thread.sleep(100);
            if ((System.currentTimeMillis() - now) > timeout) {
                Assertions.fail("Failed to start webserver");
            }
        }
    }

    @AfterAll
    public static void stopServer() throws Exception {
        if (webServer != null) {
            webServer.shutdown()
                     .toCompletableFuture()
                     .get(10, TimeUnit.SECONDS);
        }
    }

    @Test
    public void testCreate() throws Exception {
        HttpURLConnection conn;

        conn = getURLConnection("POST","/income");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        Income income = new Income();
        income.idAccount = 1L;
        income.idIncome = 1L;
        income.title = "Dinner";
        income.value = new BigDecimal("12.95");

        OutputStream os = conn.getOutputStream();
        os.write(m.writeValueAsString(income).getBytes());
        os.close();
        Assertions.assertEquals(Http.Status.OK_200.code(), conn.getResponseCode(), "Created new income registry");
    }

    private HttpURLConnection getURLConnection(String method, String path) throws Exception {
        URL url = new URL("http://localhost:" + webServer.port() + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Accept", "application/json");
        System.out.println("Connecting: " + method + " " + url);
        return conn;
    }
}
