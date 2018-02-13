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
import ru.customerapp.web.RegisterProvider;
import ru.customerapp.web.WebContext;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    public RegisterFragment() {}
    private Context context;
    private View pbRegister;
    private View svRegisterForm;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private RegisterAsyncTask registerAsyncTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        context = getActivity();
        
        pbRegister =  view.findViewById(R.id.pbRegister);
        svRegisterForm = view.findViewById(R.id.svRegisterForm);

        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etRepeatPassword = (EditText) view.findViewById(R.id.etRepeatPassword);
        Button btnSignIn = (Button) view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                attemptRegister();
                break;
        }
    }

    private void attemptRegister() {
        if (registerAsyncTask != null) {
            return;
        }

        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        String repeatPassword = etRepeatPassword.getText().toString();


        // Reset errors.
        etFirstName.setError(null);
        etLastName.setError(null);
        etEmail.setError(null);
        etPhone.setError(null);
        etPassword.setError(null);
        etRepeatPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid firstName.
        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError(getString(R.string.field_required));
            focusView = etFirstName;
            cancel = true;
        }

        // Check for a valid lastName.
        if (TextUtils.isEmpty(lastName)) {
            etLastName.setError(getString(R.string.field_required));
            focusView = etLastName;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.field_required));
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError(getString(R.string.invalid_email));
            focusView = etEmail;
            cancel = true;
        }

        // Check for a valid phone number.
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError(getString(R.string.field_required));
            focusView = etPhone;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            etPhone.setError(getString(R.string.invalid_phone));
            focusView = etPhone;
            cancel = true;
        }

        // Check for a valid password.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid repeat password.
        if (!TextUtils.isEmpty(repeatPassword) && !isPasswordValid(repeatPassword)) {
            etRepeatPassword.setError(getString(R.string.invalid_password));
            focusView = etRepeatPassword;
            cancel = true;
        }

        // Check for a equal password and repeat password.
        if (!isPasswordsEqual(password, repeatPassword)) {
            etRepeatPassword.setError(getString(R.string.invalid_password));
            focusView = etRepeatPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user register attempt.
            showProgress(true);
            //String name = String.format("%s %s", firstName, lastName);
            String postData = String.format("{\"firstName\":\"%s\", \"lastName\":\"%s\", \"email\":\"%s\", \"phone\":\"%s\", \"password\":\"%s\"}", firstName, lastName, email, phone, password);

            registerAsyncTask = new RegisterAsyncTask(postData);
            registerAsyncTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() == 10;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isPasswordsEqual(String password, String repeatPassword) {
        return password.equals(repeatPassword);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            svRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
            svRegisterForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    svRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            pbRegister.setVisibility(show ? View.VISIBLE : View.GONE);
            pbRegister.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pbRegister.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pbRegister.setVisibility(show ? View.VISIBLE : View.GONE);
            svRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void loadDataCallback() {
        //Toast.makeText(context, "Register success!", Toast.LENGTH_SHORT).show();
        AppContext appContext = AppContext.getInstance(context);
        WebContext webContext = WebContext.getInstance();
        appContext.User = webContext.User;
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.showFragment(new ProfileFragment());

    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

        private int responseCode;
        private final String postData;
        private RegisterAsyncTask(String postData) {
            this.postData = postData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            RegisterProvider registerProvider = new RegisterProvider();
            responseCode = registerProvider.logIn(postData);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            registerAsyncTask = null;
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
