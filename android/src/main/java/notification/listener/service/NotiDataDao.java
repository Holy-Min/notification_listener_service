package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface NotiDataDao {
    @Query("SELECT * FROM NotiData")
    List<NotiData> getAll();

    @Query("SELECT * FROM NotiData WHERE room= :room")
    List<NotiData> getRoom(String room);

//    @Query("INSERT INTO NotiData VALUES(name= :name, text= :text, room= :room, date= :date, send= :send)")
    @Insert
    void addChat(NotiData noti);

    @Insert
    void insert(NotiData noti);

    @Delete
    void delete(NotiData noti);

}
