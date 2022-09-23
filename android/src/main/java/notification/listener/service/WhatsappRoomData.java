package notification.listener.service;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WhatsappRoomData {
    @PrimaryKey(autoGenerate = true)
    public int nid;

    @ColumnInfo(name = "room")
    public String room;

//    public RoomData(String room) {
//        this.room = room;
//    }
}
