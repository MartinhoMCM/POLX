package com.example.appauth.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appauth.R;
import com.example.appauth.home.HomeActivity;
import com.example.appauth.models.User;
import com.example.appauth.proload.ProloadActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotActivity extends AppCompatActivity {

    private TextInputLayout mInptuResent;
    private EditText mEditResent;
    //private Dialog dialog1;
    private Dialog progressDialog;
    private LinearLayout mLayout_login;
    private TextView mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        progressDialog = new Dialog(this);
        mInptuResent = (TextInputLayout) findViewById(R.id.inputResent);
        mEditResent = (EditText) findViewById(R.id.editResent);
        Button mSaveResent = (Button) findViewById(R.id.btn_Resent);
        mLayout_login = (LinearLayout) findViewById(R.id.layout_login);
        mLink = (TextView) findViewById(R.id.link_forgot) ;
        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotActivity.this, FormKitActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        mSaveResent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveResent();
            }
        });
    }



    private void SaveResent() {


        if (isInternetAvailable()) {

            final String email = mEditResent.getText().toString().toLowerCase();

            if (email.contains("@")) {

                if (!validateEmail()) {
                    //onLoginFailed();
                    return;
                }

                showProgressBar(getString(R.string.forgot_veri));

                ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {


                                    // An email was successfully sent with reset instructions.
                                    //dialog1.dismiss();
                                    // Dismiss progress bar
                                    progressDialog.dismiss();

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            ForgotActivity.this);
                                    alertDialogBuilder.setTitle(R.string.forgot_send2);
                                    alertDialogBuilder
                                            .setMessage(R.string.forgot_check)
                                            .setCancelable(false)
                                            .setPositiveButton(R.string.forgot_login, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // if this button is clicked, close
                                                    // current activity
                                                    // ForgotActivity.this.finish();
                                                    //Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
                                                    progressDialog.dismiss();
                                                    //startActivity(intent);

                                                    Intent intent = new Intent(ForgotActivity.this.getApplicationContext(), ForgotActivity.class);
                                                    ForgotActivity.this.startActivity(intent);
                                                    ForgotActivity.this.finish();
                                                }
                                            })
                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {

                                                    progressDialog.dismiss();
                                                    // if this button is clicked, just close
                                                    // the dialog box and do nothing
                                                    dialog.cancel();
                                                }
                                            });

                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    // show it
                                    alertDialog.show();


                                } else {

                                    switch (e.getCode()) {

                                        case ParseException.TIMEOUT:
                                            Log.d("MyApp", "time out");

                                            Snackbar.make(mLayout_login, R.string.signup_request_t, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            }).setActionTextColor(Color.WHITE).show();

                                            progressDialog.dismiss();

                                            break;

                                        case ParseException.CONNECTION_FAILED:
                                            Log.d("MyApp", "connection failed");

                                            Snackbar.make(mLayout_login, R.string.login_cant, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            }).setActionTextColor(Color.WHITE).show();

                                            progressDialog.dismiss();

                                            break;
                                        case ParseException.EMAIL_NOT_FOUND:
                                            Log.d("MyApp", "email not found");

                                            progressDialog.dismiss();

                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                    ForgotActivity.this);
                                            alertDialogBuilder.setTitle(R.string.forgot_em_not);
                                            alertDialogBuilder
                                                    .setMessage(R.string.forgot_email_wasnt)
                                                    .setCancelable(false)
                                                    .setPositiveButton(R.string.forgot_register, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // if this button is clicked, close
                                                            // current activity
                                                            // LoginActivity.this.finish();
                                                            progressDialog.dismiss();

                                                            Intent intent = new Intent(ForgotActivity.this.getApplicationContext(), FormKitActivity.class);
                                                            ForgotActivity.this.startActivity(intent);
                                                            ForgotActivity.this.finish();
                                                        }
                                                    })
                                                    .setNegativeButton(R.string.login_verify, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int id) {

                                                            progressDialog.dismiss();
                                                            // if this button is clicked, just close
                                                            // the dialog box and do nothing
                                                            dialog.cancel();
                                                        }
                                                    });

                                            // create alert dialog
                                            AlertDialog alertDialog = alertDialogBuilder.create();

                                            // show it
                                            alertDialog.show();

                                            break;
                                    }
                                    // Something went wrong. Look at the ParseException to see what's up.

                                    // Dismiss Progress bar
                                    //progressDialog.dismiss();


                                }
                            }
                        }

                );

            } else {

                if (!validate2()) {

                    return;
                }

                showProgressBar(getString(R.string.forgot_veri2));

                final String username = mEditResent.getText().toString().toLowerCase();

                ParseQuery<User> query = User.getUserQuery();
                query.whereEqualTo(User.USER_NAME, username);
                query.getFirstInBackground(new GetCallback<User>() {
                    @Override
                    public void done(User user, ParseException e) {
                        if (e != null) {

                            progressDialog.dismiss();

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ForgotActivity.this);
                            alertDialogBuilder.setTitle(R.string.forgot_em_not2);
                            alertDialogBuilder
                                    .setMessage(R.string.forgot_email_wasnt2)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.forgot_register, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, close
                                            // current activity
                                            // LoginActivity.this.finish();
                                            progressDialog.dismiss();

                                            Intent intent = new Intent(ForgotActivity.this.getApplicationContext(), ForgotActivity.class);
                                            ForgotActivity.this.startActivity(intent);
                                            ForgotActivity.this.finish();
                                        }
                                    })
                                    .setNegativeButton(R.string.login_verify, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {

                                            progressDialog.dismiss();
                                            dialog.cancel();
                                        }
                                    });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();

                        } else {

                            progressDialog.dismiss();

                            // showProgressBar(getString(R.string.forgot_veri3));

                            final String actualEmail = user.getEmails();

                            ParseUser.requestPasswordResetInBackground(actualEmail, new RequestPasswordResetCallback() {
                                        @Override
                                        public void done(ParseException e1) {
                                            if (e1 == null) {


                                                progressDialog.dismiss();
                                                // progressDialog.dismiss();

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        ForgotActivity.this);
                                                alertDialogBuilder.setTitle(R.string.forgot_send + " " + actualEmail);
                                                alertDialogBuilder
                                                        .setMessage(R.string.forgot_check)
                                                        .setCancelable(false)
                                                        .setPositiveButton(R.string.forgot_login, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                // if this button is clicked, close
                                                                // current activity
                                                                // ForgotActivity.this.finish();
                                                                //Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
                                                                progressDialog.dismiss();
                                                                //startActivity(intent);

                                                                Intent intent = new Intent(ForgotActivity.this.getApplicationContext(), ForgotActivity.class);
                                                                ForgotActivity.this.startActivity(intent);
                                                                ForgotActivity.this.finish();
                                                            }
                                                        })
                                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                progressDialog.dismiss();
                                                                // if this button is clicked, just close
                                                                // the dialog box and do nothing
                                                                dialog.cancel();
                                                            }
                                                        });

                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();

                                                // show it
                                                alertDialog.show();


                                            } else {

                                                switch (e1.getCode()) {

                                                    case ParseException.TIMEOUT:
                                                        Log.d("MyApp", "time out");

                                                        Snackbar.make(mLayout_login, R.string.signup_request_t, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                            }
                                                        }).setActionTextColor(Color.WHITE).show();

                                                        progressDialog.dismiss();

                                                        break;

                                                    case ParseException.CONNECTION_FAILED:
                                                        Log.d("MyApp", "connection failed");

                                                        Snackbar.make(mLayout_login, R.string.login_cant, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                            }
                                                        }).setActionTextColor(Color.WHITE).show();

                                                        progressDialog.dismiss();

                                                        break;
                                                    case ParseException.EMAIL_NOT_FOUND:
                                                        Log.d("MyApp", "email not found");

                                                        progressDialog.dismiss();

                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                ForgotActivity.this);
                                                        alertDialogBuilder.setTitle(R.string.forgot_em_not);
                                                        alertDialogBuilder
                                                                .setMessage(R.string.forgot_email_wasnt)
                                                                .setCancelable(false)
                                                                .setPositiveButton(R.string.forgot_register, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        // if this button is clicked, close
                                                                        // current activity
                                                                        // LoginActivity.this.finish();
                                                                        progressDialog.dismiss();

                                                                        Intent intent = new Intent(getApplicationContext(), FormKitActivity.class);
                                                                        startActivity(intent);
                                                                        ForgotActivity.this.finish();
                                                                    }
                                                                })
                                                                .setNegativeButton(R.string.login_verify, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {

                                                                        progressDialog.dismiss();
                                                                        // if this button is clicked, just close
                                                                        // the dialog box and do nothing
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                        // create alert dialog
                                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                                        // show it
                                                        alertDialog.show();

                                                        break;
                                                }
                                            }
                                        }
                                    }
                            );
                        }
                    }
                });
            }

        } else {

            showInternetConnectionLostMessage();
        }
    }

    private boolean validate2() {

        //String email = mUsernameView.getText().toString().trim();

        if (mEditResent.getText().toString().isEmpty()) {
            mInptuResent.setError(getString(R.string.err_msg_user));
            requestFocus(mEditResent);
            return false;
        } else {
            mInptuResent.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {

        final String email = mEditResent.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mInptuResent.setError(getString(R.string.login_email_fail));
            requestFocus(mEditResent);
            return false;
        } else {
            mInptuResent.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showInternetConnectionLostMessage() {
        Snackbar.make(mLayout_login, R.string.login_no_int, Snackbar.LENGTH_SHORT).show();
    }

    public void showProgressBar(String message) {
        progressDialog = ProgressDialog.show(this, "", message, true);
    }

    public void dismissProgressBar() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
