package com.example.appauth.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appauth.R;
import com.example.appauth.chat.ChatActivity;
import com.example.appauth.models.Message;
import com.example.appauth.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.example.appauth.adapter.DisplayReceiverChatAdapter;

public class MessageHomePage_Fragment extends Fragment {

    private DisplayReceiverChatAdapter adapter;
    private RecyclerView mRecyclerChat;
    private User mCurrentUser;
    private ArrayList<Message> messageArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View view;
    private ParseQuery<Message> query;

    public MessageHomePage_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_fragment, container, false);

        mCurrentUser =(User) ParseUser.getCurrentUser();
        mRecyclerChat = view.findViewById(R.id.displayreceiver);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        messageArrayList= new ArrayList<>();

        swipeRefreshLayout.setRefreshing(true);

        DisplayReceiverNotification();
        addContent();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                DisplayReceiverNotification();
                addContent();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    private void addContent() {

        adapter= new DisplayReceiverChatAdapter(messageArrayList, getActivity(),
                new DisplayReceiverChatAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Message userItem) {


                        Intent startChatActivity =new Intent(getActivity(), ChatActivity.class);
                        if( userItem.getSenderAuthor().getObjectId().equals(mCurrentUser.getObjectId()))
                        {

                            startChatActivity.putExtra(ProductDescriptionActivity.ObjectId, userItem.getReceiverAuthor().getObjectId());
                        }
                        else
                        {
                            startChatActivity.putExtra(ProductDescriptionActivity.ObjectId, userItem.getSenderAuthor().getObjectId());
                        }
                        startActivity(startChatActivity);

                    }
                });
        mRecyclerChat.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerChat.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    public void DisplayReceiverNotification()
    {

        if(mCurrentUser!=null) {

            Message message = new Message();

           ParseQuery<Message> query1 = message.getMessageParseQuery();
           ParseQuery<Message> query2 =message.getMessageParseQuery();

            query1.whereEqualTo(Message.RECEIVER_AUTHOR_ID, mCurrentUser.getObjectId());

            query2.whereEqualTo(Message.SENDER_AUTHOR_ID, mCurrentUser.getObjectId());

            List<ParseQuery<Message>> queries = new ArrayList<>();
            queries.add(query1);
            queries.add(query2);


            query = ParseQuery.or(queries);
            query.addDescendingOrder(Message.MESSAGE);

            query.findInBackground(new FindCallback<Message>() {
                @Override
                public void done(List<Message> objects, ParseException e) {

                 if(e==null)
                 {
                     if(objects.size()>0)
                     {

                         ArrayList<User> authorList =new ArrayList<>();
                         authorList.add(objects.get(0).getSenderAuthor());

                         messageArrayList.clear();
                         messageArrayList.add(objects.get(0));
                         for (int counter=1; counter<objects.size();counter++)
                         {
                             if(!authorList.contains(objects.get(counter).getSenderAuthor()))
                             {
                                 authorList.add(objects.get(counter).getSenderAuthor());
                                 messageArrayList.add(objects.get(counter));

                             }
                         }
                         // messageArrayList.addAll(objects);
                         adapter.notifyDataSetChanged();
                     }
                     else {
                         //No message available
                     }


                 }
                 else
                     {
                         if(e.getCode()==ParseException.CONNECTION_FAILED)
                         {
                             Toast.makeText(getContext(), "Conexao falhou ", Toast.LENGTH_LONG).show();
                         }
                         messageArrayList.clear();
                     }

                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        addContent();
    }
}
