package org.chelonix.daggersample;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;

public class EchoServer extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new EchoServer());
    }

    private Info info = new Info();

    @Override
    public void start() {
        vertx.createHttpServer()
            .requestHandler(req -> handleRequest(req))
            .listen(8080, http -> {
            if (http.succeeded()) {
                System.out.println("HTTP server started on port 8080");
            } else {
                System.out.println("HTTP server failed to start");
            }
        });
    }

    private void handleRequest(HttpServerRequest req) {
        if ("/ping".equals(req.path())) {
            req.response()
                .putHeader("content-type", "text/plain")
                .end("pong");
        } else if ("/info".equals(req.path())) {
            req.response()
                .putHeader("content-type", "application/json")
                .end(info.toJson().encodePrettily());
        } else {
            req.response()
                .putHeader("content-type", "text/plain")
                .end("Echo: " + req.uri());
        }
    }
}
