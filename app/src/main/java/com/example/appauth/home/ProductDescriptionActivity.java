package com.example.appauth.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.adapter.SlideAdapter;
import com.example.appauth.buy.BuyConfirmActivity;
import com.example.appauth.buy.BuyFormActivity;
import com.example.appauth.chat.ChatActivity;
import com.example.appauth.models.Favority;
import com.example.appauth.models.Model;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProductDescriptionActivity extends AppCompatActivity {

    public static String ObjectId = "objectId";
    Dialog myDialog;

    private TextView mProdutoTitle;
    private TextView mProdutoDescription;
    private TextView mProdutoLocation;
    private TextView mProdutoPreco;
    private TextView mProdutoUserNames;
    private TextView mUserNumerCall,mTextInfo;
    private ImageView mFavorityUserPost;
    private CircleImageView mProdutoUserAvatar;
    private TextView mBuyForm;


    private ImageView mProdutoImage;
    Posts posts;
    private User mCurrentUser;

    private ViewPager mSlideViewPager;
    private SlideAdapter slideAdapter;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private ArrayList<ParseFile> parseFileArrayList;
    private ShimmerFrameLayout shimmer,shimmerLayout;
    private RelativeLayout mLayoutShimmir,mLayoutInformation;
    private FloatingActionsMenu floatingActionsMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);


        myDialog = new Dialog(this);

        final  FloatingActionButton fab1 = findViewById(R.id.actionfab_chamada);
        final FloatingActionButton fabMessage = findViewById(R.id.actionfab_message);
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.fab);

        mProdutoTitle = (TextView) findViewById(R.id.produto_title);
        mProdutoDescription = (TextView) findViewById(R.id.produto_description);
        mProdutoLocation = (TextView) findViewById(R.id.produto_localization);
        mProdutoPreco = (TextView) findViewById(R.id.produto_preco);
        mProdutoUserNames = (TextView) findViewById(R.id.produto_user_names);
        mTextInfo = (TextView) findViewById(R.id.textinfo);
        mBuyForm = (TextView) findViewById(R.id.buyForm);

        ImageView mBackHome = (ImageView) findViewById(R.id.back);
        mProdutoImage = (ImageView) findViewById(R.id.produto_image);

        mFavorityUserPost = (ImageView) findViewById(R.id.favority_user_id);
        mProdutoUserAvatar = (CircleImageView) findViewById(R.id.produto_avatar_user);

        mUserNumerCall = (TextView) findViewById(R.id.user_number_call);

        mLayoutShimmir = (RelativeLayout) findViewById(R.id.animationShimmir);
        mLayoutInformation = (RelativeLayout) findViewById(R.id.information);


        final Intent intent = getIntent();
        String objectId = intent.getExtras().getString(ObjectId);

        ParseQuery<Posts> postsParseQuery = Posts.getQuery();
        postsParseQuery.getInBackground(objectId, new GetCallback<Posts>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(final  Posts posts, ParseException e) {
                if (posts != null) {

                    ProductDescriptionActivity.this.posts = posts;

                    if (posts.getAuthor().getPhotoUrl().isEmpty()) {
                        mProdutoUserAvatar.setImageResource(R.drawable.placeholder);
                    } else {

                        Glide.with(ProductDescriptionActivity.this)
                                .load(posts.getAuthor().getPhotoUrl())
                                .error(R.drawable.placeholder)
                                .centerCrop()
                                .fitCenter()
                                .placeholder(R.drawable.placeholder)
                                .into(mProdutoUserAvatar);

                    }

                    getImagesList(posts);

                    //////////////////////////////////////////////////////
                    if (posts.getPostImageUrl().isEmpty()) {

                        mProdutoImage.setImageResource(R.drawable.placeholder);

                    } else {

                        Glide.with(ProductDescriptionActivity.this)
                                .load(posts.getPostImageUrl())
                                //.networkPolicy(NetworkPolicy.OFFLINE)
                                .error(R.drawable.placeholder)
                                .fitCenter()
                                .centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .into(mProdutoImage);

                    }



                    mProdutoTitle.setText(ProductDescriptionActivity.this.posts.getTitle());
                    mProdutoDescription.setText(ProductDescriptionActivity.this.posts.getDescription());
                    mProdutoLocation.setText(ProductDescriptionActivity.this.posts.getLocalization());

                    mProdutoPreco.setText(ProductDescriptionActivity.this.posts.getPrice());
                    mProdutoUserNames.setText(posts.getAuthor().getUserFirstName() + " " + posts.getAuthor().getUserLastName());
                    mUserNumerCall.setText(posts.getAuthor().getNumber());
                    final String mNumero_Telefone = ProductDescriptionActivity.this.posts.getAuthor().getNumber();

                    mLayoutShimmir.setVisibility(View.GONE);
                    mLayoutInformation.setVisibility(View.VISIBLE);



                    fab1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(Intent.ACTION_DIAL);

                            i.setData(Uri.parse("tel:"+mNumero_Telefone));
                            startActivity(i);

                            Toast.makeText(getApplicationContext(), "Ligar para " +posts.getAuthor().getUserFirstName() +posts.getAuthor().getUserLastName(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });


        ParseQuery<Posts> PostQuery = Posts.getQuery();
        PostQuery.getInBackground(objectId, new GetCallback<Posts>() {
            @Override
            public void done(Posts mpost, ParseException e) {
                if (mpost != null) {

                    ProductDescriptionActivity.this.posts = mpost;
                    mCurrentUser = (User) ParseUser.getCurrentUser();

                    if (mpost.getAuthor().getObjectId().equals(mCurrentUser.getObjectId())) {
                        floatingActionsMenu.setVisibility(View.GONE);
                        mTextInfo.setVisibility(View.GONE);
                        mBuyForm.setVisibility(View.GONE);
                        mUserNumerCall.setVisibility(View.VISIBLE);

                    }else {
                        floatingActionsMenu.setVisibility(View.VISIBLE);
                    }

                    Log.d("temos o valor do id  ", "é " + mpost);

                    ParseQuery<Favority> favorityQuery = Favority.getQuery();

                    favorityQuery.whereEqualTo(Favority.COL_POST, mpost);
                    favorityQuery.whereEqualTo(Favority.COL_AUTHOR, mCurrentUser);
                    favorityQuery.getFirstInBackground(new GetCallback<Favority>() {
                        @Override
                        public void done(Favority object, ParseException e) {

                            if (e == null) {

                                Log.d("adicinou ", "" + object);
                                mFavorityUserPost.setImageResource(R.drawable.favorito_bold);

                            }
                        }
                    });


                } else {

                    Toast.makeText(getApplicationContext(), "post é null " + e, Toast.LENGTH_LONG).show();
                }
            }
        });

        mFavorityUserPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFavorityUserPost.setImageResource(R.drawable.favorito_bold);

                ParseQuery<Favority> FavorityQuery = Favority.getQuery();
                User currentUser = (User) User.getCurrentUser();

                FavorityQuery.whereEqualTo(Favority.COL_AUTHOR, currentUser);
                FavorityQuery.whereEqualTo(Favority.COL_POST, posts);
                FavorityQuery.getFirstInBackground(new GetCallback<Favority>() {
                    @Override
                    public void done(Favority update, ParseException e) {
                        if (e == null) {

                            update.deleteInBackground(new DeleteCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {

                                        mFavorityUserPost.setImageResource(R.drawable.favorito_normal);
                                        Toast.makeText(getApplicationContext(), R.string.deleted_update, Toast.LENGTH_LONG).show();
                                        onStart();
                                        //.notifyDataSetChanged();

                                    } else {

                                        // not deleted
                                        Toast.makeText(getApplicationContext(), "not deleted " + e, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {

                            Favority favority = new Favority();
                            favority.setPost(posts);
                            favority.setAuthor(mCurrentUser);

                            favority.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {

                                        // Liked
                                        mFavorityUserPost.setImageResource(R.drawable.favorito_bold);
                                        Toast.makeText(getApplicationContext(), "adicinou nos favorito ", Toast.LENGTH_LONG).show();

                                    } else {

                                        // Not liked
                                        mFavorityUserPost.setImageResource(R.drawable.favorito_normal);
                                        Toast.makeText(getApplicationContext(), "removeu dos favoritos ", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        }

                    }
                });

            }
        });


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Floating Action working", Toast.LENGTH_LONG).show();
            }
        });
        mBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDescriptionActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        fabMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posts != null) {

                    if (posts.getAuthor().getObjectId().equals(mCurrentUser.getObjectId())) {
                        floatingActionsMenu.setVisibility(View.VISIBLE);


                    } else {

                        Intent startChat = new Intent(getApplicationContext(), ChatActivity.class);
                        startChat.putExtra(ObjectId, posts.getAuthor().getObjectId());
                        startActivity(startChat);
                    }

                }
            }
        });
        mBuyForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent startBuy = new Intent(getApplicationContext(), BuyFormActivity.class).
                        putExtra(BuyFormActivity.productName, posts.getTitle())
                        .putExtra(BuyFormActivity.productPrice, posts.getPrice())
                        .putExtra(BuyFormActivity.postId, posts.getObjectId());

                startActivity(startBuy);
            }
        });



        mProdutoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup();
            }
        });
    }

    private void ShowPopup() {
        myDialog.setContentView(R.layout.custompopup);
        mSlideViewPager = myDialog.findViewById(R.id.mViewPagerSlide);
        dotsLayout =myDialog.findViewById(R.id.dotsLinearLayout);

        SlideAdapter slideAdapter = new SlideAdapter(getApplicationContext(), parseFileArrayList);
        Log.d("ParseFileArrayList ", "" + parseFileArrayList);
        mSlideViewPager.setAdapter(slideAdapter);
        addDotsIndicator(0);

        if (parseFileArrayList != null) {
            if (parseFileArrayList.size() <= 1) {
                myDialog.setTitle("IMAGEM DO PRODUTO");
            }
            else {
                myDialog.setTitle("IMAGENS DO PRODUTO");
            }
        }

        mSlideViewPager.addOnPageChangeListener(listener);


        myDialog.show();
    }


    private  void addDotsIndicator(int position){


        dots = new TextView[parseFileArrayList.size()];
        dotsLayout.removeAllViews();

        for (int i=0;i<dots.length; i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextColor(getResources().getColor(R.color.grey));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);
        }

        if(dots.length>0)
        {
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            int currentItem = position;


        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {



        }
    };


    private  void getImagesList(Posts posts){

    parseFileArrayList = new ArrayList<>(posts.getImagePostArrayList());
    ProductDescriptionActivity.this.posts=posts;


        if (parseFileArrayList.size() > 0){

            for (int i = 0; i< parseFileArrayList.size(); i++)
            {

                String url = parseFileArrayList.get(i).getUrl();
                Log.v("IMAGES", url);
            }
        }
    }

}
