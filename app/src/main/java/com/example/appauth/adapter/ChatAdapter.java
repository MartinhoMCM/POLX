package com.example.appauth.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.models.Message;
import com.example.appauth.models.User;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Message> mMessages;
    private Context mContext;
    private User mUserId;

    public ChatAdapter(Context context, User userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_chat, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        final boolean isMe = message.getSenderAuthorId() != null && message.getSenderAuthorId().equals(mUserId.getObjectId());

        if (isMe) {
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.GONE);
            holder.bodyMe.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            holder.bodyMe.setBackground(ContextCompat.getDrawable(mContext, R.drawable.my_message));

        } else {
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.imageMe.setVisibility(View.GONE);
            holder.bodyMe.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.bodyMe.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.bodyMe.setBackground(ContextCompat.getDrawable(mContext, R.drawable.my_message1));


        }

        final ImageView profileView = isMe ? holder.imageMe : holder.imageOther;

        if (isMe){
            if (!message.getSenderAuthor().getPhotoUrl().isEmpty()){

                Glide.with(mContext).load(message.getSenderAuthor().getPhotoUrl()).into(profileView);
            } else {
                Glide.with(mContext).load(R.drawable.tres).into(profileView);
            }

        } else {
            if (message.getSenderAuthor().getPhotoUrl()!=null){

                Glide.with(mContext).load(message.getSenderAuthor().getPhotoUrl()).into(profileView);
            } else {
                Glide.with(mContext).load(R.drawable.tres).into(profileView);
            }
        }

        holder.bodyMe.setText(message.getMessage());


    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        ImageView imageMe;
        TextView bodyMe;
        //Text bodyOther;

        public ViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe);
            bodyMe = (TextView)itemView.findViewById(R.id.tvBodyMe);
           // bodyOther=itemView.findViewById(R.id.tvBodyOther);
        }
    }
}
