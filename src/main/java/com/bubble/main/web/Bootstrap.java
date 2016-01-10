package main.web;

import main.Initializer;
import main.WarsawOrchestrator;
import spark.Request;
import spark.Response;
import spark.Route;
import web.*;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class Bootstrap {

    public static void main(String[] args) {
        staticFileLocation("/public");
        Initializer.start();
        new MarketsResource();
        new AccountsResource();
        new TransactionsResource();
        new GamesResource();
        new ApplicationResource();
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