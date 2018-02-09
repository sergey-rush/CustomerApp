package ru.customerapp.core;

import java.util.Date;
import java.util.List;

/**
 * Created by rash on 08.02.2018.
 */

public class Order {
    public int Id;
    public String OrderUid;
    public OrderStatus OrderStatus;
    public List<OrderItem> OrderItems;
    public Date Created;
}
