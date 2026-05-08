package models;

import java.util.Collections;
import java.util.List;

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

    public Order() {}

    public Order(String firstName, String lastName, String address, String metroStation,
                 String phone, int rentTime, String deliveryDate, String comment,
                 List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    public static Order defaultOrder(List<String> color) {
        return new Order(
                "Иван", "Петров", "Москва, Ленина 1",
                "Черкизовская", "+79998887766", 3,
                "2026-06-01", "Комментарий к заказу",
                color != null ? color : Collections.emptyList()
        );
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getMetroStation() { return metroStation; }
    public String getPhone() { return phone; }
    public int getRentTime() { return rentTime; }
    public String getDeliveryDate() { return deliveryDate; }
    public String getComment() { return comment; }
    public List<String> getColor() { return color; }

    @Override
    public String toString() {
        return "Order{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", metroStation='" + metroStation + '\'' +
                ", phone='" + phone + '\'' +
                ", rentTime=" + rentTime +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", comment='" + comment + '\'' +
                ", color=" + color +
                '}';
    }
}