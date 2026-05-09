package courier;

import clients.CourierClient;
import generator.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;

public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        if (courier != null) {
            Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
            if (loginResponse.statusCode() == SC_OK && loginResponse.jsonPath().get("id") != null) {
                courierId = loginResponse.jsonPath().getInt("id");
                courierClient.deleteCourier(courierId);
            }
        }
    }

    @Test
    @DisplayName("Курьера можно создать")
    @Description("Успешное создание курьера возвращает 201 и ok: true")
    public void shouldCreateCourierSuccessfully() {
        courier = CourierGenerator.getRandomCourier();
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Повторное создание курьера с тем же логином возвращает 409")
    public void shouldReturnConflictWhenCreateDuplicateCourier() {
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(SC_CONFLICT)
                .body("message", containsString("уже используется"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без логина")
    @Description("Создание курьера без логина возвращает 400")
    public void shouldReturnErrorWhenLoginIsMissing() {
        courier = CourierGenerator.getRandomCourier();
        courier.setLogin(null);
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без пароля")
    @Description("Создание курьера без пароля возвращает 400")
    public void shouldReturnErrorWhenPasswordIsMissing() {
        courier = CourierGenerator.getRandomCourier();
        courier.setPassword(null);
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    @DisplayName("Курьера можно создать без firstName")
    @Description("Создание курьера без firstName возвращает 201 и ok: true")
    public void shouldCreateCourierWithoutFirstName() {
        courier = CourierGenerator.getRandomCourier();
        courier.setFirstName(null);
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }
}