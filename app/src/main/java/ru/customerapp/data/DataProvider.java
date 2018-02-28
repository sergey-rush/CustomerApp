package ru.customerapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.customerapp.core.Order;
import ru.customerapp.core.OrderItem;
import ru.customerapp.core.OrderStatus;
import ru.customerapp.core.Product;
import ru.customerapp.core.Section;

/**
 * Created by rash on 06.02.2018.
 */

public class DataProvider extends DataAccess {

    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public DataProvider(Context context) {
        super(context);
    }

    @Override
    public List<Section> getSections() {
        List<Section> sections = new ArrayList<Section>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, SectionId, ParentId, ChildIndex, Name, QueryString FROM Sections", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Section section = new Section();
                section.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                section.SectionId = cursor.getInt(cursor.getColumnIndex("SectionId"));
                section.ParentId = cursor.getInt(cursor.getColumnIndex("ParentId"));
                section.ChildIndex = cursor.getInt(cursor.getColumnIndex("ChildIndex"));
                section.Name = cursor.getString(cursor.getColumnIndex("Name"));
                section.QueryString = cursor.getString(cursor.getColumnIndex("QueryString"));
                sections.add(section);
                cursor.moveToNext();
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return sections;
    }

    @Override
    public Section getSectionBySectionId(int sectionId) {
        Section section = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, SectionId, ParentId, ChildIndex, Name, QueryString FROM Sections WHERE SectionId = ?", new String[]{String.valueOf(sectionId)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                section = new Section();
                section.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                section.SectionId = cursor.getInt(cursor.getColumnIndex("SectionId"));
                section.ParentId = cursor.getInt(cursor.getColumnIndex("ParentId"));
                section.ChildIndex = cursor.getInt(cursor.getColumnIndex("ChildIndex"));
                section.Name = cursor.getString(cursor.getColumnIndex("Name"));
                section.QueryString = cursor.getString(cursor.getColumnIndex("QueryString"));
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return section;
    }
    
    @Override
    public void addSections(List<Section> sections) {
        for (Section section : sections) {
            section.Id = insertSection(section);
        }
    }

    private int insertSection(Section section) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("SectionId", section.SectionId);
        contentValues.put("ParentId", section.ParentId);
        contentValues.put("ChildIndex", section.ChildIndex);
        contentValues.put("Name", section.Name);
        contentValues.put("QueryString", section.QueryString);
        int ret = (int) db.insert("Sections", null, contentValues);
        return ret;
    }



    @Override
    public Product getProductById(int id) {
        Product product = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, SectionId, ProductUid, Name, Quantity, Price, Discount, Barcode, BoxSize, ImageUrl, Picture FROM Products WHERE Id = ?", new String[]{String.valueOf(id)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                product = new Product();
                product.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                product.SectionId = cursor.getInt(cursor.getColumnIndex("SectionId"));
                product.ProductUid = cursor.getString(cursor.getColumnIndex("ProductUid"));
                product.Name = cursor.getString(cursor.getColumnIndex("Name"));
                product.Quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                product.Price = cursor.getString(cursor.getColumnIndex("Price"));
                product.Discount = cursor.getString(cursor.getColumnIndex("Discount"));
                product.Barcode = cursor.getString(cursor.getColumnIndex("Barcode"));
                product.BoxSize = cursor.getString(cursor.getColumnIndex("BoxSize"));
                product.ImageUrl = cursor.getString(cursor.getColumnIndex("ImageUrl"));
                product.Picture = cursor.getBlob(cursor.getColumnIndex("Picture"));
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return product;
    }
    
    @Override
    public List<Product> getProductsBySectionId(int sectionId) {
        List<Product> products = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, SectionId, ProductUid, Name, Quantity, Price, Discount, Barcode, BoxSize, ImageUrl, Picture FROM Products WHERE SectionId = ?", new String[]{String.valueOf(sectionId)});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Product product = new Product();
                product.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                product.SectionId = cursor.getInt(cursor.getColumnIndex("SectionId"));
                product.ProductUid = cursor.getString(cursor.getColumnIndex("ProductUid"));
                product.Name = cursor.getString(cursor.getColumnIndex("Name"));
                product.Quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                product.Price = cursor.getString(cursor.getColumnIndex("Price"));
                product.Discount = cursor.getString(cursor.getColumnIndex("Discount"));
                product.Barcode = cursor.getString(cursor.getColumnIndex("Barcode"));
                product.BoxSize = cursor.getString(cursor.getColumnIndex("BoxSize"));
                product.ImageUrl = cursor.getString(cursor.getColumnIndex("ImageUrl"));
                product.Picture = cursor.getBlob(cursor.getColumnIndex("Picture"));
                products.add(product);
                cursor.moveToNext();
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return products;
    }

    @Override
    public void addProducts(List<Product> products, int sectionId){
        for (Product product : products) {
            product.SectionId = sectionId;
            product.Id = insertProduct(product);
        }
    }

    private int insertProduct(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("SectionId", product.SectionId);
        contentValues.put("ProductUid", product.ProductUid);
        contentValues.put("Name", product.Name);
        contentValues.put("Quantity", product.Quantity);
        contentValues.put("Price", product.Price);
        contentValues.put("Discount", product.Discount);
        contentValues.put("Barcode", product.Barcode);
        contentValues.put("BoxSize", product.BoxSize);
        contentValues.put("ImageUrl", product.ImageUrl);
        int ret = (int) db.insert("Products", null, contentValues);
        return ret;
    }

    @Override
    public boolean updateProductPicture(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Picture", product.Picture);
        int ret = (int) db.update("Products", contentValues, "Id = ?", new String[]{String.valueOf(product.Id)});
        return ret == 1;
    }

    @Override
    public boolean deleteProducts() {
        int ret = db.delete("Products", null, null);
        return ret == 1;
    }

    @Override
    public boolean deleteProductsBySectionId(int sectionId) {
        int ret = db.delete("Products", "SectionId = ?", new String[]{String.valueOf(sectionId)});
        return ret == 1;
    }

    @Override
    public List<Order> getOrdersByOrderStatus(OrderStatus orderStatus) {
        List<Order> orders = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, OrderUid, OrderStatus, Created FROM Orders WHERE OrderStatus = ?", new String[]{String.valueOf(orderStatus.ordinal())});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Order order = new Order();
                order.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                order.OrderUid = cursor.getString(cursor.getColumnIndex("OrderUid"));
                order.OrderStatus = OrderStatus.fromInt(cursor.getInt(cursor.getColumnIndex("OrderStatus")));
                String created = cursor.getString(cursor.getColumnIndex("Created"));
                order.Created = format.parse(created);
                orders.add(order);
                cursor.moveToNext();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return orders;
    }

    @Override
    public Order getOrderByOrderStatus(OrderStatus orderStatus) {
        Order order = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, OrderUid, OrderStatus, Created FROM Orders WHERE OrderStatus = ?", new String[]{String.valueOf(orderStatus.ordinal())});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                order = new Order();
                order.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                order.OrderUid = cursor.getString(cursor.getColumnIndex("OrderUid"));
                order.OrderStatus = OrderStatus.fromInt(cursor.getInt(cursor.getColumnIndex("OrderStatus")));
                String created = cursor.getString(cursor.getColumnIndex("Created"));
                order.Created = format.parse(created);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return order;
    }

    @Override
    public Order getOrderById(int id) {
        Order order = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, OrderUid, OrderStatus, Created FROM Orders WHERE Id = ?", new String[]{String.valueOf(id)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                order = new Order();
                order.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                order.OrderUid = cursor.getString(cursor.getColumnIndex("OrderUid"));
                order.OrderStatus = OrderStatus.fromInt(cursor.getInt(cursor.getColumnIndex("OrderStatus")));
                String created = cursor.getString(cursor.getColumnIndex("Created"));
                order.Created = format.parse(created);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return order;
    }

    @Override
    public int insertOrder(Order order) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("OrderStatus", order.OrderStatus.ordinal());
        contentValues.put("Created", format.format(order.Created));
        int ret = (int) db.insert("Orders", null, contentValues);
        return ret;
    }

    @Override
    public boolean deleteOrderById(int id) {
        int ret = db.delete("Orders", "Id = ?", new String[]{String.valueOf(id)});
        return ret == 1;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, OrderId, ProductUid, OrderStatus, Quantity, Amount FROM OrderItems WHERE OrderId = ?", new String[]{String.valueOf(orderId)});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                OrderItem orderItem = new OrderItem();
                orderItem.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                orderItem.OrderId = cursor.getInt(cursor.getColumnIndex("OrderId"));
                orderItem.ProductUid = cursor.getString(cursor.getColumnIndex("ProductUid"));
                orderItem.OrderStatus = OrderStatus.fromInt(cursor.getInt(cursor.getColumnIndex("OrderStatus")));
                orderItem.Quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                orderItem.Amount = cursor.getString(cursor.getColumnIndex("Amount"));
                orderItems.add(orderItem);
                cursor.moveToNext();
            }        
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return orderItems;
    }

    @Override
    public OrderItem getOrderItemById(int id) {
        OrderItem orderItem = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Id, OrderId, ProductUid, OrderStatus, Quantity, Amount FROM OrderItems WHERE Id = ?", new String[]{String.valueOf(id)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                orderItem = new OrderItem();
                orderItem.Id = cursor.getInt(cursor.getColumnIndex("Id"));
                orderItem.OrderId = cursor.getInt(cursor.getColumnIndex("OrderId"));
                orderItem.ProductUid = cursor.getString(cursor.getColumnIndex("ProductUid"));
                orderItem.OrderStatus = OrderStatus.fromInt(cursor.getInt(cursor.getColumnIndex("OrderStatus")));
                orderItem.Quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                orderItem.Amount = cursor.getString(cursor.getColumnIndex("Amount"));
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return orderItem;
    }

    @Override
    public int insertOrderItem(OrderItem orderItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("OrderId", orderItem.OrderId);
        contentValues.put("ProductUid", orderItem.ProductUid);
        contentValues.put("OrderStatus", orderItem.OrderStatus.ordinal());
        contentValues.put("Quantity", orderItem.Quantity);
        contentValues.put("Amount", orderItem.Amount);
        int ret = (int) db.insert("OrderItems", null, contentValues);
        return ret;
    }

    @Override
    public boolean deleteOrderItemsByOrderId(int orderId) {
        int ret = db.delete("OrderItems", "OrderId = ?", new String[]{String.valueOf(orderId)});
        return ret == 1;
    }

    @Override
    public void deleteData() {
        db.delete("Sections", null, null);
        db.delete("Products", null, null);
    }
}
