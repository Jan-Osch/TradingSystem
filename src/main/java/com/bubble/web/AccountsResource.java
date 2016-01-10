package com.bubble.web;

import com.bubble.accounts.exceptions.AccountUuidNotFound;
import com.bubble.accounts.interactors.AccountsInteractor;

import java.util.UUID;

import static spark.Spark.get;

public class AccountsResource {
    private static final String API_CONTEXT = "/api/account";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AccountsResource.class);

    public AccountsResource() {
        setupEndpoints();
    }

    private void setupEndpoints() {
        get(API_CONTEXT + "/:ownerUuid", "application/json", (request, response) -> {
            UUID ownerUuid = UUID.fromString(request.params(":ownerUuid"));
            LOG.debug(ownerUuid.toString());
            try {
                return AccountsInteractor.getAccount(ownerUuid);
            } catch (AccountUuidNotFound accountUuidNotFound) {
                accountUuidNotFound.printStackTrace();
            }
            return "404";
        }, new JsonTransformer());
    }
}
