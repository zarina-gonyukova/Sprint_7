package order;

import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderByTrackTest {

    private OrderClient orderClient;
    private Integer track;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        Response createOrderResponse = orderClient.createOrder(
                Order.defaultOrder(Collections.emptyList())
        );
        track = createOrderResponse.jsonPath().getInt("track");
    }

    @Test
    @DisplayName("Получение заказа по номеру")
    @Description("Запрос заказа по track возвращает 200 и объект order")
    public void shouldReturnOrderByTrackSuccessfully() {
        Response response = orderClient.getOrderByTrack(track);
        response.then()
                .statusCode(SC_OK)
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Ошибка получения заказа без номера")
    @Description("Запрос заказа без track возвращает 400")
    public void shouldReturnErrorWhenTrackIsMissing() {
        Response response = orderClient.getOrderByTrackWithoutTrack();
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    @DisplayName("Ошибка получения заказа с неверным номером")
    @Description("Запрос заказа с несуществующим track возвращает 404")
    public void shouldReturnErrorWhenTrackIsWrong() {
        Response response = orderClient.getOrderByTrack(999999);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("Заказ не найден"));
    }
}