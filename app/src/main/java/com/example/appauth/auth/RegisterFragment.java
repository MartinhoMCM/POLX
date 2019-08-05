package com.example.appauth.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.parse.ParseException;
import com.parse.SignUpCallback;

public class RegisterFragment extends Fragment {

    // UI references.
    private AutoCompleteTextView mRegisterEmailView;
    private EditText mRegisterPasswordView, mFirstName, mLastName,mUserName,mNumber;
    private TextInputLayout mRegisterInputEmail, mRegisterInputPassword, mFirstNameRegisterInput, mLastNameRegisterInput,mUserNameRegisterInput,mUserFirstNumberRegisterInput;
    private ProgressDialog progressDialog;
    private NestedScrollView mNeedSnackbar;
    private static final String TAG = "CLoneOlx";

    public RegisterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        progressDialog = new ProgressDialog(getContext());

        mNeedSnackbar = (NestedScrollView) view.findViewById(R.id.needSnackbar);
        mFirstName = (EditText) view.findViewById(R.id.first_name);
        mLastName = (EditText) view.findViewById(R.id.last_name);
        mUserName = (EditText) view.findViewById(R.id.user_name);
        mNumber = (EditText) view.findViewById(R.id.register_number);

        Button mRegisterBTN = (Button) view.findViewById(R.id.register_button_signin);
        mRegisterEmailView = (AutoCompleteTextView) view.findViewById(R.id.register_email);
        mRegisterPasswordView = (EditText) view.findViewById(R.id.register_password);

        mFirstNameRegisterInput = (TextInputLayout) view.findViewById(R.id.register_layout_input_first_name);
        mLastNameRegisterInput = (TextInputLayout) view.findViewById(R.id.register_layout_input_last_name);
        mRegisterInputEmail = (TextInputLayout) view.findViewById(R.id.register_layout_input_email);
        mRegisterInputPassword = (TextInputLayout) view.findViewById(R.id.register_layout_input_password);
        mUserNameRegisterInput = (TextInputLayout) view.findViewById(R.id.register_layout_input_user_name);
        mUserFirstNumberRegisterInput =(TextInputLayout) view.findViewById(R.id.register_layout_input_first_number);


        mFirstName.addTextChangedListener(new MyTextWatcher(mFirstNameRegisterInput));
        mLastName.addTextChangedListener(new MyTextWatcher(mLastNameRegisterInput));
        mRegisterEmailView.addTextChangedListener(new MyTextWatcher(mRegisterInputEmail));
        mRegisterPasswordView.addTextChangedListener(new MyTextWatcher(mRegisterInputPassword));
        mUserName.addTextChangedListener(new MyTextWatcher(mUserNameRegisterInput));
        mNumber.addTextChangedListener(new MyTextWatcher(mUserFirstNumberRegisterInput));


        mRegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickedRegister();
            }

        });

        return view;
    }

    private void ClickedRegister() {
        Log.d(TAG, "Registro");

        if (!validateUserName()) {
            return;
        }
        if (!validateFirstName()) {
            return;
        }
        if (!validateLastName()) {
            return;
        }
        if (!validateNumber()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }


        if (isInternetAvailable()){
            Log.d("OnClick", "RegisterStart");

            final User user = new User();

            user.setUserFirstName(mFirstName.getText().toString().trim());
            user.setUserLastName(mLastName.getText().toString().trim());
            user.setUsername(mUserName.getText().toString().toLowerCase().trim());
            user.setEmail(mRegisterEmailView.getText().toString().trim());
            user.setEmails(mRegisterEmailView.getText().toString().trim());
            user.setNumber(mNumber.getText().toString());
            user.setPassword(mRegisterPasswordView.getText().toString().trim());

            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Create your account");
            progressDialog.setCancelable(false);
            progressDialog.show();

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        //Ok! correu tudo bem app.
                        Toast.makeText(RegisterFragment.this.getContext(), "Bem vindo " + user.getUserFirstName() +user.getUserLastName(), Toast.LENGTH_SHORT).show();
                        RegisterFragment.this.startActivity(new Intent(RegisterFragment.this.getContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        progressDialog.dismiss();

                    } else {

                        progressDialog.dismiss();
                        switch (e.getCode()) {

                            case ParseException.TIMEOUT:
                                Log.d("CloneOLX", "tempo acabou");

                                Snackbar.make(mNeedSnackbar, R.string.signup_request_t, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).setActionTextColor(Color.WHITE).show();

                                progressDialog.dismiss();

                                break;

                            case ParseException.CONNECTION_FAILED:
                                Log.d("CloneOLX", "connexao falhou");

                                Snackbar.make(mNeedSnackbar, R.string.login_cant, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setActionTextColor(Color.WHITE).show();

                                progressDialog.dismiss();

                                break;

                            case ParseException.VALIDATION_ERROR:
                                Log.d("CloneOLX", "internal server error");

                                Snackbar.make(mNeedSnackbar, R.string.password_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setActionTextColor(Color.WHITE).show();

                                progressDialog.dismiss();

                                break;

                            case ParseException.EMAIL_TAKEN:
                                Log.d("CloneOLX", "email taken");

                                Snackbar.make(mNeedSnackbar, R.string.login_teken, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setActionTextColor(Color.WHITE).show();

                                progressDialog.dismiss();

                                break;
                            case ParseException.USERNAME_TAKEN:
                                Log.d("CloneOLX", "username taken");

                                Snackbar.make(mNeedSnackbar, R.string.signup_userta, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setActionTextColor(Color.rgb(255, 255, 168)).show();

                                progressDialog.dismiss();

                                break;

                            case ParseException.OTHER_CAUSE:
                                Log.d("CloneOLX", "tempo acabou");

                                Snackbar.make(mNeedSnackbar, R.string.signup_request, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setActionTextColor(Color.WHITE).show();


                                break;
                        }
                    }
                }
            });
           } else {
               showInternetConnectionLostMessage();
              progressDialog.dismiss();

        }
    }

    //Validando
    private boolean validateUserName() {
        if (mUserName.getText().toString().isEmpty() || mUserName.length() < 3) {
            mUserNameRegisterInput.setError(getString(R.string.signup_name_at));
            requestFocus(mUserName);
            return false;
        } else {
            mUserNameRegisterInput.setErrorEnabled(false);
        }
        return true;

    }

    private boolean validateFirstName() {
        if (mFirstName.getText().toString().isEmpty() || mFirstName.length() < 3) {
            mFirstNameRegisterInput.setError(getString(R.string.signup_name_at));
            requestFocus(mFirstName);
            return false;
        } else {
            mFirstNameRegisterInput.setErrorEnabled(false);
        }

        return true;

    }

    private boolean validateLastName() {
        if (mLastName.getText().toString().isEmpty() || mLastName.length() < 3) {
            mLastNameRegisterInput.setError(getString(R.string.signup_name_at));
            requestFocus(mLastName);
            return false;
        } else {
            mLastNameRegisterInput.setErrorEnabled(false);
        }
        return true;

    }

    private boolean validateNumber() {
        if (mNumber.getText().toString().isEmpty() || mNumber.length() < 9 ) {
            mUserFirstNumberRegisterInput.setError("Error number");
            requestFocus(mNumber);
            return false;
        } else {
            mUserFirstNumberRegisterInput.setErrorEnabled(false);
        }
        return true;

    }

    private boolean validateEmail() {
        String email = mRegisterEmailView.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mRegisterInputEmail.setError(getString(R.string.err_msg_email));
            requestFocus(mRegisterEmailView);
            return false;
        } else {
            mRegisterInputEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (mRegisterPasswordView.getText().toString().trim().isEmpty()) {
            mRegisterInputPassword.setError(getString(R.string.err_msg_password));
            requestFocus(mRegisterPasswordView);
            return false;
        } else {
            mRegisterInputPassword.setErrorEnabled(false);
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
                case R.id.first_name:
                    validateFirstName();
                    break;
                case R.id.last_name:
                    validateLastName();
                    break;
                case R.id.register_number:
                    validateNumber();
                    break;
                case R.id.register_email:
                    validateEmail();
                    break;
                case R.id.register_password:
                    validatePassword();
                    break;
            }
        }
    }

    private void showInternetConnectionLostMessage() {

        Snackbar.make(getView(), R.string.login_no_int, Snackbar.LENGTH_SHORT).show();
    }

    private boolean isInternetAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
