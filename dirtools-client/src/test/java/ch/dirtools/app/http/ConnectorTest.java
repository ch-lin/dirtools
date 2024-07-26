package ch.dirtools.app.http;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectorTest {

    @Test
    public void testGet() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);

        String hostname = server.getAddress().getHostString();
        int port = server.getAddress().getPort();

        server.createContext("/create", new HttpHandler() {
            @Override
            public void handle(HttpExchange msg) {
                try {
                    try {
                        msg.sendResponseHeaders(200, -1);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } finally {
                    msg.close();
                }
            }
        });
        server.setExecutor(null);

        try {
            server.start();
            Request client = new Request(hostname, port);
            Response response = client.get("create", null);
            assertEquals(200, response.getStatus());
        } finally {
            server.stop(5);
        }

    }


    public void tearDown() throws Exception {
        //server.stop();
    }

}