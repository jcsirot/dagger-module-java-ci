package org.chelonix.daggersample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Unit test for simple App.
 */
@ExtendWith(VertxExtension.class)
public class EchoServerTest {

    private Vertx vertx;

    @BeforeEach
    void setUp(VertxTestContext testContext) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new EchoServer(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void testEchoServer(VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);

        client.get(8080, "localhost", "/test")
            .send(ar -> {
                if (ar.succeeded()) {
                    testContext.verify(() -> {
                        assertEquals("Echo: /test", ar.result().bodyAsString());
                        testContext.completeNow();
                    });
                } else {
                    testContext.failNow(ar.cause());
                }
            });
    }
}
