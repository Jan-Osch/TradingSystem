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
                MarketsInteractor.getInstrument(marketUuid, instrumentUuid);
            } catch (MarketNotFoundException | InstrumentUuidNotFoundException e) {
                e.printStackTrace();
            }
            return "501";
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
            }
            return "501";
        }, new JsonTransformer());

        get(API_CONTEXT + "/:marketUuid/instrument/:instrumentUuid", "application/json", (request, response) -> {

            UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));

            try {
                return MarketsInteractor.getInstrument(marketUuid, instrumentUuid);
            } catch (MarketNotFoundException e) {
                e.printStackTrace();
                return "Market not Found";
            } catch (InstrumentUuidNotFoundException e) {
                e.printStackTrace();
                return "Instrument not found!";
            }
        }, new JsonTransformer());

        get(API_CONTEXT + "/:marketUuid/instrument/:instrumentUuid/record/current", "application/json", (request, response) -> {

            UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));

            try {
                return MarketsInteractor.getCurrentRecordForInstrument(instrumentUuid, marketUuid);
            } catch (MarketNotFoundException | InstrumentUuidNotFoundException e) {
                e.printStackTrace();
            }
            return "501";
        }, new JsonTransformer());

        get(API_CONTEXT + "/:marketUuid/instrument/:instrumentUuid/record/period", "application/json", (request, response) -> {

            UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));
            Date start = SqlUtils.dateTimeFromString(request.queryParams("start"));
            Date end = SqlUtils.dateTimeFromString(request.queryParams("end"));
            return MarketsInteractor.getCurrentRecordsForPeriod(instrumentUuid, marketUuid, start, end);
        }, new JsonTransformer());

        get(API_CONTEXT + "/:marketUuid/instrument/:instrumentUuid/record/historical", "application/json", (request, response) -> {

            UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));
            Date start = SqlUtils.dateTimeFromString(request.queryParams("start"));
            Date end = SqlUtils.dateTimeFromString(request.queryParams("end"));

            try {
                return MarketsInteractor.getHistoricalRecordsForInstrument(instrumentUuid, marketUuid, start, end);
            } catch (MarketNotFoundException | InstrumentUuidNotFoundException e) {
                e.printStackTrace();
            }
            return "501";
        }, new JsonTransformer());
    }
}