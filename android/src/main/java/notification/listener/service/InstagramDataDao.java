package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface InstagramDataDao {
    @Query("SELECT * FROM InstagramData")
    List<InstagramData> getAll();

    @Query("SELECT * FROM InstagramData WHERE room= :room")
    List<InstagramData> getRoom(String room);

//    @Query("INSERT INTO NotiData VALUES(name= :name, text= :text, room= :room, date= :date, send= :send)")
    @Insert
    void addChat(InstagramData noti);

    @Insert
    void insert(InstagramData noti);

    @Query("DELETE FROM InstagramData WHERE date(vsDate) < date('now', '-3 days')")
    void delete();

//    @Query("update InstagramData set result = :result, url = :url where nid = (select nid from (select * from InstagramData where url = 'yet' and result = 'yet' limit 1))")
//    void update(String result, String url);

    @Query("update InstagramData set result = :result, url = :url, text = :text where nid = :nid")
    void update(String result, String url, int nid, String text);

//    @Query("update InstagramData set result = :result where nid = (select nid from (select * from InstagramData where result = 'yet' limit 1))")
//    void update(String result);
//
//    @Query("update InstagramData set url = :url where nid = (select nid from (select * from InstagramData where url = 'yet' limit 1))")
//    void updateUrl(String url);

//    @Query("update InstagramData set read = 1 where room = (select nid from (select * from InstagramData where read = 2))")
//    void update(String result);

    @Query("DELETE FROM InstagramData WHERE room = :room")
    void roomDelete(String room);

    @Query("DELETE FROM InstagramData")
    void deleteAll();

    @Query("select * from InstagramData where room = :room order by nid desc limit 1")
    List<InstagramData> select(String room);

    @Query("SELECT * FROM InstagramData WHERE result = 'yet'")
    List<InstagramData> undetectedSelect();
    
    @Query("SELECT * FROM InstagramData order by nid desc limit 1")
    List<InstagramData> lastOneSelect();

    @Query("select count(*) from InstagramData where room = :room and read = '2'")
    int roomCount(String room);

    @Query("select count(*) from InstagramData")
    int total();

    @Query("select count(*) from InstagramData where result NOT in ('101', '111', '201', '211', '301', '311', 'yet', 'N')")
    int resultCount();

    @Query("select text from InstagramData where room = :room order by nid desc limit 1")
    String lastText(String room);

    @Query("select date from InstagramData where room = :room order by nid desc limit 1")
    String lastDate(String room);

    @Query("update InstagramData set read = '1' where room = :room and read = '2'")
    void read(String room);

    @Query("update InstagramData set read = '1' where read = '2'")
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
