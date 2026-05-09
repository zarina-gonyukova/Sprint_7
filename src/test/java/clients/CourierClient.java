package clients;

import constants.ApiConstants;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class CourierClient {

    static {
        RestAssured.baseURI = ApiConstants.BASE_URL;
    }

    @Step("Создать курьера")
    public Response createCourier(Courier courier) {
        return given()
                .filter(new AllureRestAssured())
                .header(ApiConstants.CONTENT_TYPE, ApiConstants.APPLICATION_JSON)
                .body(courier)
                .post(ApiConstants.COURIER_PATH);
    }

    @Step("Авторизоваться курьером")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .filter(new AllureRestAssured())
                .header(ApiConstants.CONTENT_TYPE, ApiConstants.APPLICATION_JSON)
                .body(credentials)
                .post(ApiConstants.COURIER_LOGIN_PATH);
    }

    @Step("Авторизоваться курьером без логина")
    public Response loginCourierWithoutLogin(String password) {
        return given()
                .filter(new AllureRestAssured())
                .header(ApiConstants.CONTENT_TYPE, ApiConstants.APPLICATION_JSON)
                .body(Map.of("password", password))
                .post(ApiConstants.COURIER_LOGIN_PATH);
    }

    @Step("Авторизоваться курьером без пароля")
    public Response loginCourierWithoutPassword(String login) {
        return given()
                .filter(new AllureRestAssured())
                .header(ApiConstants.CONTENT_TYPE, ApiConstants.APPLICATION_JSON)
                .body(Map.of("login", login))
                .post(ApiConstants.COURIER_LOGIN_PATH);
    }

    @Step("Удалить курьера по id")
    public Response deleteCourier(int courierId) {
        return given()
                .filter(new AllureRestAssured())
                .delete(ApiConstants.COURIER_PATH + "/" + courierId);
    }

    @Step("Удалить курьера без id")
    public Response deleteCourierWithoutId() {
        return given()
                .filter(new AllureRestAssured())
                .delete(ApiConstants.COURIER_PATH);
    }
}