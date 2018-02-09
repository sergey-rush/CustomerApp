package ru.customerapp.views;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.customerapp.R;
import ru.customerapp.core.AppContext;
import ru.customerapp.core.Order;
import ru.customerapp.core.OrderAdapter;
import ru.customerapp.core.OrderStatus;
import ru.customerapp.data.DataAccess;

public class OrderListFragment extends Fragment {

    public OrderListFragment() {
    }

    private DataAccess dataAccess;
    private AppContext appContext;
    private Context context;
    private View view;
    private int parentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order_list, container, false);
        context = getActivity();
        dataAccess = DataAccess.getInstance(context);
        appContext = AppContext.getInstance(context);


        List<Order> orderList = dataAccess.getOrdersByOrderStatus(OrderStatus.Created);

        OrderAdapter adapter = new OrderAdapter(context, orderList);
        ListView listView = (ListView) view.findViewById(R.id.lvOrders);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                TextView tvId = (TextView) view.findViewById(R.id.tvId);
                int orderId = (int) tvId.getTag();

                MainActivity mainActivity = (MainActivity) getActivity();
                OrderItemListFragment orderItemListFragment = OrderItemListFragment.newInstance(orderId);
                mainActivity.showFragment(orderItemListFragment);

                Toast.makeText(context, "OrderId: " + tvId.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

}
