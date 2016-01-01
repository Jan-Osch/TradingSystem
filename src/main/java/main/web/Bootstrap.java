package main.web;

import game.entities.Game;
import main.Initializer;
import main.WarsawOrchestrator;
import spark.Request;
import spark.Response;
import spark.Route;
import web.*;

import static spark.Spark.*;

public class Bootstrap {

    public static void main(String[] args) {
        Initializer.start();
        new WarsawOrchestrator();
        new MarketsResource();
        new AccountsResource();
        new TransactionsResource();
        new GamesResource();
        new ApplicationResource();

        get("/", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                return "Hello World!!";
            }
        });
    }
}