package main.web;

import markets.exceptions.InstrumentUuidNotFoundException;
import markets.exceptions.MarketNotFoundException;
import markets.interactors.GetInstrumentTransaction;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class MarketsResource {

    private static final String API_CONTEXT = "/api/market";

    public MarketsResource() {
        setupEndpoints();
    }

    private void setupEndpoints() {
        get(API_CONTEXT + "/:marketUuid/instrument/:instrumentUuid", "application/json", new Route() {
                    @Override
                    public Object handle(Request request, Response response) {

                        UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
                        UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));
                        GetInstrumentTransaction getInstrumentTransaction = new GetInstrumentTransaction(instrumentUuid,marketUuid);
                        try {
                            return getInstrumentTransaction.execute();
                        } catch (MarketNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstrumentUuidNotFoundException e) {
                            e.printStackTrace();
                        }
                        return "501";
                    }
                }, new JsonTransformer());

//                (request, response) -> {
//            todoService.createNewTodo(request.body());
//            response.status(201);
//            return response;
//        }, new JsonTransformer());

//        get(API_CONTEXT + "/todos/:id", "application/json", (request, response)
//
//                -> todoService.find(request.params(":id")), new JsonTransformer());
//
//        get(API_CONTEXT + "/todos", "application/json", (request, response)
//
//                -> todoService.findAll(), new JsonTransformer());
//
//        put(API_CONTEXT + "/todos/:id", "application/json", (request, response)
//
//                -> todoService.update(request.params(":id"), request.body()), new JsonTransformer());
//    }


    }
}