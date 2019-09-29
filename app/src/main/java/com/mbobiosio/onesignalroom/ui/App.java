package com.mbobiosio.onesignalroom.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
import com.mbobiosio.onesignalroom.ui.data.NotificationDB;
import com.mbobiosio.onesignalroom.ui.model.Notification;
import com.mbobiosio.onesignalroom.ui.ui.activity.NotificationDetail;
import com.mbobiosio.onesignalroom.ui.utils.DateUtil;
import com.mbobiosio.onesignalroom.ui.view.NotificationDao;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.json.JSONObject;
import timber.log.Timber;

/**
 * Created by Mbuodile Obiosio on Jul 23,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class App extends Application {

    private static App mApp;

    String mTitle, mBody, mImgUrl;
    Long mPosition;
    Notification mNotification;
    NotificationDB mNotificationDB;
    NotificationDao mNotificationDAO;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;

        initOneSignal();
    }

    private void initOneSignal() {
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationReceivedHandler(new NotificationReceivedHandler())
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .init();
    }

    private class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {

            mTitle = notification.payload.title;
            mBody = notification.payload.body;
            mImgUrl = notification.payload.bigPicture;
            mNotificationDB = NotificationDB.getInstance(getApplicationContext());
            mNotificationDAO = mNotificationDB.notificationDao();
            if (mImgUrl == null)
                mNotification = new Notification(mTitle, mBody, DateUtil.notificationTime(), false);
            else
                mNotification = new Notification(mTitle, mBody, DateUtil.notificationTime(), false, mImgUrl);

            addToDatabase();
        }
    }

    private void addToDatabase() {

        Single.fromCallable(() -> mNotificationDAO.addToList(mNotification))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(aLong -> {
                    mPosition = aLong;
                    Intent local = new Intent();
                    local.setAction("com.mbobiosio.onesignalroom");
                    getApplicationContext().sendBroadcast(local);
                })
                .subscribe();
    }

    private class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String customKey;
            if (data != null) {
                customKey = data.optString("intent", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }
            if (actionType == OSNotificationAction.ActionType.ActionTaken) {
                Timber.d("ID " + result.action.actionID);
            }
            final Intent resultIntent = new Intent(App.this, NotificationDetail.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            resultIntent.putExtra("title", mTitle);
            resultIntent.putExtra("message", mBody);
            if (mImgUrl != null)
                resultIntent.putExtra("image_url", mImgUrl);

            Observable.create(emitter -> mNotificationDAO.updateSeen(mPosition))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            startActivity(resultIntent);
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
                    });
        }
    }


    public static App getInstance() {
        return mApp;
    }
}
