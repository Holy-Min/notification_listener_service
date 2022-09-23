package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface KakaoDataDao {
    @Query("SELECT * FROM KakaoData")
    List<KakaoData> getAll();

    @Query("SELECT * FROM KakaoData WHERE room= :room")
    List<KakaoData> getRoom(String room);

//    @Query("INSERT INTO NotiData VALUES(name= :name, text= :text, room= :room, date= :date, send= :send)")
    @Insert
    void addChat(KakaoData noti);

    @Insert
    void insert(KakaoData noti);

    @Query("DELETE FROM KakaoData WHERE date(vsDate) < date('now', '-3 days')")
    void delete();

    @Query("DELETE FROM KakaoData WHERE room = :room")
    void roomDelete(String room);

    @Query("select * from KakaoData where room = :room order by nid desc limit 1")
    List<KakaoData> select(String room);

    @Query("SELECT * FROM KakaoData WHERE result = 'yet'")
//    @Query("SELECT * FROM KakaoData WHERE result = :result")
    List<KakaoData> undetectedSelect();
//    List<KakaoData> undetecdSelect(String result);

//    public class NotiData {
//        public String name;
//        public String text;
//        public String room;
//        public String date;
//        public String vsDate;
//        public String send;
//    }

}
