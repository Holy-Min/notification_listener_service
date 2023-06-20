package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LineDataDao {
    @Query("SELECT * FROM LineData")
    List<LineData> getAll();

    @Query("SELECT * FROM LineData WHERE room= :room")
    List<LineData> getRoom(String room);

//    @Query("INSERT INTO NotiData VALUES(name= :name, text= :text, room= :room, date= :date, send= :send)")
    @Insert
    void addChat(LineData noti);

    @Insert
    void insert(LineData noti);

    @Query("DELETE FROM LineData WHERE date(vsDate) < date('now', '-3 days')")
    void delete();

//    @Query("update LineData set result = :result, url = :url where nid = (select nid from (select * from LineData where url = 'yet' and result = 'yet' limit 1))")
//    void update(String result, String url);

    @Query("update LineData set result = :result, url = :url, text = :text where nid = :nid")
    void update(String result, String url, int nid, String text);

//    @Query("update LineData set result = :result where nid = (select nid from (select * from LineData where result = 'yet' limit 1))")
//    void update(String result);
//
//    @Query("update LineData set url = :url where nid = (select nid from (select * from LineData where url = 'yet' limit 1))")
//    void updateUrl(String url);

//    @Query("update LineData set read = 1 where room = (select nid from (select * from LineData where read = 2))")
//    void update(String result);

    @Query("DELETE FROM LineData WHERE room = :room")
    void roomDelete(String room);

    @Query("DELETE FROM LineData")
    void deleteAll();

    @Query("select * from LineData where room = :room order by nid desc limit 1")
    List<LineData> select(String room);

    @Query("SELECT * FROM LineData WHERE result = 'yet'")
    List<LineData> undetectedSelect();
    
    @Query("SELECT * FROM LineData order by nid desc limit 1")
    List<LineData> lastOneSelect();

    @Query("select count(*) from LineData where room = :room and read = '2'")
    int roomCount(String room);

    @Query("select count(*) from LineData")
    int total();

    @Query("select count(*) from LineData where result NOT in ('101', '111', '201', '211', '301', '311', 'yet', 'N')")
    int resultCount();

    @Query("select text from LineData where room = :room order by nid desc limit 1")
    String lastText(String room);

    @Query("select date from LineData where room = :room order by nid desc limit 1")
    String lastDate(String room);

    @Query("update LineData set read = '1' where room = :room and read = '2'")
    void read(String room);

    @Query("update LineData set read = '1' where read = '2'")
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
