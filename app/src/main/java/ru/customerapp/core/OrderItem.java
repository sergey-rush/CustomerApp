package ru.customerapp.core;

import java.util.Date;

/**
 * Created by rash on 08.02.2018.
 */

public class OrderItem {
    public int Id;
    public int OrderId;
    public String ProductUid;
    public OrderStatus OrderStatus;
    public int Quantity;
    public String Amount;
}
