package ru.customerapp;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import ru.customerapp.core.AppContext;
import ru.customerapp.core.Section;
import ru.customerapp.data.DataAccess;
import ru.customerapp.views.MainActivity;
import ru.customerapp.web.SectionProvider;
import ru.customerapp.web.WebContext;

/**
 * Created by rash on 06.02.2018.
 */

public class CustomerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
