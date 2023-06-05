package notification.listener.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageListDataDao {
    @Query("SELECT name FROM MessageListData")
    List<String> getAll();

    @Query("SELECT count(*) FROM MessageListData")
    int countList();

    @Insert
    void insert(MessageListData noti);

    @Query("DELETE FROM MessageListData WHERE name = :name")
    void delete(String name);

}
