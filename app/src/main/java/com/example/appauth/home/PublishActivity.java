package com.example.appauth.home;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appauth.adapter.GalleryAdapter;
import com.example.appauth.chat.CustomAdapter;
import com.example.appauth.classes.CostumItems;
import com.example.appauth.R;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PublishActivity extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText mTitle, mDescription, mLocalization, mEmailPost,mPrice;
    private Button mButtonPublish;
    private GridView gvGallery;
    private User mUser;
    private Posts posts;
    private ImageButton mButtonGallery;
    private GalleryAdapter galleryAdapter;
    private String imageEncoded;
    private String[] filePathColumn;
    private int pick_image_multiple;
    private ParseFile postimage;
    private ArrayList<Uri> mArrayUri;
    private ProgressBar progressDialog;

    private byte[] inpuData;
    private ArrayList<ParseFile> parseFiles;
    private boolean  mVerificarQtdPhotos=false;
    private boolean bool=true;
    private Spinner spinnerCategory;
    private String Category="null";
    private TextView TextViewError;
    private boolean controlPost=false;
    String imageURI;

    ArrayList<String> encodedImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = findViewById(R.id.toolbarPost);
        setSupportActionBar(toolbar);
        progressDialog =findViewById(R.id.spin_kit);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Publicar anúncio");

        Circle wave =new Circle();
        progressDialog.setIndeterminateDrawable(wave);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        mArrayUri =new ArrayList<>();
        mTitle = findViewById(R.id.titlePost);
        mDescription=findViewById(R.id.descriptionPost);
        mLocalization=findViewById(R.id.localizationPost);
        mEmailPost=findViewById(R.id.emailPost);
        mButtonPublish =findViewById(R.id.IdPublish);;
        mButtonGallery =findViewById(R.id.buttonGallery);
        spinnerCategory =findViewById(R.id.categoryPost);
        mPrice = findViewById(R.id.pricePost);
        gvGallery=findViewById(R.id.gvGallery);
        mUser =(User) ParseUser.getCurrentUser();
        posts = new Posts();

        TextViewError = (TextView) spinnerCategory.getSelectedView();

        mButtonGallery.setOnClickListener(this);
        mButtonPublish.setOnClickListener(this);

        if(mUser!=null) {
            mEmailPost.setText(mUser.getEmails());
            mEmailPost.setKeyListener(null);
        }

        gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mArrayUri.clear();
                 UploadPhotoGallery();
            }
        });

        SpinnerCategoryMethod();

    }

    private void SpinnerCategoryMethod() {

        ArrayList<CostumItems> costumItems = new ArrayList<>();

        for (int i =0; i<=7; i++)
        {
            if(i==0)
                costumItems.add(new CostumItems("Selecione"));
            if(i==1)
                costumItems.add(new CostumItems("Tecnologia"));
            if(i==2)
                costumItems.add(new CostumItems("Automóvel"));
            if(i==3)
                costumItems.add(new CostumItems("Cosmético"));
            if(i==4)
                costumItems.add(new CostumItems("Casa ou Móveis"));
            if(i==5)
                costumItems.add(new CostumItems("Jogos"));

            if(i==6)
                costumItems.add(new CostumItems("Roupa"));
            if(i==7)
                costumItems.add(new CostumItems("Serviço"));

        }

        CustomAdapter customAdapter = new CustomAdapter(this, costumItems);
        if(spinnerCategory!=null)
        {
            spinnerCategory.setAdapter(customAdapter);
            spinnerCategory.setOnItemSelectedListener(this);
        }
    }
    @Override
    public void onClick(View v) {

        int id =v.getId();

        if(id ==R.id.buttonGallery) {

            UploadPhotoGallery();
        }
        else
        if(id==R.id.IdPublish){

            if(mTitle.getText().toString().trim().isEmpty())
            {
                mTitle.setError(getString(R.string.empty));
                return;
            }
            if(Category.trim().isEmpty() || Category.equals("Selecione"))
            {
                Toast.makeText(getApplicationContext(), "Selecione uma das categorias", Toast.LENGTH_SHORT).show();
                return;
            }
            if(mDescription.getText().toString().trim().isEmpty())
            {
                mDescription.setError(getString(R.string.empty));
                return;
            }
            if(mLocalization.getText().toString().trim().isEmpty())
            {
                mLocalization.setError(getString(R.string.empty));
                return;
            }
            if(mPrice.getText().toString().trim().isEmpty())
            {
                mPrice.setError(getString(R.string.empty));
                return;
            }

            if(!mTitle.getText().toString().trim().isEmpty() && (!Category.trim().isEmpty() || !Category.equals("Selecione"))
                    && !mDescription.getText().toString().trim().isEmpty() && !mLocalization.getText().toString().trim().isEmpty()&& !mPrice.getText().toString().trim().isEmpty())
            {

                if(isInternetAvailable())
                {
                    //showProgressBar("Publicando ...");
                    posts.setTitle(mTitle.getText().toString());
                    posts.setCategory(Category);
                    posts.setDescription(mDescription.getText().toString());
                    posts.setLocalization(mLocalization.getText().toString());
                    posts.setPrice(mPrice.getText().toString());
                    posts.setAuthor(mUser);

                    if(postimage!=null)
                    {
                            posts.setImage(postimage);
                            parseFiles = new ArrayList<>();
                            parseFiles.add(postimage);
                            posts.setImageList(parseFiles);

                    }
                    else
                    if(parseFiles!=null && parseFiles.size()>1) {
                        posts.setImage(parseFiles.get(0));
                        posts.setImageList(parseFiles);
                    }
                    if(parseFiles!=null || posts!=null)
                    {
                        progressDialog.setVisibility(View.VISIBLE);
                        posts.saveInBackground(new SaveCallback() {
                                                   @Override
                                                   public void done(ParseException e) {

                                                       if(e!=null)
                                                       {
                                                           Toast.makeText(getApplicationContext(), "Nao foi enviado, deve ter algum problema", Toast.LENGTH_SHORT).show();
                                                           progressDialog.setVisibility(View.INVISIBLE);
                                                           bool=false;

                                                       }
                                                       else {

                                                           Toast.makeText(getApplicationContext(), "Enviado, com sucesso", Toast.LENGTH_SHORT).show();
                                                           progressDialog.setVisibility(View.GONE);
                                                           startActivity(new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                       }

                                                   }
                                               }
                        );
                    }
                    else
                    {
                        View mView =findViewById(R.id.LinearViewSnack) ;
                        Snackbar.make(mView, "Sem Imagem", 30).show();
                    }

                }
                else
                {
                    showInternetConnectionLostMessage();
                }
            }
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void UploadPhotoGallery() {

        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGallery.setType("image/*");
        intentGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intentGallery.setAction(Intent.ACTION_GET_CONTENT);
        pick_image_multiple = 1;
        startActivityForResult(Intent.createChooser(intentGallery, "Selecione as imagens"), pick_image_multiple);
        mButtonGallery.setVisibility(View.INVISIBLE);
        gvGallery.setVisibility(View.VISIBLE);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {
            if (requestCode == pick_image_multiple && resultCode == Activity.RESULT_OK && data != null) {

                filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                encodedImageList = new ArrayList<>();

                if (data.getData() != null) {

                    Uri mImageUri = data.getData();
                    mArrayUri.add(mImageUri);

                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageURI  = cursor.getString(columnIndex);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                    encodedImageList.add(encodedImage);

                    inpuData =byteArrayOutputStream.toByteArray();
                    postimage = new ParseFile("postImage.jpg", inpuData, "jpeg");
                    controlPost=true;

                    gvGallery.setGravity(Gravity.CENTER);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(100, gvGallery.getHorizontalSpacing(), 100, 0);


                    cursor.close();


                }
                else
                if (data.getClipData() != null) {

                    parseFiles = new ArrayList<>();

                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageURI  = cursor.getString(columnIndex);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                        encodedImageList.add(encodedImage);
                        cursor.close();

                        galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                        gvGallery.setAdapter(galleryAdapter);
                        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                .getLayoutParams();
                        mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        inpuData =byteArrayOutputStream.toByteArray();
                        parseFiles.add(new ParseFile("postImages.jpg", inpuData, "jpeg"));
                        controlPost=true;

                    }

                    Log.v("LOG_TAG", "Imagens selecionadas" + mArrayUri.size());
                }
                else {
                    Toast.makeText(this, "Não selecionaste imagens",
                            Toast.LENGTH_LONG).show();
                }
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this, "Alguma coisa deu errado", Toast.LENGTH_LONG)
                    .show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void showInternetConnectionLostMessage(){
        Toast.makeText(getApplicationContext(), "Sem conexao a internet", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {


        super.onResume();
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        CostumItems items = (CostumItems)parent.getSelectedItem();
        Category =items.getSpinnerText();
        // Toast.makeText(this, items.getSpinnerText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}