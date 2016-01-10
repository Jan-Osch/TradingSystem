package web;

import accounts.exceptions.AccountUuidNotFound;
import accounts.interactors.AccountsInteractor;

import java.util.UUID;


public class AccountsResource {
    private static final String API_CONTEXT = "/api/account";

    public AccountsResource() {
        setupEndpoints();
    }

    private void setupEndpoints() {
        get(API_CONTEXT + "/:ownerUuid", "application/json", (request, response) -> {

            UUID ownerUuid = UUID.fromString(request.params(":ownerUuid"));
            try {
                return AccountsInteractor.getAccount(ownerUuid);
            } catch (AccountUuidNotFound accountUuidNotFound) {
                accountUuidNotFound.printStackTrace();
            }
            return "404";
        }, new JsonTransformer());
    }
}
