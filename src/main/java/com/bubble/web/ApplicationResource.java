package com.bubble.web;

import com.bubble.application.exceptions.InvalidPassword;
import com.bubble.application.exceptions.LoginAlreadyExists;
import com.bubble.application.exceptions.UserDoesNotExist;
import com.bubble.application.interactors.ApplicationInteractor;
import com.bubble.commons.JsonHelper;

import java.util.Map;
import java.util.UUID;

import static spark.Spark.post;

public class ApplicationResource {
    private static final String API_CONTEXT = "api";
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ApplicationResource.class);

    public ApplicationResource() {
        setupEndpoints();
    }

    private void setupEndpoints() {
        post(API_CONTEXT + "/login", "application/json", (request, response) -> {

            Map<String, String> postParams = JsonHelper.jsonToMapStringString(request.body());
            try {
                String login = postParams.get("login");
                String password = postParams.get("password");
                UUID uuid = ApplicationInteractor.loginUser(login, password);
                request.session().attribute("user_uuid", uuid.toString());
                response.cookie("server_user_uuid", uuid.toString());
                return uuid;
            } catch (NullPointerException e) {
                response.status(400);
                return "Invalid params";
            } catch (UserDoesNotExist userDoesNotExist) {
                userDoesNotExist.printStackTrace();
                response.status(404);
                return "User does not exist";
            } catch (InvalidPassword invalidPassword) {
                invalidPassword.printStackTrace();
                response.status(400);
                return "Invalid password";
            }
        });

        post(API_CONTEXT + "/register", "application/json", (request, response) -> {
            Map<String, String> postParams = JsonHelper.jsonToMapStringString(request.body());
            try {
                String login = postParams.get("login");
                String password = postParams.get("password");
                UUID uuid = ApplicationInteractor.registerUser(login, password);
                request.session().attribute("user_uuid", uuid.toString());
                response.cookie("server_user_uuid", uuid.toString());
                return uuid;
            } catch (NullPointerException e) {
                response.status(400);
                return "Invalid params";
            } catch (LoginAlreadyExists loginAlreadyExists) {
                loginAlreadyExists.printStackTrace();
                response.status(409);
                return "Login already exists";
            }
        });

        post(API_CONTEXT + "/logout", "application/json", (request, response) -> {
            request.session().removeAttribute("user_uuid");
            response.removeCookie("user_uuid");
            return "OK";
        });
    }
}
