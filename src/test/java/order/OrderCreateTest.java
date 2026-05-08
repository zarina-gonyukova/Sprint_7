package order;

import clients.OrderClient;
import io.restassured.response.Response;
import models.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> color;
    private final OrderClient orderClient = new OrderClient();

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Collection<Object[]> getOrderColor() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Collections.emptyList()}
        });
    }

    @Test
    public void shouldCreateOrderWithDifferentColorsAndReturnTrack() {
        Order order = Order.defaultOrder(color);
        Response response = orderClient.createOrder(order);
        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}