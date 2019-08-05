package com.example.appauth.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.models.Favority;
import com.example.appauth.models.Notifications;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
        private List<Notifications> mNotifications;
    private Activity mContext;
    private User mCurrentUser;
    private onItemClickListener mListener;

    public NotificationsAdapter(Activity context, List<Notifications> notifications) {
        mNotifications = notifications;
        mContext = context;
    }

    public void onItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_notification_list_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Notifications notifications = mNotifications.get(position);

        mCurrentUser = (User) User.getCurrentUser();
        if (notifications != null)
        {

            if (notifications.getColNotiType().equals("favority")){

                String nomes = notifications.getAuthor().getUserFirstName() + " " + notifications.getAuthor().getUserLastName();
                String descricao = mContext.getString(R.string.favorito);

                SpannableString span1 = new SpannableString(nomes);

                span1.setSpan(new StyleSpan(Typeface.BOLD), 0, nomes.length(), SPAN_INCLUSIVE_INCLUSIVE);
                SpannableString span2 = new SpannableString(descricao);
                span2.setSpan(new RelativeSizeSpan(1f), 0, descricao.length(), SPAN_INCLUSIVE_INCLUSIVE);
                CharSequence Textos = TextUtils.concat(span1, " ", span2);

                viewHolder.mNotifiUsername.setText(Textos);
                viewHolder.mData.setText(notifications.getTime());


                if (notifications.getAuthor().getPhotoUrl().isEmpty()) {

                    viewHolder.mNotificImageUser.setImageResource(R.drawable.placeholder);

                } else {

                    Glide.with(mContext)
                            .load(notifications.getAuthor().getPhotoUrl())
                            //.networkPolicy(NetworkPolicy.OFFLINE)
                            .error(R.drawable.placeholder)
                            .fitCenter()
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(viewHolder.mNotificImageUser);

                }

                //////////////////////////////////////////////////////
                if (notifications.getPostImageUrl().isEmpty()) {

                    viewHolder.NotifcImage.setImageResource(R.drawable.placeholder);

                } else {

                    Glide.with(mContext)
                            .load(notifications.getPostImageUrl())
                            //.networkPolicy(NetworkPolicy.OFFLINE)
                            .error(R.drawable.placeholder)
                            .fitCenter()
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(viewHolder.NotifcImage);

                }

            }


        }
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mNotifiUsername,mData;
        ImageView NotifcImage,mFavoritoPost;
        CircleImageView mNotificImageUser;



        ViewHolder(View v) {
            super(v);

            mNotifiUsername = (TextView) v.findViewById(R.id.notifi_username);
            mData = (TextView) v.findViewById(R.id.notifi_message_txt);

            mNotificImageUser = (CircleImageView) v.findViewById(R.id.notifi_userProfile);
            NotifcImage = (ImageView) v.findViewById(R.id.notifi_postPhoto);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {

                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

}