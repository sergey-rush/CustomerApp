package ru.customerapp.views;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ru.customerapp.R;
import ru.customerapp.core.AppContext;
import ru.customerapp.web.LoginProvider;
import ru.customerapp.web.WebContext;

public class LoginFragment extends Fragment implements View.OnClickListener{

    public LoginFragment() {}
    private Context context;
    private View pbLogin;
    private View svLoginForm;
    private EditText etLogin;
    private EditText etPassword;
    private LoginAsyncTask loginAsyncTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        context = getActivity();
        pbLogin =  view.findViewById(R.id.pbLogin);
        svLoginForm = view.findViewById(R.id.svLoginForm);
        etLogin = (EditText) view.findViewById(R.id.etLogin);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        Button btnSignIn = (Button) view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                attemptLogin();
                break;
        }
    }

    private void attemptLogin() {
        if (loginAsyncTask != null) {
            return;
        }
        String postData = String.format("{\"login\":\"%s\", \"password\":\"%s\"}", etLogin.getText(), etPassword.getText());
        // Reset errors.
        etLogin.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etLogin.setError(getString(R.string.field_required));
            focusView = etLogin;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etLogin.setError(getString(R.string.invalid_email));
            focusView = etLogin;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            loginAsyncTask = new LoginAsyncTask(postData);
            loginAsyncTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            svLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            svLoginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    svLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            pbLogin.setVisibility(show ? View.VISIBLE : View.GONE);
            pbLogin.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pbLogin.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pbLogin.setVisibility(show ? View.VISIBLE : View.GONE);
            svLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void loadDataCallback() {
        //Toast.makeText(context, "Login success!", Toast.LENGTH_SHORT).show();
        AppContext appContext = AppContext.getInstance(context);
        WebContext webContext = WebContext.getInstance();
        appContext.User = webContext.User;
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.showFragment(new ProfileFragment());

    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        private int responseCode;
        private final String postData;
        private LoginAsyncTask(String postData) {
            this.postData = postData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            LoginProvider loginProvider = new LoginProvider();
            responseCode = loginProvider.logIn(postData);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            loginAsyncTask = null;
            showProgress(false);

            if (responseCode == 200) {
                loadDataCallback();
            } else {
                etPassword.setError(getString(R.string.invalid_password));
                etPassword.requestFocus();
            }
        }
    }
}
