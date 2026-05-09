package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    public static Order defaultOrder(List<String> color) {
        return new Order(
                "Иван", "Петров", "Москва, Ленина 1",
                "Черкизовская", "+79998887766", 3,
                "2026-06-01", "Комментарий к заказу",
                color != null ? color : Collections.emptyList()
        );
    }
}