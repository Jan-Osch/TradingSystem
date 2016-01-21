package com.bubble.main;

import com.bubble.web.*;
import org.slf4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;

public class BubbleApplication {

    public static void main(String[] args) {

        Logger LOG = org.slf4j.LoggerFactory.getLogger(BubbleApplication.class);
        staticFileLocation("/public");
        Initializer.start();
        new MarketsResource();
        new AccountsResource();
        new TransactionsResource();
        new GamesResource();
        new ApplicationResource();

        after((req, res) -> {
            LOG.info("Request: {}", req.url());
        });
        get("/", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                response.redirect("/index.html");
                return "";
            }
        });
        new WarsawOrchestrator();
    }
}