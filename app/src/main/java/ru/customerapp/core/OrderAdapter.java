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

public class OrderAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<Order> orderList;
    private AppContext appContext;

    public OrderAdapter(Context context, List<Order> orderList) {
        super(context, R.layout.order_item);
        this.context = context;
        this.orderList = orderList;
        appContext = AppContext.getInstance(context);
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.order_item, parent, false);
            holder.tvId = (TextView) view.findViewById(R.id.tvId);
            holder.tvOrderUid = (TextView) view.findViewById(R.id.tvOrderUid);
            holder.tvOrderStatus = (TextView) view.findViewById(R.id.tvOrderStatus);
            holder.tvCreated = (TextView) view.findViewById(R.id.tvCreated);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Order order = orderList.get(position);
        holder.tvId.setText(Integer.toString(order.Id));
        holder.tvId.setTag(order.Id);
        holder.tvOrderUid.setText(order.OrderUid);
        holder.tvOrderStatus.setText(order.OrderStatus.toString());
        holder.tvCreated.setText(appContext.formatDate(order.Created));
        return view;
    }

    static class ViewHolder {
        TextView tvId;
        TextView tvOrderUid;
        TextView tvOrderStatus;
        TextView tvCreated;
    }
}

