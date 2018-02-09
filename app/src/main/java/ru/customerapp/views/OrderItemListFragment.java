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
import ru.customerapp.core.OrderItem;
import ru.customerapp.core.OrderItemAdapter;
import ru.customerapp.data.DataAccess;

public class OrderItemListFragment extends Fragment {
    private static final String ARG_ORDER_ID = "orderId";
    private int orderId;


    private DataAccess dataAccess;
    private AppContext appContext;
    private Context context;
    private View view;

    public OrderItemListFragment() { }

    public static OrderItemListFragment newInstance(int orderId) {
        OrderItemListFragment fragment = new OrderItemListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().getInt(ARG_ORDER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order_item_list, container, false);
        context = getActivity();
        dataAccess = DataAccess.getInstance(context);
        appContext = AppContext.getInstance(context);

    
        List<OrderItem> orderItemList = dataAccess.getOrderItemsByOrderId(orderId);
        
        OrderItemAdapter adapter = new OrderItemAdapter(context, orderItemList);
        ListView listView = (ListView) view.findViewById(R.id.lvOrderItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                TextView tvId = (TextView) view.findViewById(R.id.tvId);
                int id = (int) tvId.getTag();
//                OrderItem orderItem = getOrderItem(id);
//                if (orderItem.ChildIndex == 2) {
//                    MainActivity mainActivity = (MainActivity) getActivity();
//                    ProductListFragment productListFragment = ProductListFragment.newInstance(orderItem.OrderItemId);
//                    mainActivity.showFragment(productListFragment);
//                } else {
//                    parentId = orderItem.OrderItemId;
//                    loadDataCallback();
//                }

                Toast.makeText(context, "OrderItemId: " + tvId.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    

        return view;
    }

}
