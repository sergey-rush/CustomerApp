package ru.customerapp.core;

import android.content.Context;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.customerapp.data.DataAccess;

/**
 * Created by rash on 06.02.2018.
 */
public class AppContext {

    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private Context context;
    private NumberFormat numberFormat;
    private DecimalFormat decimalFormat;
    private DataAccess dataAccess;
    public List<Section> SectionList;
    public List<Product> ProductList;
    private Order order;

    private static AppContext current;

    public static AppContext getInstance(Context context) {
        if (current == null) {
            current = new AppContext(context);
        }
        return current;
    }

    private AppContext(Context context) {
        this.context = context;
        dataAccess = DataAccess.getInstance(context);
        Locale currentLocale = Locale.getDefault();
        numberFormat = NumberFormat.getNumberInstance(currentLocale);
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
        decimalFormat = new DecimalFormat("#.##", otherSymbols);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
    }

    public Order getCurrentOrder() {
        if (order == null) {
            order = dataAccess.getOrderByOrderStatus(OrderStatus.Created);
        }

        if (order == null) {
            order = new Order();
            order.OrderStatus = OrderStatus.Created;
            order.Created = new Date();
            order.Id = dataAccess.insertOrder(order);
        }
        return order;
    }

    public String getOriginalPrice(Product product) {
        String priceString = "";
        try {
            String productPrice = product.Price.replace(".", ",");
            Number priceNumber = numberFormat.parse(productPrice);
            double price = priceNumber.doubleValue();
            priceString = decimalFormat.format(price);
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        return priceString;
    }

    public String getDiscountedPrice(Product product) {
        String discountedPrice = "";
        try {
            String productPrice = product.Price.replace(".", ",");
            Number priceNumber = numberFormat.parse(productPrice);
            double price = priceNumber.doubleValue();
            String productDiscount = product.Discount.replace(".", ",");
            Number discountNumber = numberFormat.parse(productDiscount);
            double discount = discountNumber.doubleValue();
            double result = price - ((price / 100) * discount);
            discountedPrice = decimalFormat.format(result);
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        return discountedPrice;
    }

    public String getOrderItemAmount(Product product, int quantity) {
        String amountString = "";
        try {
            String productPrice = product.Price.replace(".", ",");
            Number priceNumber = numberFormat.parse(productPrice);
            double price = priceNumber.doubleValue();
            String productDiscount = product.Discount.replace(".", ",");
            Number discountNumber = numberFormat.parse(productDiscount);
            double discount = discountNumber.doubleValue();
            double discountedPrice = price - ((price / 100) * discount);
            double amount = discountedPrice * quantity;
            amountString = decimalFormat.format(amount);
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        return amountString;
    }

    public String formatDate(Date date){
        return dateFormat.format(date);
    }
}
