package courier;

import clients.CourierClient;
import generator.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierTest {

    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Успешное удаление курьера")
    @Description("Удаление существующего курьера возвращает 200 и ok: true")
    public void shouldDeleteCourierSuccessfully() {
        Courier courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        int courierId = loginResponse.then()
                .statusCode(SC_OK)
                .extract()
                .path("id");
        Response response = courierClient.deleteCourier(courierId);
        response.then()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Ошибка удаления курьера без id")
    @Description("Удаление курьера без id возвращает 404")
    public void shouldReturnErrorWhenDeleteCourierWithoutId() {
        Response response = courierClient.deleteCourierWithoutId();
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("Not Found"));
    }

    @Test
    @DisplayName("Ошибка удаления курьера с несуществующим id")
    @Description("Удаление курьера с неверным id возвращает 404")
    public void shouldReturnErrorWhenDeleteCourierWithWrongId() {
        Response response = courierClient.deleteCourier(999999);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("Курьера с таким id"));
    }
}