package ru.customerapp.views;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import ru.customerapp.R;

public class OfflineFragment extends Fragment implements View.OnClickListener{

    public OfflineFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offline, container, false);

        Button btnRetry = (Button) view.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRetry:
                retryConnect(view);
                break;
        }
    }

    private void retryConnect(View view){
        SplashActivity splashActivity = (SplashActivity)getActivity();
        splashActivity.onConnect(true);
    }
}
