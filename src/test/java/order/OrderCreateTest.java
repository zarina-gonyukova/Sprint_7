package order;

import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> color;
    private final OrderClient orderClient = new OrderClient();
    private Integer track;

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

    @After
    public void tearDown() {
        if (track != null) {
            orderClient.cancelOrder(track);
        }
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    @Description("Создание заказа возвращает 201 и track в теле ответа")
    public void shouldCreateOrderWithDifferentColorsAndReturnTrack() {
        Order order = Order.defaultOrder(color);
        Response response = orderClient.createOrder(order);
        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
        track = response.jsonPath().getInt("track");
    }
}