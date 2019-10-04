package com.mbobiosio.onesignalroom.ui.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mbobiosio.onesignalroom.R;
import com.mbobiosio.onesignalroom.ui.App;
import com.mbobiosio.onesignalroom.ui.data.NotificationDB;
import com.mbobiosio.onesignalroom.ui.ui.base.BaseActivity;
import com.mbobiosio.onesignalroom.ui.view.NotificationDao;

public class MainActivity extends BaseActivity {

    NotificationDB mNotifDB;
    NotificationDao mNotifDAO;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    private static int items;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNotifDB = NotificationDB.getInstance(this);
        mNotifDAO = mNotifDB.notificationDao();
        mIntentFilter = new IntentFilter();

        mIntentFilter.addAction("com.mbobiosio.onesignalroom");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //removeBadge(mNavigation, R.id.navigation_account);
            }
        };

        //notification("", mNotifDAO);
    }

/*

    private Drawable buildCounterDrawable(int count, Drawable backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.notification_layout, null);
        view.setBackground(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }
*/

    private static void notification(BottomNavigationView mNavigation, NotificationDao dao) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return dao.getCount();
            }

            @Override
            protected void onPostExecute(Integer count) {
                items = count;
                if (count == 0) {
                    //removeBadge(mNavigation, R.id.navigation_account);
                } else {
                    //showBadge(App.getInstance(), mNavigation, R.id.navigation_account, String.valueOf(count));
                }
            }
        }.execute();
    }

    public void invalidateCart() {
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
        //removeBadge(mNavigation, R.id.navigation_account);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.notification_badge, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText(value);
        itemView.addView(badge);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
