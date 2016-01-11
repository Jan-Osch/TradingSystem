package com.bubble.web;

import com.bubble.database.SqlUtils;
import com.bubble.markets.exceptions.InstrumentUuidNotFoundException;
import com.bubble.markets.exceptions.MarketNotFoundException;
import com.bubble.markets.interactors.MarketsInteractor;

import java.util.Date;
import java.util.UUID;

import static spark.Spark.get;

public class MarketsResource {

    private static final String API_CONTEXT = "/api/markets";

    public MarketsResource() {
        setupEndpoints();
    }

    private void setupEndpoints() {
        get(API_CONTEXT + "/:marketUuid/instrument/:instrumentUuid", "application/json", (request, response) -> {

            UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));
            try {
                return MarketsInteractor.getInstrument(marketUuid, instrumentUuid);
            } catch (MarketNotFoundException | InstrumentUuidNotFoundException e) {
                e.printStackTrace();
                response.status(404);
            }
            return "";
        }, new JsonTransformer());

        get(API_CONTEXT + "/instrument/:instrumentUuid", "application/json", (request, response) -> {

            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));
            try {
                UUID marketUuid = MarketsInteractor.getMarketUuidForInstrument(instrumentUuid);
                return MarketsInteractor.getInstrument(marketUuid, instrumentUuid);
            } catch (MarketNotFoundException | InstrumentUuidNotFoundException e) {
                e.printStackTrace();
                response.status(404);
            }
            return "";
        }, new JsonTransformer());

        get(API_CONTEXT, "application/json", (request, response) -> {
            return MarketsInteractor.getAllMarkets();
        }, new JsonTransformer());

        get(API_CONTEXT + "/:marketUuid/instrument", "application/json", (request, response) -> {
            UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
            try {
                return MarketsInteractor.getAllInstrumentsForMarket(marketUuid);
            } catch (MarketNotFoundException e) {
                e.printStackTrace();
                response.status(404);
                return e;
            }
        }, new JsonTransformer());


        get(API_CONTEXT + "/instrument/:instrumentUuid/record/current", "application/json", (request, response) -> {

            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));

            try {
                UUID marketUuid = MarketsInteractor.getMarketUuidForInstrument(instrumentUuid);
                return MarketsInteractor.getCurrentRecordForInstrument(instrumentUuid, marketUuid);
            } catch (MarketNotFoundException | InstrumentUuidNotFoundException e) {
                e.printStackTrace();
                response.status(404);
                return e;
            }
        }, new JsonTransformer());

        get(API_CONTEXT + "/instrument/:instrumentUuid/record/period", "application/json", (request, response) -> {

            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));
            Date start = SqlUtils.dateTimeFromString(request.queryParams("start"));
            Date end = SqlUtils.dateTimeFromString(request.queryParams("end"));
            try {
                UUID marketUuid = MarketsInteractor.getMarketUuidForInstrument(instrumentUuid);
                return MarketsInteractor.getCurrentRecordsForPeriod(instrumentUuid, marketUuid, start, end);
            } catch (InstrumentUuidNotFoundException e) {
                e.printStackTrace();
                response.status(404);
                return e;
            }
        }, new JsonTransformer());

        get(API_CONTEXT + "/instrument/:instrumentUuid/record/historical", "application/json", (request, response) -> {

            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));
            Date start = SqlUtils.dateTimeFromString(request.queryParams("start"));
            Date end = SqlUtils.dateTimeFromString(request.queryParams("end"));

            try {
                UUID marketUuid = MarketsInteractor.getMarketUuidForInstrument(instrumentUuid);
                return MarketsInteractor.getHistoricalRecordsForInstrument(instrumentUuid, marketUuid, start, end);
            } catch (MarketNotFoundException | InstrumentUuidNotFoundException e) {
                e.printStackTrace();
                response.status(404);
                return e;
            }
        }, new JsonTransformer());
    }
}