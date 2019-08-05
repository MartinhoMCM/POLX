package com.example.appauth.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.home.HomeActivity;
import com.example.appauth.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EditarPerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CLoneOlx";
    private static final int FOTO_GALARIA = 1;
    private User mCurrentUser;
    private Intent mLoginIntent;
    private ParseFile mUserImage;
    private boolean mVerificarURLPhotos =false;

    private TextInputLayout mEditInputFirstName,mEditInputLasteName,mEditInputEmail,mEditInputUsername,mEditInputTelemovel;
    private EditText mEditFirstName,mEditLasteName,mEditEmail,mEditUsername,mEditTelemovel;
    private CircleImageView mProfilePhoto;
    private ProgressDialog dialog1;
    private NestedScrollView mNestedSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        Toolbar toolbar = findViewById(R.id.toolbarPost);
        setSupportActionBar(toolbar);

        mCurrentUser = (User)User.getCurrentUser();
        mLoginIntent = getIntent();

        dialog1 = new ProgressDialog(EditarPerfilActivity.this);
        Button mEditbutton = (Button) findViewById(R.id.edit_button);
        mNestedSnackbar = (NestedScrollView)findViewById(R.id.id_nested_snackbar);


        mEditInputFirstName = (TextInputLayout) findViewById(R.id.edit_layout_input_first_name);
        mEditInputLasteName = (TextInputLayout) findViewById(R.id.edit_layout_input_last_name);
        mEditInputEmail = (TextInputLayout) findViewById(R.id.edit_layout_input_email);
        mEditInputUsername = (TextInputLayout) findViewById(R.id.edit_layout_input_username);
        mEditInputTelemovel = (TextInputLayout) findViewById(R.id.edit_layout_input_telefone);


        mEditFirstName = (EditText) findViewById(R.id.edit_first_name);
        mEditLasteName = (EditText) findViewById(R.id.edit_last_name);
        mEditEmail = (EditText) findViewById(R.id.edit_email);
        mEditUsername = (EditText) findViewById(R.id.edit_username);
        mEditTelemovel = (EditText)  findViewById(R.id.edit_telefone);

        mProfilePhoto = (CircleImageView) findViewById(R.id.user_profile_photo);
        mProfilePhoto.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        // User profile info
        if(mCurrentUser != null) {

            mEditFirstName.setText(mCurrentUser.getUserFirstName().trim());
            mEditLasteName.setText(mCurrentUser.getUserLastName().trim());
            mEditUsername.setText(mCurrentUser.getUsername().trim());
            mEditEmail.setText(mCurrentUser.getEmail());
            mEditTelemovel.setText(mCurrentUser.getNumber());

            //Verficado no servidor a imag do usuario

            if (mCurrentUser.getPhotoUrl().isEmpty()) {
                mProfilePhoto.setImageResource(R.drawable.placeholder);
            } else {

                Glide.with(this)
                        .load(mCurrentUser.getPhotoUrl())
                        .error(R.drawable.placeholder)
                        .fitCenter()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(mProfilePhoto);

            }
        }

        mEditbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Editar Perfil");

                if (!validateFirstName()) {
                    return;
                }
                if (!validateLastName()) {
                    return;
                }

                if (!validateUserName()) {
                    return;
                }

                if (!validateEmail()) {
                    return;
                }
                //  posts.setImageArray(parseFiles);
                 if(mUserImage != null) {

                   }
                    if (isInternetAvailable()) {

                        //showProgressBar("Updating Profile...");

                        dialog1.setTitle("Please wait");
                        dialog1.setMessage("Atualizando");
                        dialog1.setCancelable(true);
                        dialog1.show();

                        mCurrentUser.setUserFirstName(mEditFirstName.getText().toString());
                        mCurrentUser.setUserLastName(mEditLasteName.getText().toString());
                        mCurrentUser.setUsername(mEditUsername.getText().toString().toLowerCase().trim());
                        mCurrentUser.setNumber(mEditTelemovel.getText().toString());
                        mCurrentUser.setEmail(mEditEmail.getText().toString());

                        if (mUserImage != null) {
                            mCurrentUser.setProfilePhoto(mUserImage);
                        }
                        mCurrentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {

                                    dialog1.dismiss();

                                    switch (e.getCode()) {

                                        case ParseException.TIMEOUT:
                                            Log.d("CloneOLX", "tempo acabou");

                                            Snackbar.make(mNestedSnackbar, R.string.signup_request_t, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v1) {

                                                }
                                            }).setActionTextColor(Color.WHITE).show();

                                            dialog1.dismiss();

                                            break;

                                        case ParseException.CONNECTION_FAILED:
                                            Log.d("CloneOLX", "conexao falhou");

                                            Snackbar.make(mNestedSnackbar, R.string.login_cant, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v1) {

                                                }
                                            }).setActionTextColor(Color.WHITE).show();

                                            dialog1.dismiss();

                                            break;

                                        case ParseException.VALIDATION_ERROR:
                                            Log.d("CloneOLX", "internal server error");

                                            Snackbar.make(mNestedSnackbar, R.string.password_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v1) {

                                                }
                                            }).setActionTextColor(Color.WHITE).show();

                                            dialog1.dismiss();

                                            break;

                                        case ParseException.EMAIL_TAKEN:
                                            Log.d("CloneOLX", "email taken");

                                            Snackbar.make(mNestedSnackbar, R.string.login_teken, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v1) {

                                                }
                                            }).setActionTextColor(Color.WHITE).show();

                                            dialog1.dismiss();

                                            break;
                                        case ParseException.USERNAME_TAKEN:
                                            Log.d("CloneOLX", "username taken");

                                            Snackbar.make(mNestedSnackbar, R.string.signup_userta, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v1) {

                                                }
                                            }).setActionTextColor(Color.rgb(255, 255, 168)).show();

                                            dialog1.dismiss();

                                            break;

                                        case ParseException.OTHER_CAUSE:
                                            Log.d("CloneOLX", "tempo acabou");

                                            Snackbar.make(mNestedSnackbar, R.string.signup_request, Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v1) {

                                                }
                                            }).setActionTextColor(Color.WHITE).show();

                                            break;
                                    }

                                }
                                else {
                                    dialog1.dismiss();
                                    finish();

                                }
                                }
                        });
                         }

                    else {
                        showInternetConnectionLostMessage();
                    }
                }

        });

    }


    @SuppressLint("IntentReset")
    @Override
    public void onClick(View v) {
        @SuppressLint("IntentReset") Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentGallery, "Selecione as imagens"), FOTO_GALARIA);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //Validando

    private boolean validateFirstName() {
        if (mEditFirstName.getText().toString().isEmpty() || mEditFirstName.length() < 3) {
            mEditInputFirstName.setError(getString(R.string.signup_name_at));
            requestFocus(mEditFirstName);
            return false;
        } else {
            mEditInputFirstName.setErrorEnabled(false);
        }

        return true;

    }

    private boolean validateLastName() {
        if (mEditLasteName.getText().toString().isEmpty() || mEditLasteName.length() < 3) {
            mEditInputLasteName.setError(getString(R.string.signup_name_at));
            requestFocus(mEditLasteName);
            return false;
        } else {
            mEditInputLasteName.setErrorEnabled(false);
        }
        return true;

    }

    private boolean validateUserName() {
        if (mEditUsername.getText().toString().isEmpty() || mEditUsername.length() < 3) {
            mEditInputUsername.setError(getString(R.string.signup_name_at));
            requestFocus(mEditUsername);
            return false;
        } else {
            mEditInputUsername.setErrorEnabled(false);
        }
        return true;

    }

    private boolean validateEmail() {
        String email = mEditEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mEditInputEmail.setError(getString(R.string.err_msg_email));
            requestFocus(mEditEmail);
            return false;
        } else {
            mEditInputEmail.setErrorEnabled(false);
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

    private boolean isInternetAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showInternetConnectionLostMessage() {
        Snackbar.make(mNestedSnackbar, R.string.login_no_int, Snackbar.LENGTH_SHORT).show();
    }

    //corte de imagem
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FOTO_GALARIA && resultCode == RESULT_OK){

            Uri imageUri= data.getData();
            assert imageUri != null;

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mVerificarURLPhotos =true;

                Uri resultUri = result.getUri();
                File file = new File(resultUri.getPath());

                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(file);
                } catch
                (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                assert thumb_bitmap != null;
                thumb_bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                if(result.getUri() != null) {
                    Glide.with(this)
                            .load(thumb_byte)
                            .error(R.drawable.placeholder)
                            .centerCrop()
                            .fitCenter()
                            .placeholder(R.drawable.placeholder)
                            .into(mProfilePhoto);
                }

                mUserImage = new ParseFile("postImage",thumb_byte);
                mCurrentUser.setProfilePhoto(mUserImage);

              } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                dialog1.dismiss();
                Toast.makeText(this,"Alguma coisa foi mal"+error,Toast.LENGTH_LONG).show();
            }

        }
    }

}
