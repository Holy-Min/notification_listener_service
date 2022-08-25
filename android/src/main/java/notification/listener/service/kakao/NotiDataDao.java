package android.src.main.java.notification.listener.service.kakao;

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

    @Insert
    void insert(NotiData noti);

    @Delete
    void delete(NotiData noti);

}
