package ru.customerapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import ru.customerapp.R;
import ru.customerapp.core.AppContext;
import ru.customerapp.core.Order;
import ru.customerapp.core.OrderItem;
import ru.customerapp.core.OrderStatus;
import ru.customerapp.core.Product;
import ru.customerapp.data.DataAccess;

public class ProductFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PRODUCT_ID = "productId";
    private int productId;
    private int quantity = 1;
    private View view;
    private AppContext appContext;
    private DataAccess dataAccess;
    private Context context;
    private Product product;
    private NumberFormat numberFormat;
    private DecimalFormat decimalFormat;

    public ProductFragment() {
    }

    public static ProductFragment newInstance(int productId) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt(ARG_PRODUCT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tlbMain);
        toolbar.setTitle(getString(R.string.product_fragment_title));
        context = getActivity();
        appContext = AppContext.getInstance(context);
        dataAccess = DataAccess.getInstance(context);
        product = dataAccess.getProductById(productId);

        ImageView ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
        TextView tvProductName = (TextView) view.findViewById(R.id.tvProductName);
        TextView tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
        TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        TextView tvBoxSize = (TextView) view.findViewById(R.id.tvBoxSize);
        TextView tvSku = (TextView) view.findViewById(R.id.tvSku);
        ImageView ivAddToCart = (ImageView) view.findViewById(R.id.ivAddToCart);
        ivAddToCart.setOnClickListener(this);

        Bitmap bitmap = BitmapFactory.decodeByteArray(product.Picture, 0, product.Picture.length);
        ivProductImage.setImageBitmap(bitmap);

        tvProductName.setText(product.Name);
        tvBoxSize.setText(product.BoxSize);
        tvSku.setText(product.Sku);

        String productPrice = appContext.getOriginalPrice(product);
        tvPrice.setText(productPrice);
        tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        String discountedPrice = appContext.getDiscountedPrice(product);
        tvDiscount.setText(discountedPrice);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivAddToCart:
                addToCart(view);
                break;
        }
    }

    private void addToCart(View view){
        Order order = appContext.getCurrentOrder();
        OrderItem orderItem = new OrderItem();
        orderItem.OrderId = order.Id;
        orderItem.ProductUid = product.ProductUid;
        orderItem.Quantity = quantity;
        orderItem.OrderStatus = OrderStatus.Created;
        orderItem.Amount = appContext.getOrderItemAmount(product, quantity);
        int orderItemId = dataAccess.insertOrderItem(orderItem);

        Toast.makeText(getActivity(), product.Name + " quantity " + quantity + "added to cart " + orderItemId, Toast.LENGTH_SHORT).show();
    }

}
