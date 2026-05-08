package order;

import clients.OrderClient;
import io.restassured.response.Response;
import models.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

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
    public void shouldReturnOrderByTrackSuccessfully() {
        Response response = orderClient.getOrderByTrack(track);
        response.then()
                .statusCode(200)
                .body("order", notNullValue());
    }

    @Test
    public void shouldReturnErrorWhenTrackIsMissing() {
        Response response = orderClient.getOrderByTrackWithoutTrack();
        response.then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    public void shouldReturnErrorWhenTrackIsWrong() {
        Response response = orderClient.getOrderByTrack(999999);
        response.then()
                .statusCode(404)
                .body("message", containsString("Заказ не найден"));
    }
}