package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FacebookRoomDataDao {
    @Query("SELECT * FROM FacebookRoomData")
    List<FacebookRoomData> getAll();

    @Query("SELECT * FROM FacebookRoomData WHERE room = :room")
    int checkId(String room);

    @Insert
    void insert(FacebookRoomData room);

    @Query("DELETE FROM FacebookRoomData WHERE room = :room")
    void delete(String room);

    @Query("DELETE FROM FacebookRoomData")
    void deleteAll();

    @Query("delete from FacebookRoomData where (select count(*) from FacebookData where room = :room and room = :room) = 0")
    void roomDelete(String room);

    @Query("SELECT room FROM FacebookRoomData")
    List<String> checkRoom();

    @Query("update FacebookRoomData set isSafe = 2 where room = :room")
    void update(String room);

}
