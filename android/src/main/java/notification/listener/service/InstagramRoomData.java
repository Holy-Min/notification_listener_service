package notification.listener.service;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class InstagramRoomData {
    @PrimaryKey(autoGenerate = true)
    public int nid;

    @ColumnInfo(name = "room")
    public String room;

    @ColumnInfo(name = "vsDate")
    public String vsDate;

    @ColumnInfo(name = "isSafe")
    public int isSafe;

//    public RoomData(String room) {
//        this.room = room;
//    }
}
