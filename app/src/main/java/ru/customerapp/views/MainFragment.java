package ru.customerapp.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.customerapp.R;

public class MainFragment extends Fragment {

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tlbMain);
        toolbar.setTitle(getString(R.string.main_fragment_title));
        return view;
    }
}
