package main.web;

import database.SqlUtils;
import markets.exceptions.InstrumentUuidNotFoundException;
import markets.exceptions.MarketNotFoundException;
import markets.interactors.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Date;
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
        get(API_CONTEXT + "/:marketUuid/instrument/:instrumentUuid", "application/json", (request, response) -> {

            UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));
            GetInstrumentTransaction getInstrumentTransaction = new GetInstrumentTransaction(instrumentUuid, marketUuid);
            try {
                return getInstrumentTransaction.execute();
            } catch (MarketNotFoundException | InstrumentUuidNotFoundException e) {
                e.printStackTrace();
            }
            return "501";
        }, new JsonTransformer());

        get(API_CONTEXT, "application/json", (request, response) -> {
            GetAllMarketsTransaction getAllMarketsTransaction = new GetAllMarketsTransaction();
            try {
                return getAllMarketsTransaction.execute();
            } catch (MarketNotFoundException e) {
                e.printStackTrace();
            }
            return "501";
        }, new JsonTransformer());

        get(API_CONTEXT + "/:marketUuid/instrument", "application/json", (request, response) -> {
            UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
            GetAllInstrumentsForMarket getAllInstrumentsForMarket = new GetAllInstrumentsForMarket(marketUuid);
            try {
                return getAllInstrumentsForMarket.execute();
            } catch (MarketNotFoundException e) {
                e.printStackTrace();
            }
            return "501";
        }, new JsonTransformer());

        get(API_CONTEXT + "/:marketUuid/instrument/:instrumentUuid/record/current", "application/json", (request, response) -> {

            UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));

            GetCurrentRecordForInstrumentTransaction getCurrentRecordForInstrumentTransaction = new GetCurrentRecordForInstrumentTransaction(instrumentUuid, marketUuid);
            try {
                return getCurrentRecordForInstrumentTransaction.execute();
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
            GetCurrentRecordsForPeriodTransaction getCurrentRecordsForPeriodTransaction
                    = new GetCurrentRecordsForPeriodTransaction(instrumentUuid, marketUuid, start, end);
            return getCurrentRecordsForPeriodTransaction.execute();
        }, new JsonTransformer());

        get(API_CONTEXT + "/:marketUuid/instrument/:instrumentUuid/record/historical", "application/json", (request, response) -> {

            UUID marketUuid = UUID.fromString(request.params(":marketUuid"));
            UUID instrumentUuid = UUID.fromString(request.params(":instrumentUuid"));
            Date start = SqlUtils.dateTimeFromString(request.queryParams("start"));
            Date end = SqlUtils.dateTimeFromString(request.queryParams("end"));

            GetHistoricalRecordsForInstrument getHistoricalRecordsForInstrument = new GetHistoricalRecordsForInstrument(instrumentUuid, marketUuid, start, end);
            try {
                return getHistoricalRecordsForInstrument.execute();
            } catch (MarketNotFoundException | InstrumentUuidNotFoundException e) {
                e.printStackTrace();
            }
            return "501";
        }, new JsonTransformer());

    }
}