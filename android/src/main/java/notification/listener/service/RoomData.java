package notification.listener.service;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomData {
    @PrimaryKey(autoGenerate = true)
    public int nid;

    @ColumnInfo(name = "room")
    public String room;

    @ColumnInfo(name = "app")
    public String app;

    @ColumnInfo(name = "vsDate")
    public String vsDate;

//    public RoomData(String room) {
//        this.room = room;
//    }
}
