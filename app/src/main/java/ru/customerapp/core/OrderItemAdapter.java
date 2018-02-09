package ru.customerapp.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.customerapp.R;

/**
 * Created by rash on 06.02.2018.
 */

public class OrderItemAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<OrderItem> orderItemList;
    private AppContext appContext;

    public OrderItemAdapter(Context context, List<OrderItem> orderItemList) {
        super(context, R.layout.orderitem_item);
        this.context = context;
        this.orderItemList = orderItemList;
        appContext = AppContext.getInstance(context);
    }

    @Override
    public int getCount() {
        return orderItemList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.orderitem_item, parent, false);
            holder.tvId = (TextView) view.findViewById(R.id.tvId);
            holder.tvProductUid = (TextView) view.findViewById(R.id.tvProductUid);
            holder.tvOrderStatus = (TextView) view.findViewById(R.id.tvOrderStatus);
            holder.tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            holder.tvAmount = (TextView) view.findViewById(R.id.tvAmount);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        OrderItem orderItem = orderItemList.get(position);
        holder.tvId.setText(Integer.toString(orderItem.Id));
        holder.tvId.setTag(orderItem.Id);
        holder.tvProductUid.setText(orderItem.ProductUid);
        holder.tvOrderStatus.setText(orderItem.OrderStatus.toString());
        holder.tvQuantity.setText(Integer.toString(orderItem.Quantity));
        holder.tvAmount.setText(orderItem.Amount);
        return view;
    }

    static class ViewHolder {
        TextView tvId;
        TextView tvProductUid;
        TextView tvOrderStatus;
        TextView tvQuantity;
        TextView tvAmount;
    }
}

