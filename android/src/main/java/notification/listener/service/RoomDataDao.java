package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface RoomDataDao {
    @Query("SELECT * FROM RoomData")
    List<RoomData> getAll();

    @Query("SELECT * FROM RoomData WHERE room = :room")
    int checkId(String room);

    @Insert
    void insert(RoomData room);

    @Query("DELETE FROM RoomData WHERE room = :room")
    void delete(String room);

    @Query("DELETE FROM RoomData")
    void deleteAll();

    @Query("DELETE FROM RoomData WHERE date(vsDate) < date('now', '-3 days')")
    void roomDelete();

}
