package com.mbobiosio.onesignalroom.ui.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.mbobiosio.onesignalroom.R;
import com.mbobiosio.onesignalroom.ui.data.NotificationDB;
import com.mbobiosio.onesignalroom.ui.ui.base.BaseActivity;
import com.mbobiosio.onesignalroom.ui.view.NotificationDao;

public class MainActivity extends BaseActivity {

    NotificationDB mNotifDB;
    NotificationDao mNotifDAO;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

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

    public void invalidateCart() {
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
