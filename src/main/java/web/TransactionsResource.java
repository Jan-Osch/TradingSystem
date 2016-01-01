package web;

import accounts.exceptions.AccountUuidNotFound;
import accounts.exceptions.AssetNotSufficient;
import accounts.exceptions.ResourcesNotSufficient;
import commons.JsonHelper;
import markets.exceptions.InstrumentUuidNotFoundException;
import markets.exceptions.MarketNotFoundException;
import markets.interactors.GetAllMarketsTransaction;
import markets.interactors.GetInstrumentTransaction;
import transactions.interactors.TransactionsInteractor;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.post;

public class TransactionsResource {
    private static final String API_CONTEXT = "/api/transactions";

    public TransactionsResource() {
        setupEndpoints();
    }

    private void setupEndpoints() {
        post(API_CONTEXT + "/buyByMarketPrice", "application/json", (request, response) -> {

            Map<String, String> postParams = JsonHelper.jsonToMapStringString(request.body());
            try {
                UUID accountUuid = UUID.fromString(postParams.get("accountUuid"));
                UUID instrumentUuid = UUID.fromString(postParams.get("instrumentUuid"));
                BigDecimal amount = new BigDecimal(postParams.get("amount"));
                TransactionsInteractor.createStockBuyOrderByMarketPrice(instrumentUuid, accountUuid, amount);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return "Invalid Params";
            } catch (AccountUuidNotFound | ResourcesNotSufficient | MarketNotFoundException | InstrumentUuidNotFoundException e) {
                e.printStackTrace();
            }
            return "501";

        }, new JsonTransformer());
        post(API_CONTEXT + "/sellByMarketPrice", "application/json", (request, response) -> {

            Map<String, String> postParams = JsonHelper.jsonToMapStringString(request.body());
            try {
                UUID accountUuid = UUID.fromString(postParams.get("accountUuid"));
                UUID instrumentUuid = UUID.fromString(postParams.get("instrumentUuid"));
                BigDecimal amount = new BigDecimal(postParams.get("amount"));
                TransactionsInteractor.createStockSellOrderByMarketPrice(instrumentUuid, accountUuid, amount);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return "Invalid Params";
            } catch (AccountUuidNotFound | MarketNotFoundException | InstrumentUuidNotFoundException | AssetNotSufficient e) {
                e.printStackTrace();
            }
            return "501";

        }, new JsonTransformer());

        get(API_CONTEXT + "/total/:accountUuid", "application/json", (request, response) -> {
            UUID accountUUid = UUID.fromString(request.params("accountUUid"));
            try {
                return TransactionsInteractor.getTotalValueOfAccount(accountUUid);
            } catch (AccountUuidNotFound | MarketNotFoundException | InstrumentUuidNotFoundException accountUuidNotFound) {
                accountUuidNotFound.printStackTrace();
            }

            return "404";
        }, new JsonTransformer());

    }
}
