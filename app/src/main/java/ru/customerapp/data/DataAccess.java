package ru.customerapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import ru.customerapp.core.Order;
import ru.customerapp.core.OrderItem;
import ru.customerapp.core.OrderStatus;
import ru.customerapp.core.Product;
import ru.customerapp.core.Section;

/**
 * Created by rash on 06.02.2018.
 */
public abstract class DataAccess extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CUSTOMERDB.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    protected SQLiteDatabase db;

    protected DataAccess(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        db = getWritableDatabase();
    }

    private static DataAccess _instance = null;

    public static DataAccess getInstance(Context context) {
        if (_instance == null) {
            _instance = new DataProvider(context);
        }
        return _instance;
    }

    public abstract List<Section> getSections();

    public abstract Section getSectionBySectionId(int sectionId);

    public abstract void addSections(List<Section> sections);


    public abstract Product getProductById(int id);

    public abstract List<Product> getProductsBySectionId(int sectionId);

    public abstract void addProducts(List<Product> products, int sectionId);

    public abstract boolean updateProductPicture(Product product);

    public abstract boolean deleteProducts();

    public abstract boolean deleteProductsBySectionId(int sectionId);

    public abstract List<Order> getOrdersByOrderStatus(OrderStatus orderStatus);

    public abstract Order getOrderByOrderStatus(OrderStatus orderStatus);

    public abstract Order getOrderById(int id);

    public abstract int insertOrder(Order order);

    public abstract boolean deleteOrderById(int id);

    public abstract List<OrderItem> getOrderItemsByOrderId(int orderId);

    public abstract OrderItem getOrderItemById(int id);

    public abstract int insertOrderItem(OrderItem orderItem);

    public abstract boolean deleteOrderItemsByOrderId(int orderId);

    public abstract void deleteData();


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Sections(Id INTEGER PRIMARY KEY AUTOINCREMENT, SectionId INTEGER, ParentId INTEGER, ChildIndex INTEGER, Name TEXT, QueryString TEXT)");
        db.execSQL("CREATE TABLE Products(Id INTEGER PRIMARY KEY AUTOINCREMENT, SectionId INTEGER, ProductUid TEXT, Name TEXT, Quantity INTEGER, Price TEXT, Discount TEXT, Sku TEXT, BoxSize TEXT, ImageUrl TEXT, Picture BLOB)");
        db.execSQL("CREATE TABLE Orders(Id INTEGER PRIMARY KEY AUTOINCREMENT, OrderUid TEXT, OrderStatus INTEGER, Created TEXT)");
        db.execSQL("CREATE TABLE OrderItems(Id INTEGER PRIMARY KEY AUTOINCREMENT, OrderId INTEGER, ProductUid TEXT, OrderStatus INTEGER, Quantity INTEGER, Amount TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Sections");
        db.execSQL("DROP TABLE IF EXISTS Products");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS OrderItems");
        onCreate(db);
    }

    /**
     * Drops all tables in current database
     */
    public void onDrop() {
        context.deleteDatabase(DATABASE_NAME);
        Log.d("Drop", "Database is dropped");
    }
}
