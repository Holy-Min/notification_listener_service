package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FacebookDataDao {
    @Query("SELECT * FROM FacebookData")
    List<FacebookData> getAll();

    @Query("SELECT * FROM FacebookData WHERE room= :room")
    List<FacebookData> getRoom(String room);

//    @Query("INSERT INTO NotiData VALUES(name= :name, text= :text, room= :room, date= :date, send= :send)")
    @Insert
    void addChat(FacebookData noti);

    @Insert
    void insert(FacebookData noti);

    @Query("DELETE FROM FacebookData WHERE date(vsDate) < date('now', '-3 days')")
    void delete();

//    @Query("update FacebookData set result = :result, url = :url where nid = (select nid from (select * from FacebookData where url = 'yet' and result = 'yet' limit 1))")
//    void update(String result, String url);

    @Query("update FacebookData set result = :result, url = :url, text = :text where nid = :nid")
    void update(String result, String url, int nid, String text);

//    @Query("update FacebookData set result = :result where nid = (select nid from (select * from FacebookData where result = 'yet' limit 1))")
//    void update(String result);
//
//    @Query("update FacebookData set url = :url where nid = (select nid from (select * from FacebookData where url = 'yet' limit 1))")
//    void updateUrl(String url);

//    @Query("update FacebookData set read = 1 where room = (select nid from (select * from FacebookData where read = 2))")
//    void update(String result);

    @Query("DELETE FROM FacebookData WHERE room = :room")
    void roomDelete(String room);

    @Query("DELETE FROM FacebookData")
    void deleteAll();

    @Query("select * from FacebookData where room = :room order by nid desc limit 1")
    List<FacebookData> select(String room);

    @Query("SELECT * FROM FacebookData WHERE result = 'yet'")
    List<FacebookData> undetectedSelect();
    
    @Query("SELECT * FROM FacebookData order by nid desc limit 1")
    List<FacebookData> lastOneSelect();

    @Query("select count(*) from FacebookData where room = :room and read = '2'")
    int roomCount(String room);

    @Query("select count(*) from FacebookData")
    int total();

    @Query("select count(*) from FacebookData where result NOT in ('101', '111', '201', '211', '301', '311', 'yet', 'N')")
    int resultCount();

    @Query("select text from FacebookData where room = :room order by nid desc limit 1")
    String lastText(String room);

    @Query("select date from FacebookData where room = :room order by nid desc limit 1")
    String lastDate(String room);

    @Query("update FacebookData set read = '1' where room = :room and read = '2'")
    void read(String room);

    @Query("update FacebookData set read = '1' where read = '2'")
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
