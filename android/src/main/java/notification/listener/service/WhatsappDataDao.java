package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface WhatsappDataDao {
    @Query("SELECT * FROM WhatsappData")
    List<WhatsappData> getAll();

    @Query("SELECT * FROM WhatsappData WHERE room= :room")
    List<WhatsappData> getRoom(String room);

//    @Query("INSERT INTO NotiData VALUES(name= :name, text= :text, room= :room, date= :date, send= :send)")
    @Insert
    void addChat(WhatsappData noti);

    @Insert
    void insert(WhatsappData noti);

    @Query("DELETE FROM WhatsappData WHERE date(vsDate) < date('now', '-3 days')")
    void delete();

    @Query("update WhatsappData set result = :result where result = 'yet'")
    void update(String result);

    @Query("DELETE FROM WhatsappData WHERE room = :room")
    void roomDelete(String room);

    @Query("select * from WhatsappData where room = :room order by nid desc limit 1")
    List<WhatsappData> select(String room);

    @Query("SELECT * FROM WhatsappData WHERE result = 'yet'")
//    @Query("SELECT * FROM WhatsappData WHERE result = :result")
    List<WhatsappData> undetectedSelect();
//    List<WhatsappData> undetecdSelect(String result);

//    public class NotiData {
//        public String name;
//        public String text;
//        public String room;
//        public String date;
//        public String vsDate;
//        public String send;
//    }

}
