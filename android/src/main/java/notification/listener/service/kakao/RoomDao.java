package android.src.main.java.notification.listener.service.kakao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

@Dao
public interface RoomDataDao {
    @Query("SELECT * FROM RoomData")
    List<RoomData> getAll();

    @Query("SELECT * FROM RoomData WHERE room = :room")
    Int checkId(String room);

    @Insert
    void insert(RoomData room);


}
