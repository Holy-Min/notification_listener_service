package notification.listener.service;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LineData {
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

    @ColumnInfo(name = "vsDate")
    public String vsDate;

    @ColumnInfo(name = "send")
    public int send;

    @ColumnInfo(name = "app")
    public String app;
    
    @ColumnInfo(name = "result")
    public String result;

    @ColumnInfo(name = "read")
    public String read;

    @ColumnInfo(name = "url")
    public String url;

}
