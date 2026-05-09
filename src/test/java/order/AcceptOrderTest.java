package order;

import clients.CourierClient;
import clients.OrderClient;
import generator.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import models.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class AcceptOrderTest {

    private CourierClient courierClient;
    private OrderClient orderClient;
    private Courier courier;
    private Integer courierId;
    private Integer orderId;
    private Integer track;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        orderClient = new OrderClient();
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.then()
                .statusCode(SC_OK)
                .extract()
                .path("id");
        Response createOrderResponse = orderClient.createOrder(
                Order.defaultOrder(Collections.emptyList())
        );
        track = createOrderResponse.jsonPath().getInt("track");
        Response getOrderResponse = orderClient.getOrderByTrack(track);
        orderId = getOrderResponse.jsonPath().getInt("order.id");
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешное принятие заказа")
    @Description("Принятие заказа возвращает 200 и ok: true")
    public void shouldAcceptOrderSuccessfully() {
        Response response = orderClient.acceptOrder(orderId, courierId);
        response.then()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Ошибка принятия заказа без courierId")
    @Description("Принятие заказа без courierId возвращает 400")
    public void shouldReturnErrorWhenAcceptOrderWithoutCourierId() {
        Response response = orderClient.acceptOrderWithoutCourierId(orderId);
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    @DisplayName("Ошибка принятия заказа с неверным courierId")
    @Description("Принятие заказа с неверным courierId возвращает 404")
    public void shouldReturnErrorWhenAcceptOrderWithWrongCourierId() {
        Response response = orderClient.acceptOrder(orderId, 999999);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("Курьера с таким id"));
    }

    @Test
    @DisplayName("Ошибка принятия заказа без orderId")
    @Description("Принятие заказа без orderId возвращает 404")
    public void shouldReturnErrorWhenAcceptOrderWithoutOrderId() {
        Response response = orderClient.acceptOrderWithoutOrderId(courierId);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("Not Found"));
    }

    @Test
    @DisplayName("Ошибка принятия заказа с неверным orderId")
    @Description("Принятие заказа с неверным orderId возвращает 404")
    public void shouldReturnErrorWhenAcceptOrderWithWrongOrderId() {
        Response response = orderClient.acceptOrder(999999, courierId);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("Заказа с таким id"));
    }
}