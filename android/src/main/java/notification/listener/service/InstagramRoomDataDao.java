package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface InstagramRoomDataDao {
    @Query("SELECT * FROM InstagramRoomData")
    List<InstagramRoomData> getAll();

    @Query("SELECT * FROM InstagramRoomData WHERE room = :room")
    int checkId(String room);

    @Insert
    void insert(InstagramRoomData room);

    @Query("DELETE FROM InstagramRoomData WHERE room = :room")
    void delete(String room);

    @Query("DELETE FROM InstagramRoomData")
    void deleteAll();

    @Query("delete from InstagramRoomData where (select count(*) from InstagramData where room = :room and room = :room) = 0")
    void roomDelete(String room);

    @Query("SELECT room FROM InstagramRoomData")
    List<String> checkRoom();

    @Query("update InstagramRoomData set isSafe = 2 where room = :room")
    void update(String room);

}
