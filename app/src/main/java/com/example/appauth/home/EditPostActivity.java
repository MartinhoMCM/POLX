package com.example.appauth.home;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appauth.R;
import com.example.appauth.chat.CustomAdapter;
import com.example.appauth.classes.CostumItems;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.snackbar.Snackbar;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditPostActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText mTitle, mDescription, mLocalization, mEmailPost, mPrice;
    private Button mButtonPublish;
    private GridView gvGallery;
    private User mCurrentUser;
    private Posts posts;
    private ImageButton mButtonGallery;
    private GalleryAdapter galleryAdapter;
    private String imageEncoded;
    private String[] filePathColumn;
    private int pick_image_multiple;
    private ParseFile postimage;
    private ArrayList<Uri> mArrayUri;
    private List<String> imagesEncodedList;
    private ProgressBar progressDialog;
    private ImageView feedImage;
    private InputStream stream;
    private byte[] inpuData;
    private ArrayList<ParseFile> parseFiles;
    private boolean mVerificarQtdPhotos = false;
    private boolean bool = true;
    private Spinner spinnerCategory;
    private String Category = "null";
    private TextView TextViewError;
    public static String ObjectId = "objectId";
    public String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        //initialize
        mCurrentUser = (User)User.getCurrentUser();
        posts = new Posts();

        Toolbar toolbar = findViewById(R.id.edit_toolbarPost);
        setSupportActionBar(toolbar);
        progressDialog = findViewById(R.id.edit_spin_kit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Editar meu anúncio");

        Circle wave = new Circle();
        progressDialog.setIndeterminateDrawable(wave);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyAnuncioActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        //edit input
        mTitle = findViewById(R.id.edit_titlePost);
        mDescription = findViewById(R.id.edit_descriptionPost);
        mLocalization = findViewById(R.id.edit_localizationPost);
        mEmailPost = findViewById(R.id.edit_emailPost);
        spinnerCategory = findViewById(R.id.edit_categoryPost);
        mPrice = findViewById(R.id.edit_pricePost);

        //button
        TextViewError = (TextView) spinnerCategory.getSelectedView();
        mButtonPublish = findViewById(R.id.edit_IdPublish);
        mButtonGallery = findViewById(R.id.buttonGallery);

        mButtonPublish.setOnClickListener(this);
        mButtonGallery.setOnClickListener(this);

        //img
        gvGallery = findViewById(R.id.gvGallery);

        //
        if (mCurrentUser != null) {
            mEmailPost.setText(mCurrentUser.getEmails());

        }
         Intent intent = getIntent();
        objectId = intent.getExtras().getString(ObjectId);

        ParseQuery<Posts> query = Posts.getQuery();
        query.whereEqualTo(Posts.COL_AUTHOR, mCurrentUser);

        query.whereExists(Posts.COL_TITLE);
        query.whereExists(Posts.COL_DESCRIPTION);
        query.whereExists(Posts.COL_PRICE);
        query.whereExists(Posts.CATEGORY);
        query.whereExists(Posts.COL_IMAGE);
        query.whereExists(Posts.COL_LOCALIZATION);

        query.getInBackground(objectId, new GetCallback<Posts>() {
            @Override
            public void done(Posts object, ParseException e) {

                if (object != null){

                    mTitle.setText(object.getTitle());
                    mDescription.setText(object.getDescription());
                    mPrice.setText(object.getPrice());
                    mLocalization.setText(object.getLocalization());

                }

            }
        });


        gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UploadPhotoGallery();


            }
        });

        SpinnerCategoryMethod();

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.buttonGallery) {

            UploadPhotoGallery();

        } else if (v == mButtonPublish) {

            if (mTitle.getText().toString().trim().isEmpty()) {
                mTitle.setError(getString(R.string.empty));
                return;
            }
            if (Category.trim().isEmpty() || Category.equals("")) {
                Toast.makeText(getApplicationContext(), "Selecione uma das categorias", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mDescription.getText().toString().trim().isEmpty()) {
                mDescription.setError(getString(R.string.empty));
                return;
            }
            if (mLocalization.getText().toString().trim().isEmpty()) {
                mLocalization.setError(getString(R.string.empty));
                return;
            }
            if (mPrice.getText().toString().trim().isEmpty()) {
                mPrice.setError(getString(R.string.empty));
                return;
            }

            if (!mTitle.getText().toString().trim().isEmpty() && (!Category.trim().isEmpty() || !Category.equals(posts.getCATEGORY()))
                    && !mDescription.getText().toString().trim().isEmpty() && !mLocalization.getText().toString().trim().isEmpty() && !mPrice.getText().toString().trim().isEmpty()) {
                if(!mVerificarQtdPhotos)
                {
                    //  posts.setImageArray(parseFiles);
                }
                if (isInternetAvailable()) {

                    progressDialog.setVisibility(View.VISIBLE);

                    ParseQuery<Posts> query = Posts.getQuery();
                    query.getInBackground(objectId, new GetCallback<Posts>() {
                        @Override
                        public void done(Posts update, ParseException e) {

                            update.setTitle(mTitle.getText().toString());
                            update.setCategory(Category);
                            update.setDescription(mDescription.getText().toString());
                            update.setLocalization(mLocalization.getText().toString());
                            update.setPrice(mPrice.getText().toString().trim());
                            update.setAuthor(mCurrentUser);
                            if(postimage != null) {
                                update.setImage(postimage);
                            }


                            update.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e == null){

                                        Toast.makeText(getApplicationContext(), "Atualizado, com sucesso", Toast.LENGTH_SHORT).show();
                                        progressDialog.setVisibility(View.GONE);
                                        startActivity(new Intent(getApplicationContext(), MyAnuncioActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                                    }else {
                                        Toast.makeText(getApplicationContext(), "Nao salvou"+e, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                    } else {
                        View mView = findViewById(R.id.LinearViewSnack);
                        Snackbar.make(mView, "Sem net", 30).show();
                    }

                } else {
                    showInternetConnectionLostMessage();
                }
            }

    }

    private void SpinnerCategoryMethod() {

        Intent intent = getIntent();
        objectId = intent.getExtras().getString(ObjectId);

        ParseQuery<Posts> query = Posts.getQuery();
        query.whereEqualTo(Posts.COL_AUTHOR, mCurrentUser);
        query.whereExists(Posts.CATEGORY);
        query.getInBackground(objectId, new GetCallback<Posts>() {
            @Override
            public void done(Posts object, ParseException e) {
                if (object != null) {

                    ArrayList<CostumItems> costumItems = new ArrayList<>();

                    for (int i = 0; i < 11; i++) {
                        if (i == 0)
                            costumItems.add(new CostumItems(object.getCATEGORY()));
                        if (i == 1)
                            costumItems.add(new CostumItems("Telemóvel"));
                        if (i == 2)
                            costumItems.add(new CostumItems("Automóvel"));
                        if (i == 3)
                            costumItems.add(new CostumItems("Cosmético"));
                        if (i == 4)
                            costumItems.add(new CostumItems("Aparelhos Domésticos"));
                        if (i == 5)
                            costumItems.add(new CostumItems("Material Escolares"));
                        if (i == 6)
                            costumItems.add(new CostumItems("Tecnólogia"));
                        if (i == 7)
                            costumItems.add(new CostumItems("Mobilhas"));
                        if (i == 8)
                            costumItems.add(new CostumItems("Roupas"));
                        if (i == 9)
                            costumItems.add(new CostumItems("Jogos"));

                    }

                    CustomAdapter customAdapter = new CustomAdapter(EditPostActivity.this, costumItems);
                    if (spinnerCategory != null) {
                        spinnerCategory.setAdapter(customAdapter);
                        spinnerCategory.setOnItemSelectedListener(EditPostActivity.this);
                    }
                }
            }
        });


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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {
            if (requestCode == pick_image_multiple && resultCode == Activity.RESULT_OK && data != null) {

                filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<>();

                if (data.getData() != null) {

                    mVerificarQtdPhotos = true;
                    Uri mImageUri = data.getData();

                    Cursor cursor = getContentResolver().
                            query(mImageUri, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columIndex);
                    cursor.close();

                    mArrayUri = new ArrayList<>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    marginLayoutParams.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                    stream = getContentResolver().openInputStream(mImageUri);
                    inpuData = getBytes(stream);

                    postimage = new ParseFile("postImage", inpuData);
                } else if (data.getClipData() != null) {

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
                        assert cursor != null;
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        imagesEncodedList.add(imageEncoded);
                        cursor.close();

                        galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                        gvGallery.setAdapter(galleryAdapter);
                        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                .getLayoutParams();
                        mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        ///
                        stream = getContentResolver().openInputStream(mArrayUri.get(i));
                        inpuData = getBytes(stream);

                        parseFiles.add(new ParseFile("postimage", inpuData));

                    }
                    Log.v("LOG_TAG", "Imagens selecionadas" + mArrayUri.size());
                } else {
                    Toast.makeText(this, "Não selecionaste imagens",
                            Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Alguma coisa deu errado", Toast.LENGTH_LONG)
                    .show();

        }
        super.onActivityResult(requestCode, resultCode, data);
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

        CostumItems items = (CostumItems) parent.getSelectedItem();
        Category = items.getSpinnerText();
        // Toast.makeText(this, items.getSpinnerText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void showInternetConnectionLostMessage() {
        Toast.makeText(getApplicationContext(), "Sem conexao a internet", Toast.LENGTH_SHORT).show();

    }

    private boolean isInternetAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
