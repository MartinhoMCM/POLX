package com.example.appauth.chat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.app.App;
import com.example.appauth.home.HomeActivity;
import com.example.appauth.home.MessageHomePage_Fragment;
import com.example.appauth.models.Message;
import com.example.appauth.models.User;
import com.github.ybq.android.spinkit.style.Circle;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.SubscriptionHandling;

import java.util.ArrayList;
import java.util.List;

import com.example.appauth.adapter.ChatAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.appauth.home.ProductDescriptionActivity.*;

public class ChatActivity extends AppCompatActivity {

    static final String TAG = MessageHomePage_Fragment.class.getSimpleName();

    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    EditText etMessage;
    Button btSend;

    RecyclerView rvChat;
    ArrayList<Message> mMessages;
    ChatAdapter mAdapter;

    boolean mFirstLoad;

    private User mCurrentUser, mReceiverId;
    private ParseQuery<Message> query, parseQuery;

    private TextView idUserReceiver;
    private CircleImageView idImageReceiverUser;

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private LinearLayout mUserLigar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String objectId = getIntent().getStringExtra(ObjectId);
        mCurrentUser = (User) ParseUser.getCurrentUser();
        parseQuery =ParseQuery.getQuery(Message.class);
        idUserReceiver=findViewById(R.id.idReceiverUser);
        idImageReceiverUser=findViewById(R.id.idReceiverUserImage);
        mUserLigar = findViewById(R.id.call);
        toolbar =findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);


        ToolbarListener();

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        progressBar =findViewById(R.id.spin_kitProgressbar);
        Circle wave =new Circle();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setVisibility(View.VISIBLE);

        mUserLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String mNumero_Telefone = mCurrentUser.getNumber();

                i.setData(Uri.parse("tel:"+mNumero_Telefone));
                startActivity(i);

                Toast.makeText(getApplicationContext(), "Ligando" , Toast.LENGTH_LONG).show();
            }
        });



        // Loading
        ParseQuery<User> receiverUser = ParseQuery.getQuery(User.class);
        receiverUser.getInBackground(objectId, new GetCallback<User>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(User user, ParseException e) {

                if (user != null){
                    // Hide
                    mReceiverId = user;
                    refreshMessages();
                    setupMessagePosting();

                    idUserReceiver.setText(user.getUserFirstName());
                    Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(idImageReceiverUser);
                }
            }
        });

        //myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        // parseQuery.whereNotEqualTo(SENDER_AUTHOR, ParseUser.getCurrentUser().getObjectId());

        // Connect to Parse server
        SubscriptionHandling<Message> subscriptionHandling = App.getLiveQueryClient().subscribe(parseQuery);
        // Listen for CREATE events
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new SubscriptionHandling.HandleEventCallback<Message>() {
            @Override
            public void onEvent(ParseQuery<Message> query, Message object) {
                mMessages.add(0, object);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        rvChat.scrollToPosition(0);
                    }
                });
            }
        });

    }

    private void ToolbarListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }

    void setupMessagePosting() {

        // Find the text field and button
        etMessage = findViewById(R.id.etMessage);
        btSend = findViewById(R.id.btSend);
        rvChat=(RecyclerView)findViewById(R.id.rvChat);
        mFirstLoad =true;
        mMessages = new ArrayList<>();
        mAdapter = new ChatAdapter(ChatActivity.this, mCurrentUser, mMessages );
        rvChat.setAdapter(mAdapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
         linearLayoutManager.setReverseLayout(true);
        rvChat.setLayoutManager(linearLayoutManager);

        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                Message message = new Message();

                // Message
                message.setMessage(data);

                // Sender
                message.setSenderAuthor(mCurrentUser);
                message.setSenderAuthorId(mCurrentUser.getObjectId());

                // Receiver
                message.setReceiverAuthor(mReceiverId);
                message.setReceiverAuthorId(mReceiverId.getObjectId());

                /*** END OF CHANGE **/

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                            Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).setDuration(Toast.LENGTH_LONG);
                            refreshMessages();
                    }
                });
                etMessage.setText(null);
                /**mMessages.add(message);
                mAdapter.notifyDataSetChanged();
                rvChat.scrollToPosition(mAdapter.getItemCount()-1);
                **/
            }
        });
    }

    // Query messages from Parse so we can load them into the chat adapter
    void refreshMessages() {
        // Construct query to execute

        Message message = new Message();
        ParseQuery<Message> query1 = message.getMessageParseQuery();
        ParseQuery<Message> query2 = message.getMessageParseQuery();

        // Get sent message to this user
        query1.whereEqualTo(Message.SENDER_AUTHOR_ID, mCurrentUser.getObjectId());
        query1.whereEqualTo(Message.RECEIVER_AUTHOR_ID, mReceiverId.getObjectId());



        // Get message From this user
        query2.whereEqualTo(Message.SENDER_AUTHOR_ID, mReceiverId.getObjectId());
        query2.whereEqualTo(Message.RECEIVER_AUTHOR_ID, mCurrentUser.getObjectId());

        List<ParseQuery<Message>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);


        query =ParseQuery.or(queries);
        query.addDescendingOrder("createdAt");

        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                progressBar.setVisibility(View.GONE);
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }



}
