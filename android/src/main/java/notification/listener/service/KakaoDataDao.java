package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

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

    @Query("update KakaoData set result = :result where nid = (select nid from (select * from KakaoData where result = 'yet' limit 1))")
    void update(String result);

//    @Query("update KakaoData set read = 1 where room = (select nid from (select * from KakaoData where read = 2))")
//    void update(String result);

    @Query("DELETE FROM KakaoData WHERE room = :room")
    void roomDelete(String room);

    @Query("select * from KakaoData where room = :room order by nid desc limit 1")
    List<KakaoData> select(String room);

    @Query("SELECT * FROM KakaoData WHERE result = 'yet'")
    List<KakaoData> undetectedSelect();

    @Query("select count(*) from KakaoData where room = :room and read = '2'")
    int roomCount(String room);

    @Query("select count(*) from KakaoData")
    int total();

    @Query("select count(*) from KakaoData where result = '2'")
    int resultCount();

    @Query("select text from KakaoData where room = :room order by nid desc limit 1")
    String lastText(String room);

    @Query("select date from KakaoData where room = :room order by nid desc limit 1")
    String lastDate(String room);

    @Query("update KakaoData set read = '1' where room = :room and read = '2'")
    void read(String room);


//    public class NotiData {
//        public String name;
//        public String text;
//        public String room;
//        public String date;
//        public String vsDate;
//        public String send;
//    }

}
