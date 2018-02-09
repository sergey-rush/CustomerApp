package ru.customerapp.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ru.customerapp.R;
import ru.customerapp.core.AppContext;
import ru.customerapp.core.Section;
import ru.customerapp.data.DataAccess;
import ru.customerapp.web.SectionProvider;
import ru.customerapp.web.WebContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SectionFragment.OnFragmentInteractionListener,
        ProductListFragment.OnFragmentInteractionListener{

    private Context context;
    private DataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbMain);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        context = getApplicationContext();
        dataAccess = DataAccess.getInstance(context);
        List<Section> sections = dataAccess.getSections();
        int sectionSize = sections.size();
        if (sectionSize == 0) {
            SectionAsyncTask sectionAsyncTask = new SectionAsyncTask();
            sectionAsyncTask.execute();
        }
        else {
            AppContext appContext = AppContext.getInstance(context);
            appContext.SectionList = sections;
        }

        showFragment(new MainFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            fragment = new MainFragment();
        } else if (id == R.id.nav_product_list) {
            fragment = new SectionFragment();
        } else if (id == R.id.nav_cart) {
            fragment = new CartFragment();
        } else if (id == R.id.nav_order_list) {
            fragment = new OrderListFragment();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        showFragment(fragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private void loadDataCallback() {

        Toast.makeText(MainActivity.this, "Sections downloaded", Toast.LENGTH_SHORT).show();
    }

    private class SectionAsyncTask extends AsyncTask<Void, Void, Void> {
        private SectionAsyncTask() {
        }

        private ProgressDialog pDialog;
        private int responseCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Пожалуйста, подождите...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            SectionProvider sectionProvider = new SectionProvider();
            responseCode = sectionProvider.getSections();
            if (responseCode == 200) {
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

            loadDataCallback();
        }
    }
}
