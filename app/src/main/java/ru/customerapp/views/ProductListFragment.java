package ru.customerapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.customerapp.R;
import ru.customerapp.core.AppContext;
import ru.customerapp.core.DividerItemDecoration;
import ru.customerapp.core.Product;
import ru.customerapp.core.ProductAdapter;
import ru.customerapp.core.RecyclerTouchListener;
import ru.customerapp.data.DataAccess;
import ru.customerapp.web.ProductProvider;
import ru.customerapp.web.WebContext;

public class ProductListFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_SECTION_ID = "sectionId";
    private int sectionId;

    private AppContext appContext;
    private DataAccess dataAccess;
    private Context context;
    //private RecyclerView recyclerView;
    private View view;
    private ProductAdapter adapter;

    public ProductListFragment() { }

    public static ProductListFragment newInstance(int sectionId) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_ID, sectionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionId = getArguments().getInt(ARG_SECTION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        context = getActivity();
        appContext = AppContext.getInstance(context);
        dataAccess = DataAccess.getInstance(context);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tlbMain);
        toolbar.setTitle(getString(R.string.product_list_fragment_title));
        //recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        ProductAsyncTask productAsyncTask = new ProductAsyncTask();
        productAsyncTask.execute();

        return view;
    }

    private void loadDataCallback() {

        adapter = new ProductAdapter(context, appContext.ProductList);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TextView tvProductName = (TextView) view.findViewById(R.id.tvProductName);
                int id = (int) tvProductName.getTag();
                MainActivity mainActivity = (MainActivity) getActivity();
                ProductFragment productFragment = ProductFragment.newInstance(id);
                mainActivity.showFragment(productFragment);
                //Toast.makeText(context, "Product clicked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onClick(View view) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class ProductAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProductAsyncTask() {
        }

        private ProgressDialog pDialog;
        private int responseCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Пожалуйста, подождите...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            appContext.ProductList = dataAccess.getProductsBySectionId(sectionId);
            int productSize = appContext.ProductList.size();

            if(productSize==0){
                ProductProvider productProvider = new ProductProvider();
                responseCode = productProvider.getProducts(sectionId);
                if (responseCode == 200) {
                    WebContext webContext = WebContext.getInstance();
                    dataAccess.addProducts(webContext.ProductList, sectionId);
                    appContext.ProductList = dataAccess.getProductsBySectionId(sectionId);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            loadDataCallback();
        }
    }
}
