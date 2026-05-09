package generator;

import models.Courier;

public class CourierGenerator {

    public static Courier getRandomCourier() {
        String unique = String.valueOf(System.currentTimeMillis());
        return new Courier(
                "courier_" + unique,
                "password_" + unique,
                "name_" + unique
        );
    }
}