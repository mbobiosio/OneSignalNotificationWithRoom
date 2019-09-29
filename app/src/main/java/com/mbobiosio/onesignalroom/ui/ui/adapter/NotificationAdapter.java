package com.mbobiosio.onesignalroom.ui.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mbobiosio.onesignalroom.R;
import com.mbobiosio.onesignalroom.ui.model.Notification;
import com.mbobiosio.onesignalroom.ui.utils.DateUtil;

import java.util.List;
import java.util.Random;

/**
 * Created by Mbuodile Obiosio on Jul 28,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context mContext;
    private List<Notification> mNotificationList;
    private NotificationListener mListener;

    public NotificationAdapter(Context mContext, List<Notification> mNotificationList, NotificationListener mListener) {
        this.mContext = mContext;
        this.mNotificationList = mNotificationList;
        this.mListener = mListener;
    }

    public interface NotificationListener {
        void onItemClick(View view, int position);
    }

    public void setData(List<Notification> items) {
        mNotificationList = items;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false), mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        Random r = new Random();
        int red = r.nextInt(255 + 1);
        int green = r.nextInt(255 + 1);
        int blue = r.nextInt(255 + 1);

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.OVAL);
        draw.setSize(80, 80);
        draw.setColor(Color.rgb(red, green, blue));
        holder.mBoldText.setBackground(draw);

        holder.mTitle.setText(mNotificationList.get(position).getTitle());
        holder.mBoldText.setText(mNotificationList.get(position).getTitle().substring(0, 1));
        holder.mTime.setText(DateUtil.covertTimeToText(mNotificationList.get(position).getTime()));
        //holder.message.setText(mNotificationList.get(position).getTitle());
        if (mNotificationList.get(position).getSeen()) {
            holder.mNotificationItem.setCardBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        } else
            holder.mNotificationItem.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.browser_actions_bg_grey));

    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.notification_item)
        CardView mNotificationItem;
        @BindView(R.id.bold_text)
        TextView mBoldText;
        @BindView(R.id.time_ago)
        TextView mTime;
        NotificationListener mListener;

        public NotificationViewHolder(View itemView, NotificationListener Listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mListener = Listener;
        }

        @Override
        public void onClick(View view) {

            int id = view.getId();
            int position = getAdapterPosition();
            /*if (position != RecyclerView.NO_POSITION) {
                if (id == R.id.root_layout) {
                    mListener.onItemClick(view, position);
                }
            }*/
            mListener.onItemClick(view, position);

        }
    }
}