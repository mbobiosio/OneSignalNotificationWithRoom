package com.mbobiosio.onesignalroom.ui.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.mbobiosio.onesignalroom.R;
import com.mbobiosio.onesignalroom.ui.data.NotificationDB;
import com.mbobiosio.onesignalroom.ui.model.Notification;
import com.mbobiosio.onesignalroom.ui.ui.adapter.NotificationAdapter;
import com.mbobiosio.onesignalroom.ui.ui.base.BaseActivity;
import com.mbobiosio.onesignalroom.ui.view.NotificationDao;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class NotificationsList extends BaseActivity {

    @BindView(R.id.notifications_list)
    RecyclerView mNotificationsList;
    @BindView(R.id.notifications_count)
    TextView mCount;
    NotificationAdapter mAdapter;
    List<Notification> mList = new ArrayList<>();
    NotificationDao mDAO;
    NotificationDB mDataBase;
    BroadcastReceiver mReceiver;
    IntentFilter mFilter;
    String mImgUrl, mTitle, mBody;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_notifications_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mNotificationsList.setLayoutManager(linearLayoutManager);
        //mNotificationsList.setHasFixedSize(true);

        linearLayoutManager.setReverseLayout(true);

        mDataBase = NotificationDB.getInstance(this);
        mDAO = mDataBase.notificationDao();

        mFilter = new IntentFilter();
        mFilter.addAction("com.talentbox.afcquarterly");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                startActivity(getIntent());
                notificationCount();
            }
        };

        init();
        handleAdapter();
        mNotificationsList.setAdapter(mAdapter);
        setResume();

        notificationCount();
    }

    private void init() {
        Single.fromCallable(() -> mDAO.getAllNotification())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(messages -> {
                    mList.clear();
                    mList.addAll(messages);
                    mAdapter.notifyDataSetChanged();
                })
                .doOnError(throwable -> {

                })
                .subscribe();
    }

    private void handleAdapter() {

        mAdapter = new NotificationAdapter(this, mList, (view, position) -> Observable.create(emitter -> mDAO.updateSeen(mList.get(position).getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mImgUrl = mList.get(position).getImg_url();
                        mTitle = mList.get(position).getTitle();
                        mBody = mList.get(position).getMessage();
                        //mMessages = new Messages(mTitle, mBody, DateUtil.covertTimeToText(), false);

                        Intent i = new Intent(NotificationsList.this, NotificationDetail.class);
                        i.putExtra("title", mList.get(position).getTitle());
                        i.putExtra("message", mList.get(position).getMessage());
                        i.putExtra("date", mList.get(position).getTime());
                        if (mList.get(position).getImg_url() != null)
                            i.putExtra("image_url", mList.get(position).getImg_url());
                        startActivity(i);
                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

    private void notificationCount() {

        Single.fromCallable(() -> mDAO.getCount())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(integer -> {
                    if (integer == 0)
                        mCount.setText(getString(R.string.all_notifications));
                    else
                        mCount.setText(getString(R.string.new_notification).concat(" ")
                                .concat("(").concat(String.valueOf(integer).concat(")")));
                    setResume();
                })
                .doOnError(throwable -> {

                })
                .subscribe();
    }


    private void setResume() {
        Single.fromCallable(() -> mDAO.getAllNotification())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(messages -> {
                    mList.clear();
                    mList.addAll(messages);
                    mAdapter.setData(messages);
                    mAdapter.notifyDataSetChanged();
                })
                .doOnError(throwable -> {

                })
                .subscribe();
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mFilter);
        setResume();
        notificationCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
