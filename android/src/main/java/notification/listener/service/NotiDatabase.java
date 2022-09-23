package notification.listener.service;

import android.content.Context;
import androidx.room.*;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
//import androidx.room.Migration
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {NotiData.class, KakaoData.class, WhatsappData.class, RoomData.class, KakaoRoomData.class, WhatsappRoomData.class},
        version = 1, exportSchema = false)
public abstract class NotiDatabase extends RoomDatabase {
    public abstract NotiDataDao NotiDao();
    public abstract KakaoDataDao KakaoDao();
    public abstract WhatsappDataDao WhatsappDao();
    public abstract RoomDataDao RoomDataDao();
    public abstract KakaoRoomDataDao KakaoRoomDataDao();
    public abstract WhatsappRoomDataDao WhatsappRoomDataDao();

    private static NotiDatabase database;

    public static synchronized NotiDatabase getInstance(Context context){
        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(), NotiDatabase.class, "messageTest.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return database;

    }

    public static void destroyInstance() {
        database = null;
    }
}
