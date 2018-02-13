package ru.customerapp.views;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.customerapp.R;
import ru.customerapp.core.AppContext;
import ru.customerapp.core.User;

public class ProfileFragment extends Fragment {

    private Context context;
    public ProfileFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getActivity();
        AppContext appContext = AppContext.getInstance(context);
        User user = appContext.User;
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(user.Name);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvPhone.setText(user.Phone);
        TextView tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvEmail.setText(user.Email);
        return view;
    }
}
