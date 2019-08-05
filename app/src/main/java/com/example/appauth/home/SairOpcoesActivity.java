package com.example.appauth.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.auth.FormKitActivity;
import com.example.appauth.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class SairOpcoesActivity extends AppCompatActivity {

    private Button mSim,mNao;
    public User mCurrentUser;
    private TextView UserName;
    private CircleImageView userImage;
    private ProgressDialog progressDialog;
    private LinearLayout mLiniarSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sairopcoes);
        progressDialog = new ProgressDialog(this);

        mSim = findViewById(R.id.sim);
        mNao =  findViewById(R.id.nao);
        UserName = findViewById(R.id.userName);
        mLiniarSnack = findViewById(R.id.linearSnack);



        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Saindo da sua conta");
                progressDialog.setMessage("Aguarde por favor");
                progressDialog.show();

                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                           // Toast.makeText(getApplicationContext(), "voce saiu" +mCurrentUser.getUserFirstName()+mCurrentUser.getUserLastName(), Toast.LENGTH_SHORT).show();
                            Deslogar();

                        } else {

                            switch (e.getCode()) {

                                case ParseException.OTHER_CAUSE:
                                    Log.d("Invalido", "Login");

                                    Snackbar.make(mLiniarSnack, R.string.login_inc, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_verify, new View.OnClickListener() {
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

                            Toast.makeText(getApplicationContext(), "alguma coisa deu errado " +e, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Deslogar();
                        }
                    }
                });
            }
        });


    }


    private void Deslogar() {
        progressDialog.dismiss();
        Intent intent = new Intent(SairOpcoesActivity.this, FormKitActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
