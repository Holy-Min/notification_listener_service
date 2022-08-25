package android.src.main.java.notification.listener.service.kakao;

import android.content.Context;
import androidx.room.*;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
//import androidx.room.Migration
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {NotiData.class, RoomData.class}, version = 1)
public abstract class NotiDatabase extends RoomDatabase {
    public abstract NotiDataDao NotiDao();
    public abstract RoomDataDao RoomDataDao();

    private static NotiDatabase database;

    public synchronized static NotiDatabase getInstance(Context context){
        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(), NotiDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return database;

    }

    void destroyNotiInstance() {
        database = null;
    }
}

