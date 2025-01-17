package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface KakaoRoomDataDao {
    @Query("SELECT * FROM KakaoRoomData")
    List<KakaoRoomData> getAll();

    @Query("SELECT * FROM KakaoRoomData WHERE room = :room")
    int checkId(String room);

    @Insert
    void insert(KakaoRoomData room);

    @Query("DELETE FROM KakaoRoomData WHERE room = :room")
    void delete(String room);

    @Query("DELETE FROM KakaoRoomData")
    void deleteAll();

    @Query("delete from KakaoRoomData where (select count(*) from KakaoData where room = :room and room = :room) = 0")
    void roomDelete(String room);

    @Query("SELECT room FROM KakaoRoomData")
    List<String> checkRoom();

    @Query("update KakaoRoomData set isSafe = 2 where room = :room")
    void update(String room);

}
