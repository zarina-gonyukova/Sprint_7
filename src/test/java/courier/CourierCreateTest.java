package courier;

import clients.CourierClient;
import generator.CourierGenerator;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
            if (loginResponse.statusCode() == 200 && loginResponse.jsonPath().get("id") != null) {
                courierId = loginResponse.jsonPath().getInt("id");
                courierClient.deleteCourier(courierId);
            }
        }
    }

    @Test
    public void shouldCreateCourierSuccessfully() {
        courier = CourierGenerator.getRandomCourier();
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    public void shouldReturnConflictWhenCreateDuplicateCourier() {
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(409)
                .body("message", containsString("уже используется"));
    }

    @Test
    public void shouldReturnErrorWhenLoginIsMissing() {
        courier = CourierGenerator.getRandomCourier();
        courier.setLogin(null);
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    public void shouldReturnErrorWhenPasswordIsMissing() {
        courier = CourierGenerator.getRandomCourier();
        courier.setPassword(null);
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    public void shouldCreateCourierWithoutFirstName() {
        courier = CourierGenerator.getRandomCourier();
        courier.setFirstName(null);
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }
}