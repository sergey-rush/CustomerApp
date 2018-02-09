package ru.customerapp.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import ru.customerapp.R;
import ru.customerapp.data.DataAccess;
import ru.customerapp.web.SectionProvider;
import ru.customerapp.web.WebContext;

public class SplashActivity extends AppCompatActivity {

    private Context context;
    private DataAccess dataAccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = getApplicationContext();
        dataAccess = DataAccess.getInstance(context);
        //showFragment(new OfflineFragment());
        showFragment(new SplashFragment());
    }

    public void onConnect(boolean showDialog){
        SectionAsyncTask sectionAsyncTask = new SectionAsyncTask(showDialog);
        sectionAsyncTask.execute();
    }

    private class SectionAsyncTask extends AsyncTask<Void, Void, Void> {

        private int responseCode;
        private ProgressDialog pDialog;
        private boolean showDialog;
        private SectionAsyncTask(boolean showDialog) {
            this.showDialog = showDialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SplashActivity.this);
            pDialog.setMessage("Пожалуйста, подождите...");
            pDialog.setCancelable(false);
            if (showDialog) {
                pDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            SectionProvider sectionProvider = new SectionProvider();
            responseCode = sectionProvider.getSections();
            if (responseCode == 200) {
                dataAccess.deleteData();
                WebContext webContext = WebContext.getInstance();
                dataAccess.addSections(webContext.SectionList);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (responseCode == 200) {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }else{
                showFragment(new OfflineFragment());
            }
        }
    }

    public void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flSplash, fragment).commit();
        }
    }
}
