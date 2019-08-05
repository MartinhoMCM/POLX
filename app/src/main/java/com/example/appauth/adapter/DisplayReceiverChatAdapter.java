package com.example.appauth.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.models.Message;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayReceiverChatAdapter extends RecyclerView.Adapter<DisplayReceiverChatAdapter.ChatViewHolder> {


    public interface  OnItemClickListener
    {
        void onItemClick(Message user);
    }

    private ArrayList<Message> messageArrayList;
    private Context context;
    private OnItemClickListener listener;


    public DisplayReceiverChatAdapter(ArrayList<Message> messageArrayList, Context context, OnItemClickListener listener) {
        this.messageArrayList = messageArrayList;
        this.context = context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_displayreceiver, parent, false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        Message message = messageArrayList.get(position);

       // try{
            if(message.getSenderAuthor()!=null) {

                try {
                    String userNameString =message.getSenderAuthor().fetchIfNeeded().getString("username");
                    String userDesc = message.fetchIfNeeded().getString("message");

                    if(userDesc.length()>25)
                    {
                        userDesc=userDesc.substring(0,24)+ "...";
                    }


                    holder.UserChatName.setText(userNameString);
                    holder.UserChatDesc.setText(userDesc);

                    holder.bind(message, listener);
                    Date date = message.getCreatedAt();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    holder.mTime.setText(simpleDateFormat.format(date));


                    if(message.getSenderAuthor().getPhotoUrl()!=null)
                    {
                        Glide.with(context).load(message.getSenderAuthor().getPhotoUrl()).into(holder.UserChatImage);
                    }
                    else {
                        Glide.with(context).load(R.drawable.tres).into(holder.UserChatImage);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    holder.cardView.setVisibility(View.GONE);
                }




            }

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder
    {
        private TextView UserChatName,UserChatDesc,mTime;
        private CardView cardView;
        private CircleImageView UserChatImage;

        ChatViewHolder( View itemView) {
            super(itemView);

            UserChatName = itemView.findViewById(R.id.UserChatName);
            UserChatDesc=itemView.findViewById(R.id.UserChatDesc);
            mTime =itemView.findViewById(R.id.mTime);
            UserChatImage=itemView.findViewById(R.id.UserChatImage);
            cardView=itemView.findViewById(R.id.idCardView);
        }

        void bind(final Message usetItem, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(usetItem);
                }
            });
        }

    }

    // Add a list of items -- change to type used
    public void addAll(ArrayList<Message> list) {
        messageArrayList.addAll(list);
        notifyDataSetChanged();
    }
}
