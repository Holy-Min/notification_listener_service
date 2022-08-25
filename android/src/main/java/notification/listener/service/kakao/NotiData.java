package android.src.main.java.notification.listener.service.kakao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotiData {
    @PrimaryKey(autoGenerate = true)
    public int nid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "room")
    public String room;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "send")
    public int send;

    @ColumnInfo(name = "id")
    public String id;
}
