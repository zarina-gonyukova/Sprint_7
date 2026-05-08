package clients;

import constants.ApiConstants;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    static {
        RestAssured.baseURI = ApiConstants.BASE_URL;
    }

    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return given()
                .filter(new AllureRestAssured())
                .header(ApiConstants.CONTENT_TYPE, ApiConstants.APPLICATION_JSON)
                .body(order)
                .post(ApiConstants.ORDERS_PATH);
    }

    @Step("Получить список заказов")
    public Response getOrdersList() {
        return given()
                .filter(new AllureRestAssured())
                .get(ApiConstants.ORDERS_PATH);
    }

    @Step("Принять заказ")
    public Response acceptOrder(int orderId, int courierId) {
        return given()
                .filter(new AllureRestAssured())
                .queryParam("courierId", courierId)
                .put(ApiConstants.ORDERS_PATH + "/accept/" + orderId);
    }

    @Step("Принять заказ без courierId")
    public Response acceptOrderWithoutCourierId(int orderId) {
        return given()
                .filter(new AllureRestAssured())
                .put(ApiConstants.ORDERS_PATH + "/accept/" + orderId);
    }

    @Step("Принять заказ без номера заказа")
    public Response acceptOrderWithoutOrderId(int courierId) {
        return given()
                .filter(new AllureRestAssured())
                .queryParam("courierId", courierId)
                .put(ApiConstants.ORDERS_PATH + "/accept/");
    }

    @Step("Получить заказ по track")
    public Response getOrderByTrack(int track) {
        return given()
                .filter(new AllureRestAssured())
                .queryParam("t", track)
                .get(ApiConstants.ORDERS_PATH + "/track");
    }

    @Step("Получить заказ без track")
    public Response getOrderByTrackWithoutTrack() {
        return given()
                .filter(new AllureRestAssured())
                .get(ApiConstants.ORDERS_PATH + "/track");
    }
}