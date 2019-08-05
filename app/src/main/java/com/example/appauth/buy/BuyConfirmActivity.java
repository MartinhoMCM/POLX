package com.example.appauth.buy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appauth.R;
import com.example.appauth.home.HomeActivity;
import com.example.appauth.home.HomePageFragement;
import com.example.appauth.models.Buy;
import com.example.appauth.models.User;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.vicmikhailau.maskededittext.MaskedFormatter;
import com.vicmikhailau.maskededittext.MaskedWatcher;

public class BuyConfirmActivity extends AppCompatActivity {

    public static String productPrice= "mProductPrice";
    public static String productName= "mProductName";
    public static String province ="mProvince";
    public static String provinceStreet="provinceStreet";
    public static String address1="address1";
    public static String address2="address2";
    public static String zipCode="zipCode";
    public static final String postId="postId";

    private TextView mProductName;
    private TextView mProductPrice;

    public static final String stringBuyId="mBuyId";

    Dialog myDialog;
    private ProgressBar progressBar;
    private EditText ticketPass;
    private User mCurrentUser;
    private String post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_confirm);

        mProductName =findViewById(R.id.mProductName);
        mProductPrice=findViewById(R.id.mProductPrice);


        mCurrentUser =(User) ParseUser.getCurrentUser();

        Button mComfirmComprovativo = findViewById(R.id.sim_confirmo);
        Button mCancelar = findViewById(R.id.nao_confirmo);

        myDialog = new Dialog(this);
        String BuyObjectId = getIntent().getStringExtra(stringBuyId);

        SetDataFromPost();

        mComfirmComprovativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup();
            }
        });

    }

    private void SetDataFromPost() {
        mProductName.setText(getIntent().getStringExtra(BuyConfirmActivity.productName));
        mProductPrice.setText(getIntent().getStringExtra(BuyConfirmActivity.productPrice));
    }


    private void ShowPopup() {

        myDialog.setContentView(R.layout.popup);
        myDialog.show();

        MaskedFormatter formatter = new MaskedFormatter("***-***-***");

        progressBar = myDialog.findViewById(R.id.spin_kit);
        ticketPass = myDialog.findViewById(R.id.tickedPass);

        ticketPass.addTextChangedListener(new MaskedWatcher(formatter, ticketPass));
        Button button = myDialog.findViewById(R.id.IdPublish);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ticketPass.getText().toString().trim().isEmpty())
                {
                    ticketPass.setError("!");

                }
                else {
                    FadingCircle fadingCircle = new FadingCircle();
                    progressBar.setIndeterminateDrawable(fadingCircle);
                    Buy buy =new Buy();
                    buy.setBuyerID(mCurrentUser.getObjectId());
                    buy.setBuyerFirstName(mCurrentUser.getUserFirstName());
                    buy.setBuyerLastName(mCurrentUser.getUserLastName());
                    buy.setBuyerProvince(getIntent().getStringExtra(province));
                    buy.setBuyerCountry("Angola");
                    buy.setBuyerAddress1(getIntent().getStringExtra(address1));
                    buy.setBuyerAddress2(getIntent().getStringExtra(address2));
                    buy.setBuyerZipCode(getIntent().getStringExtra(zipCode));
                    buy.setBuyerEmail(mCurrentUser.getEmail());
                    buy.setProductName(getIntent().getStringExtra(BuyConfirmActivity.productName));
                    buy.setProductPrice(getIntent().getStringExtra(BuyConfirmActivity.productPrice));
                    buy.setTicket(ticketPass.getText().toString());
                    buy.setPostId(getIntent().getStringExtra(BuyConfirmActivity.postId));

                    buy.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                            {
                                myDialog.dismiss();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class).
                                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_LONG).show();
                                myDialog.dismiss();

                            }}
                    });

                }



            }
        });


    }

}
