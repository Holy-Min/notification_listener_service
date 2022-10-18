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

    @Query("update NotiData set result = :result where nid = (select nid from (select * from NotiData where result = 'yet' limit 1))")
    void update(String result);

    @Query("DELETE FROM NotiData WHERE room = :room")
    void roomDelete(String room);

    @Query("select * from NotiData where room = :room order by nid desc limit 1")
    List<NotiData> select(String room);

    @Query("SELECT * FROM NotiData WHERE result = 'yet'")
    List<NotiData> undetectedSelect();

    @Query("select count(*) from NotiData where room = :room and read = '2'")
    int roomCount(String room);

    @Query("select count(*) from NotiData")
    int total();

    @Query("select count(*) from NotiData where result = '2'")
    int resultCount();

    @Query("select text from NotiData where room = :room order by nid desc limit 1")
    String lastText(String room);

    @Query("select date from NotiData where room = :room order by nid desc limit 1")
    String lastDate(String room);

    @Query("update NotiData set read = '1' where room = :room and read = '2'")
    void read(String room);
    
//     @Query("vacuum")
//     void vacuum();
    
    @RawQuery
    int vacuum(SupportSQLiteQuery supportSQLiteQuery);

//    public class NotiData {
//        public String name;
//        public String text;
//        public String room;
//        public String date;
//        public String vsDate;
//        public String send;
//    }

}
