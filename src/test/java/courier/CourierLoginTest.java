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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
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
    @DisplayName("Курьер может авторизоваться")
    @Description("Успешная авторизация возвращает 200 и id")
    public void shouldLoginCourierSuccessfullyAndReturnId() {
        Response response = courierClient.loginCourier(CourierCredentials.from(courier));
        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка авторизации без логина")
    @Description("Авторизация без логина возвращает 400")
    public void shouldReturnErrorWhenLoginIsMissing() {
        Response response = courierClient.loginCourierWithoutLogin(courier.getPassword());
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    @DisplayName("Ошибка авторизации без пароля")
    @Description("Авторизация без пароля возвращает 504")
    public void shouldReturnErrorWhenPasswordIsMissing() {
        Response response = courierClient.loginCourierWithoutPassword(courier.getLogin());
        response.then()
                .statusCode(SC_GATEWAY_TIMEOUT);
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным логином")
    @Description("Авторизация с неверным логином возвращает 404")
    public void shouldReturnErrorWithWrongLogin() {
        CourierCredentials credentials = new CourierCredentials(
                "wrongLogin_" + System.currentTimeMillis(),
                courier.getPassword()
        );
        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("не найдена"));
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным паролем")
    @Description("Авторизация с неверным паролем возвращает 404")
    public void shouldReturnErrorWithWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(
                courier.getLogin(),
                "wrongPassword_" + System.currentTimeMillis()
        );
        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("не найдена"));
    }

    @Test
    @DisplayName("Ошибка авторизации несуществующего курьера")
    @Description("Авторизация несуществующего курьера возвращает 404")
    public void shouldReturnErrorForNonexistentCourier() {
        CourierCredentials credentials = new CourierCredentials(
                "notExisting_" + System.currentTimeMillis(),
                "notExisting_" + System.currentTimeMillis()
        );
        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("не найдена"));
    }
}