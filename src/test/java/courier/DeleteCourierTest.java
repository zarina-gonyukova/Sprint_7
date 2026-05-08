package courier;

import clients.CourierClient;
import generator.CourierGenerator;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierTest {

    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    public void shouldDeleteCourierSuccessfully() {
        Courier courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        int courierId = loginResponse.then()
                .statusCode(200)
                .extract()
                .path("id");
        Response response = courierClient.deleteCourier(courierId);
        response.then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    public void shouldReturnErrorWhenDeleteCourierWithoutId() {
        Response response = courierClient.deleteCourierWithoutId();
        response.then()
                .statusCode(404)
                .body("message", containsString("Not Found"));
    }

    @Test
    public void shouldReturnErrorWhenDeleteCourierWithWrongId() {
        Response response = courierClient.deleteCourier(999999);
        response.then()
                .statusCode(404)
                .body("message", containsString("Курьера с таким id"));
    }
}