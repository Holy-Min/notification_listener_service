package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TelegramDataDao {
    @Query("SELECT * FROM TelegramData")
    List<TelegramData> getAll();

    @Query("SELECT * FROM TelegramData WHERE room= :room")
    List<TelegramData> getRoom(String room);

//    @Query("INSERT INTO NotiData VALUES(name= :name, text= :text, room= :room, date= :date, send= :send)")
    @Insert
    void addChat(TelegramData noti);

    @Insert
    void insert(TelegramData noti);

    @Query("DELETE FROM TelegramData WHERE date(vsDate) < date('now', '-3 days')")
    void delete();

//    @Query("update TelegramData set result = :result, url = :url where nid = (select nid from (select * from TelegramData where url = 'yet' and result = 'yet' limit 1))")
//    void update(String result, String url);

    @Query("update TelegramData set result = :result, url = :url, text = :text where nid = :nid")
    void update(String result, String url, int nid, String text);

//    @Query("update TelegramData set result = :result where nid = (select nid from (select * from TelegramData where result = 'yet' limit 1))")
//    void update(String result);
//
//    @Query("update TelegramData set url = :url where nid = (select nid from (select * from TelegramData where url = 'yet' limit 1))")
//    void updateUrl(String url);

//    @Query("update TelegramData set read = 1 where room = (select nid from (select * from TelegramData where read = 2))")
//    void update(String result);

    @Query("DELETE FROM TelegramData WHERE room = :room")
    void roomDelete(String room);

    @Query("DELETE FROM TelegramData")
    void deleteAll();

    @Query("select * from TelegramData where room = :room order by nid desc limit 1")
    List<TelegramData> select(String room);

    @Query("SELECT * FROM TelegramData WHERE result = 'yet'")
    List<TelegramData> undetectedSelect();
    
    @Query("SELECT * FROM TelegramData order by nid desc limit 1")
    List<TelegramData> lastOneSelect();

    @Query("select count(*) from TelegramData where room = :room and read = '2'")
    int roomCount(String room);

    @Query("select count(*) from TelegramData")
    int total();

    @Query("select count(*) from TelegramData where result NOT in ('101', '111', '201', '211', '301', '311', 'yet', 'N')")
    int resultCount();

    @Query("select text from TelegramData where room = :room order by nid desc limit 1")
    String lastText(String room);

    @Query("select date from TelegramData where room = :room order by nid desc limit 1")
    String lastDate(String room);

    @Query("update TelegramData set read = '1' where room = :room and read = '2'")
    void read(String room);

    @Query("update TelegramData set read = '1' where read = '2'")
    void allRead();


//    public class NotiData {
//        public String name;
//        public String text;
//        public String room;
//        public String date;
//        public String vsDate;
//        public String send;
//    }

}
