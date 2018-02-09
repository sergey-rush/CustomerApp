package ru.customerapp.core;

/**
 * Created by rash on 08.02.2018.
 */

public enum OrderStatus {
    None(0),
    Created(1),
    Confirmed(2),
    Paid(3),
    Delivered(4),
    Cancelled(5);

    private final int value;

    private OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderStatus fromInt(int value) {
        for (OrderStatus status : OrderStatus.values()) {
            int statusValue = status.getValue();
            if (statusValue == value) {
                return status;
            }
        }
        return OrderStatus.None;//For values out of enum scope
    }
}