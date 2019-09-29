package com.mbobiosio.onesignalroom.ui.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.mbobiosio.onesignalroom.ui.model.Notification;
import com.mbobiosio.onesignalroom.ui.view.NotificationDao;

/**
 * Created by Mbuodile Obiosio on Jul 25,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
@Database(entities = {Notification.class},version = 1)
public abstract class NotificationDB extends RoomDatabase {

    private static final String DB_NAME = "notifications_db";
    public abstract NotificationDao notificationDao();
    private static NotificationDB INSTANCE;

    private static Object LOCK = new Object();

    public static NotificationDB getInstance(Context context){
        if(INSTANCE == null){
            synchronized (LOCK){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NotificationDB.class, NotificationDB.DB_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

}
