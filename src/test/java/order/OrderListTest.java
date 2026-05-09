package order;

import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Запрос списка заказов возвращает 200 и список orders")
    public void shouldReturnOrdersList() {
        Response response = orderClient.getOrdersList();
        response.then()
                .statusCode(SC_OK)
                .body("orders", notNullValue());
    }
}