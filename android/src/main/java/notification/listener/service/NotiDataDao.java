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

    @Query("DELETE FROM NotiData WHERE date(vsDate) < date('now', '-3 days')")
    void delete();

    @Query("DELETE FROM NotiData WHERE room = :room")
    void roomDelete(String room);

    @Query("select * from NotiData order by nid desc limit 1")
    List<NotiData> select();

//    public class NotiData {
//        public String name;
//        public String text;
//        public String room;
//        public String date;
//        public String vsDate;
//        public String send;
//    }

}
