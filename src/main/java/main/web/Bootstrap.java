package main.web;

import main.Initializer;
import main.WarsawOrchestrator;
import spark.Request;
import spark.Response;
import spark.Route;
import web.AccountsResource;
import web.MarketsResource;
import web.TransactionsResource;

import static spark.Spark.*;

public class Bootstrap {

    public static void main(String[] args) {
        Initializer.start();
        new WarsawOrchestrator();
        new MarketsResource();
        new AccountsResource();
        new TransactionsResource();
        get("/", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                return "Hello World!!";
            }
        });
    }
}