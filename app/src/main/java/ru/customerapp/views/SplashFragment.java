package ru.customerapp.views;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.customerapp.R;

public class SplashFragment extends Fragment {
    public SplashFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        SplashActivity splashActivity = (SplashActivity)getActivity();
        splashActivity.onConnect(false);

        return view;
    }

}
