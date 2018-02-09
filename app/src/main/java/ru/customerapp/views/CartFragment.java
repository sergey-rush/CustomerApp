package ru.customerapp.views;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.customerapp.R;

public class CartFragment extends Fragment implements View.OnClickListener{

    public CartFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivAddToCart:
                //addToCart(view);
                break;
        }
    }

}
