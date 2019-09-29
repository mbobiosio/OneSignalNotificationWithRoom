package com.mbobiosio.onesignalroom.ui.view;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.mbobiosio.onesignalroom.ui.model.Notification;

import java.util.List;

/**
 * Created by Mbuodile Obiosio on Jul 25,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
@Dao
public interface NotificationDao {
    //@Query("SELECT * FROM notifications WHERE img_url is not null")
    @Query("SELECT * FROM notifications")
    List<Notification> getAllNotification();

    @Query("SELECT * FROM notifications WHERE img_url is  null")
    List<Notification> getAllTextNotification();


    @Query("SELECT COUNT(id) FROM notifications WHERE seen = 0 ")
    int getCount();

    @Query("SELECT COUNT(id) FROM notifications WHERE seen = 0 AND img_url is null")
    int getTextCount();

    @Query("SELECT COUNT(id) FROM notifications WHERE seen = 0 AND img_url is not null")
    int getImageCount();

    @Insert
    long addToList(Notification messages);

    @Delete
    void deletefromlist(Notification messages);

    @Query("UPDATE notifications SET seen = 1 where id = :tid")
    void updateSeen(long tid);
}
