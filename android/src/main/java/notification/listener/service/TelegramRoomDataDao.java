package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TelegramRoomDataDao {
    @Query("SELECT * FROM TelegramRoomData")
    List<TelegramRoomData> getAll();

    @Query("SELECT * FROM TelegramRoomData WHERE room = :room")
    int checkId(String room);

    @Insert
    void insert(TelegramRoomData room);

    @Query("DELETE FROM TelegramRoomData WHERE room = :room")
    void delete(String room);

    @Query("DELETE FROM TelegramRoomData")
    void deleteAll();

    @Query("delete from TelegramRoomData where (select count(*) from TelegramData where room = :room and room = :room) = 0")
    void roomDelete(String room);

    @Query("SELECT room FROM TelegramRoomData")
    List<String> checkRoom();

    @Query("update TelegramRoomData set isSafe = 2 where room = :room")
    void update(String room);

}
