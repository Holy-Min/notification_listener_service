package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

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

    @Query("delete from roomdata where (select count(*) from NotiData where room = :room) = 0 and room = :room")
    void roomDelete(String room);

    @Query("SELECT room FROM roomData")
    List<String> checkRoom();

    @Query("update RoomData set isSafe = 2 where room = :room")
    void update(String room);

}
