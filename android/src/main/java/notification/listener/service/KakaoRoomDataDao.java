package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

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

}