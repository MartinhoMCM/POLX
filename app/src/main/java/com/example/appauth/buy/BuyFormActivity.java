package com.example.appauth.buy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appauth.R;
import com.example.appauth.chat.CustomAdapter;
import com.example.appauth.classes.CostumItems;
import com.example.appauth.models.Buy;
import com.example.appauth.models.User;
import com.github.ybq.android.spinkit.style.Circle;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BuyFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText mBuyerPnome, mBuyerSnome, mBuyerEndereco1, mBuyerEndereco2, mBuyerProvBairro, mBuyerEmail, mBuyerZipCode;
    private Spinner mProvincias;
    private ImageView mNextStep;
    private String mListProvncia = "null";
    private User mCurrentUser;
    private Buy buy;
    private ProgressBar progressBar;

    public static final String productName="name";
    public static final String productPrice="price";
    public static final String postId="postId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBuyerPnome = findViewById(R.id.buy_firstname);
        mBuyerSnome = findViewById(R.id.buy_segundo_nome);
        mBuyerProvBairro = findViewById(R.id.buy_municipio_bairros);
        mBuyerEndereco1 = findViewById(R.id.buy_endereco1);
        mBuyerEndereco2 = findViewById(R.id.buy_endereco2);
        mBuyerZipCode = findViewById(R.id.buy_zip_code);
        mBuyerEmail = findViewById(R.id.buy_email);
        mProvincias = findViewById(R.id.buy_provincias_list);
        progressBar=findViewById(R.id.spin_kit);

        Circle wave =new Circle();
        progressBar.setIndeterminateDrawable(wave);

        mCurrentUser =(User) ParseUser.getCurrentUser();

        if(mCurrentUser!=null) {
            mBuyerPnome.setFocusable(false);
            mBuyerPnome.setText(mCurrentUser.getUserFirstName());
            mBuyerSnome.setFocusable(false);
            mBuyerSnome.setText(mCurrentUser.getUserLastName());
            mBuyerEmail.setFocusable(false);
            mBuyerEmail.setText(mCurrentUser.getEmails());

        }
        //stap buttom
        mNextStep = findViewById(R.id.buy_next_step);
        mNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( isValidTheInsertedInfo()){

                  progressBar.setVisibility(View.INVISIBLE);
                  Intent intent = new Intent(BuyFormActivity.this, BuyConfirmActivity.class).
                          putExtra(BuyConfirmActivity.productPrice, getIntent().getStringExtra(productPrice)).
                          putExtra(BuyConfirmActivity.productName, getIntent().getStringExtra(productName)).
                          putExtra(BuyConfirmActivity.province, mListProvncia).
                          putExtra(BuyConfirmActivity.provinceStreet,  mBuyerProvBairro.getText().toString()).
                          putExtra( BuyConfirmActivity.address1, mBuyerEndereco1.getText().toString()).
                          putExtra(BuyConfirmActivity.address2, mBuyerEndereco2.getText().toString()).
                          putExtra(BuyConfirmActivity.zipCode, mBuyerZipCode.getText().toString())
                          .putExtra(BuyConfirmActivity.postId, getIntent().getStringExtra(postId));
                               startActivity(intent);
               }
            }
        });

          SpinnerCategoryMethod();
    }

    private boolean isValidTheInsertedInfo() {

        AtomicBoolean status= new AtomicBoolean(true);

        if(mListProvncia.trim().isEmpty() || mListProvncia.equals("Selecione sua provincia"))
        {
            status.set(false);
            Toast.makeText(getApplicationContext(), "Nenhuma província selecionada", Toast.LENGTH_LONG).show();
        }
        if(mBuyerProvBairro.getText().toString().trim().isEmpty())
        {
            status.set(false);
            mBuyerProvBairro.setError("");
        }
        if(mBuyerEndereco1.getText().toString().trim().isEmpty())
        {
            status.set(false);
            mBuyerEndereco1.setError("");
        }

        if(mBuyerZipCode.getText().toString().trim().isEmpty())
        {
            status.set(false);
            mBuyerZipCode.setError("");
        }
        return status.get();
    }

    private void SpinnerCategoryMethod() {

        ArrayList<CostumItems> costumItems = new ArrayList<>();

        for (int i =0; i<=17; i++)
        {
            if(i==0)
                costumItems.add(new CostumItems("Selecione sua provincia"));
            if(i==1)
                costumItems.add(new CostumItems("Luanda"));
            if(i==2)
                costumItems.add(new CostumItems("Uíge"));
            if(i==3)
                costumItems.add(new CostumItems("Malange"));
            if(i==4)
                costumItems.add(new CostumItems("Bengo"));
            if(i==5)
                costumItems.add(new CostumItems("Muxico"));

            if(i==6)
                costumItems.add(new CostumItems("Zaíre"));
            if(i==7)
                costumItems.add(new CostumItems("Lunda Norte"));

            if(i==8)
                costumItems.add(new CostumItems("Lunda Sul"));

            if(i==9)
                costumItems.add(new CostumItems("Kwuanza Norte"));

            if(i==10)
                costumItems.add(new CostumItems("Kwuanza Sul"));

            if(i==11)
                costumItems.add(new CostumItems("Namibe"));

            if(i==12)
                costumItems.add(new CostumItems("Cunene"));

            if(i==13)
                costumItems.add(new CostumItems("Cabinda"));

            if(i==14)
                costumItems.add(new CostumItems("Huíla"));

            if(i==15)
                costumItems.add(new CostumItems("Kwuando Kubango"));

            if(i==16)
                costumItems.add(new CostumItems("Huambo"));

            if(i==17)
                costumItems.add(new CostumItems("Bié"));
            if(i==18)
                costumItems.add(new CostumItems("Bengela"));

        }

        CustomAdapter customAdapter = new CustomAdapter(this, costumItems);
        if(mProvincias!=null)
        {
            mProvincias.setAdapter(customAdapter);
            mProvincias.setOnItemSelectedListener(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        CostumItems items = (CostumItems)parent.getSelectedItem();
        mListProvncia = items.getSpinnerText();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {



    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void showInternetConnectionLostMessage(){
        Toast.makeText(getApplicationContext(), "Sem conexao a internet", Toast.LENGTH_SHORT).show();

    }


}
