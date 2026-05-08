package order;

import clients.CourierClient;
import clients.OrderClient;
import generator.CourierGenerator;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import models.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

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
                .statusCode(200)
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
    public void shouldAcceptOrderSuccessfully() {
        Response response = orderClient.acceptOrder(orderId, courierId);
        response.then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    public void shouldReturnErrorWhenAcceptOrderWithoutCourierId() {
        Response response = orderClient.acceptOrderWithoutCourierId(orderId);
        response.then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    public void shouldReturnErrorWhenAcceptOrderWithWrongCourierId() {
        Response response = orderClient.acceptOrder(orderId, 999999);
        response.then()
                .statusCode(404)
                .body("message", containsString("Курьера с таким id"));
    }

    @Test
    public void shouldReturnErrorWhenAcceptOrderWithoutOrderId() {
        Response response = orderClient.acceptOrderWithoutOrderId(courierId);
        response.then()
                .statusCode(404)
                .body("message", containsString("Not Found"));
    }

    @Test
    public void shouldReturnErrorWhenAcceptOrderWithWrongOrderId() {
        Response response = orderClient.acceptOrder(999999, courierId);
        response.then()
                .statusCode(404)
                .body("message", containsString("Заказа с таким id"));
    }
}