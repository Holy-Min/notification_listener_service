package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface WhatsappRoomDataDao {
    @Query("SELECT * FROM WhatsappRoomData")
    List<WhatsappRoomData> getAll();

    @Query("SELECT * FROM WhatsappRoomData WHERE room = :room")
    int checkId(String room);

    @Insert
    void insert(WhatsappRoomData room);

    @Query("DELETE FROM WhatsappRoomData WHERE room = :room")
    void delete(String room);

    @Query("DELETE FROM WhatsappRoomData")
    void deleteAll();

    @Query("DELETE FROM WhatsappRoomData WHERE date(vsDate) < date('now', '-3 days')")
    void roomDelete();

}
