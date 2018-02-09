package ru.customerapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;

import ru.customerapp.R;
import ru.customerapp.core.AppContext;
import ru.customerapp.core.Section;
import ru.customerapp.core.SectionAdapter;
import ru.customerapp.data.DataAccess;

public class SectionFragment extends Fragment implements View.OnClickListener{

    private DataAccess dataAccess;
    private AppContext appContext;
    private Context context;
    private View view;
    private int parentId;

    public SectionFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_section, container, false);
        context = getActivity();
        dataAccess = DataAccess.getInstance(context);
        appContext = AppContext.getInstance(context);
        if (appContext.SectionList == null) {
            SectionAsyncTask sectionAsyncTask = new SectionAsyncTask();
            sectionAsyncTask.execute();
        } else {
            loadDataCallback();
        }
        return view;
    }

    private void loadDataCallback() {
        List<Section> sectionList = getSections(appContext.SectionList);
        SectionAdapter adapter = new SectionAdapter(context, sectionList);
        ListView listView = (ListView) view.findViewById(R.id.lvSectiones);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RelativeLayout layout = (RelativeLayout) view;
                TextView tvName = (TextView) layout.findViewById(R.id.tvName);
                int id = (int) tvName.getTag();
                Section section = getSection(id);
                if (section.ChildIndex == 2) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    ProductListFragment productListFragment = ProductListFragment.newInstance(section.SectionId);
                    mainActivity.showFragment(productListFragment);
                } else {
                    parentId = section.SectionId;
                    loadDataCallback();
                }

                Toast.makeText(context, tvName.getText() + " ParentId: " + parentId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Section> getSections(List<Section> inputSections){
        List<Section> outputSections = new ArrayList<>();
        for (Section section : inputSections) {
            if(section.ParentId == parentId){
                outputSections.add(section);
            }
        }
        return outputSections;
    }

    private Section getSection(int id){
        Section outputSection = null;
        for (Section section : appContext.SectionList) {
            if(section.Id == id){
                outputSection = section;
                break;
            }
        }
        return outputSection;
    }

    @Override
    public void onClick(View view) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class SectionAsyncTask extends AsyncTask<Void, Void, Void> {
        private SectionAsyncTask() {
        }

        private ProgressDialog pDialog;

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

            appContext.SectionList = dataAccess.getSections();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            loadDataCallback();
        }
    }
}
