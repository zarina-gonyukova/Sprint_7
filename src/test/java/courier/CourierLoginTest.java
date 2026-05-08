package courier;

import clients.CourierClient;
import generator.CourierGenerator;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
            if (loginResponse.statusCode() == 200 && loginResponse.jsonPath().get("id") != null) {
                courierId = loginResponse.jsonPath().getInt("id");
                courierClient.deleteCourier(courierId);
            }
        }
    }

    @Test
    public void shouldLoginCourierSuccessfullyAndReturnId() {
        Response response = courierClient.loginCourier(CourierCredentials.from(courier));
        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    public void shouldReturnErrorWhenLoginIsMissing() {
        Response response = courierClient.loginCourierWithoutLogin(courier.getPassword());
        response.then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    public void shouldReturnErrorWhenPasswordIsMissing() {
        Response response = courierClient.loginCourierWithoutPassword(courier.getLogin());
        response.then()
                .statusCode(504);
    }

    @Test
    public void shouldReturnErrorWithWrongLogin() {
        CourierCredentials credentials = new CourierCredentials(
                "wrongLogin_" + System.currentTimeMillis(),
                courier.getPassword()
        );
        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(404)
                .body("message", containsString("не найдена"));
    }

    @Test
    public void shouldReturnErrorWithWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(
                courier.getLogin(),
                "wrongPassword_" + System.currentTimeMillis()
        );
        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(404)
                .body("message", containsString("не найдена"));
    }

    @Test
    public void shouldReturnErrorForNonexistentCourier() {
        CourierCredentials credentials = new CourierCredentials(
                "notExisting_" + System.currentTimeMillis(),
                "notExisting_" + System.currentTimeMillis()
        );
        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(404)
                .body("message", containsString("не найдена"));
    }
}