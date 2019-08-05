package com.example.appauth.auth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.appauth.R;
import com.example.appauth.home.HomeActivity;
import com.example.appauth.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginFragment extends Fragment {

    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private TextInputLayout mInputEmail, mInputPassword;
    private ProgressDialog progressDialog;
    private NestedScrollView mLiniarSnack;
    private static final String TAG = "CLoneOlx";


    public LoginFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        progressDialog = new ProgressDialog(getContext());
        mLiniarSnack = (NestedScrollView) view.findViewById(R.id.mLiniarSnack);

        Button mLogin = (Button) view.findViewById(R.id.button_signin);
        Button mBtnForgotPassord = (Button) view.findViewById(R.id.ForgotPassword);
        mUsernameView = (AutoCompleteTextView) view.findViewById(R.id.email);
        mPasswordView = (EditText) view.findViewById(R.id.password);

        mInputEmail = (TextInputLayout) view.findViewById(R.id.EditInputEmail);
        mInputPassword = (TextInputLayout) view.findViewById(R.id.EditInputPassword);

        mUsernameView.addTextChangedListener(new MyTextWatcher(mInputEmail));
        mPasswordView.addTextChangedListener(new MyTextWatcher(mInputPassword));

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickedLogin();
            }

        });
        mBtnForgotPassord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotUserNameEmail();
            }
        });

        return view;
    }

    private void ForgotUserNameEmail() {
        startActivity(new Intent(getContext(), ForgotActivity.class));
    }

    private void ClickedLogin() {

        Log.d(TAG, "Login");

        if (!validateUsername()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (isInternetAvailable()) {

            Log.d("OnClick", "SignInStart");
            final String email = mUsernameView.getText().toString().toLowerCase();
            final String password = mPasswordView.getText().toString();

            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Authenticating");
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (email.contains("@")) {

                if (!validateEmail()) {
                    onLoginFailed();
                    return;
                }

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("email", email);
                query.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user == null) {

                            Log.d("correu", "a primeira requisicao falhou.");

                            Snackbar.make(mLiniarSnack, R.string.login_email_fail, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_verify, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).setActionTextColor(Color.WHITE).show();

                            progressDialog.dismiss();

                        } else {

                            String actualUsername = user.getUsername();
                            ParseUser.logInInBackground(actualUsername, password, new LogInCallback() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    if (parseUser != null) {
                                        Log.d("OnClick", "User");

                                        if (parseUser.getBoolean("emailVerified")) {

                                            Intent intent = new Intent(getContext(), HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            progressDialog.dismiss();

                                        } else if (!parseUser.getBoolean("emailVerified")) {

                                            Intent intent = new Intent(getContext(), HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                        }


                                    } else {
                                        Log.d("OnClick", e.toString());


                                        switch (e.getCode()) {

                                            case ParseException.OBJECT_NOT_FOUND:
                                                Log.d("Invalido", "Login");

                                                Snackbar.make(mLiniarSnack, R.string.login_inc, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_verify, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                    }
                                                }).setActionTextColor(Color.WHITE).show();

                                                progressDialog.dismiss();

                                                break;

                                            case ParseException.USERNAME_MISSING:
                                                Log.d("CloneOLX", "tempo acabou");

                                                Snackbar.make(mLiniarSnack, R.string.login_incor, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_verify, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                }).setActionTextColor(Color.WHITE).show();

                                                progressDialog.dismiss();

                                                break;

                                            case ParseException.INVALID_EMAIL_ADDRESS:
                                                Log.d("CloneOLX", "tempo acabou");

                                                Snackbar.make(mLiniarSnack, R.string.login_inco_email, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_inc_veri, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                }).setActionTextColor(Color.WHITE).show();

                                                progressDialog.dismiss();

                                                break;
                                            case ParseException.EMAIL_NOT_FOUND:
                                                Log.d("CloneOLX", "tempo acabou");

                                                Snackbar.make(mLiniarSnack, R.string.login_not_fou, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_verify2, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                }).setActionTextColor(Color.WHITE).show();

                                                progressDialog.dismiss();

                                                break;

                                            case ParseException.TIMEOUT:
                                                Log.d("CloneOLX", "tempo acabou");

                                                Snackbar.make(mLiniarSnack, R.string.login_request, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                }).setActionTextColor(Color.WHITE).show();

                                                progressDialog.dismiss();

                                                break;

                                            case ParseException.CONNECTION_FAILED:
                                                Log.d("CloneOLX", "connexao falhou");

                                                Snackbar.make(mLiniarSnack, R.string.login_cont_con, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok2, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                }).setActionTextColor(Color.WHITE).show();

                                                progressDialog.dismiss();

                                                break;

                                        }

                                    }

                                }
                            });
                        }
                    }
                });

            } else {

                //showProgressBar(getString(R.string.login_auth2));

                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            Log.d("OnClick", "User");

                            if(parseUser.getBoolean("emailVerified")){

                                // Start an intent for the dispatch activity
                                Intent intent = new Intent(getContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();

                            }

                            else if (!parseUser.getBoolean("emailVerified"))
                            {

                                Intent intent = new Intent(getContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                progressDialog.dismiss();
                            }


                        } else {
                            Log.d("OnClick", e.toString());

                            switch (e.getCode()) {

                                case ParseException.OBJECT_NOT_FOUND:
                                    Log.d("Invalido", "Login");

                                    Snackbar.make(mLiniarSnack, R.string.login_inc, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_verify, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).setActionTextColor(Color.WHITE).show();

                                    progressDialog.dismiss();

                                    break;

                                case ParseException.USERNAME_MISSING:
                                    Log.d("MyApp", "time out");

                                    Snackbar.make(mLiniarSnack, R.string.login_incor, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_verify, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).setActionTextColor(Color.WHITE).show();

                                    progressDialog.dismiss();

                                    break;

                                case ParseException.INVALID_EMAIL_ADDRESS:
                                    Log.d("CloneOLX", "tempo acabou");

                                    Snackbar.make(mLiniarSnack, R.string.login_inco_email, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_inc_veri, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).setActionTextColor(Color.WHITE).show();

                                    progressDialog.dismiss();

                                    break;
                                case ParseException.EMAIL_NOT_FOUND:
                                    Log.d("CloneOLX", "tempo acabou");

                                    Snackbar.make(mLiniarSnack, R.string.login_not_fou, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_verify2, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).setActionTextColor(Color.WHITE).show();

                                    progressDialog.dismiss();

                                    break;

                                case ParseException.TIMEOUT:
                                    Log.d("CloneOLX", "tempo acabou");

                                    Snackbar.make(mLiniarSnack, R.string.login_request, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).setActionTextColor(Color.WHITE).show();

                                    progressDialog.dismiss();

                                    break;

                                case ParseException.CONNECTION_FAILED:
                                    Log.d("CloneOLX", "connexao falhou");

                                    Snackbar.make(mLiniarSnack, R.string.login_cont_con, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok2, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).setActionTextColor(Color.WHITE).show();

                                    progressDialog.dismiss();

                                    break;

                            }

                        }

                    }
                });
            }

        }else {

            showInternetConnectionLostMessage();
        }
    }

    //Validando
    private void onLoginFailed() {
        progressDialog.dismiss();
        Toast.makeText(getContext(), R.string.login_email_fail, Toast.LENGTH_LONG).show();
    }

    private boolean validateUsername() {

        //String email = mUsernameView.getText().toString().trim();

        if (mUsernameView.getText().toString().trim().isEmpty()) {
            mInputEmail.setError(getString(R.string.err_msg_user));
            requestFocus(mUsernameView);
            return false;
        } else {
            mInputEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {

        final String email = mUsernameView.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mInputEmail.setError(getString(R.string.login_email_fail));
            requestFocus(mUsernameView);
            return false;
        } else {
            mInputEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {

        if (mPasswordView.getText().toString().trim().isEmpty()) {
            mInputPassword.setError(getString(R.string.err_msg_password));
            requestFocus(mPasswordView);
            return false;
        } else {
            mInputPassword.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.email:
                    validateUsername();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showInternetConnectionLostMessage() {
        Snackbar.make(mLiniarSnack, R.string.login_no_int, Snackbar.LENGTH_SHORT).show();
    }
}
