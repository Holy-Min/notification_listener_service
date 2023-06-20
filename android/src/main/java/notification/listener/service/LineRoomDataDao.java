package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LineRoomDataDao {
    @Query("SELECT * FROM LineRoomData")
    List<LineRoomData> getAll();

    @Query("SELECT * FROM LineRoomData WHERE room = :room")
    int checkId(String room);

    @Insert
    void insert(LineRoomData room);

    @Query("DELETE FROM LineRoomData WHERE room = :room")
    void delete(String room);

    @Query("DELETE FROM LineRoomData")
    void deleteAll();

    @Query("delete from LineRoomData where (select count(*) from LineData where room = :room and room = :room) = 0")
    void roomDelete(String room);

    @Query("SELECT room FROM LineRoomData")
    List<String> checkRoom();

    @Query("update LineRoomData set isSafe = 2 where room = :room")
    void update(String room);

}
