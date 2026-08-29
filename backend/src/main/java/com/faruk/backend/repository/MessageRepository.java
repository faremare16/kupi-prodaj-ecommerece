package com.faruk.backend.repository;

import com.faruk.backend.entity.Message;
import com.faruk.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // pronalazi historiju izmedju dva korisnika
    @Query("SELECT m FROM Message m WHERE (m.sender.username = :user1 AND m.receiver.username = :user2) " +
            "OR (m.sender.username = :user2 AND m.receiver.username = :user1) ORDER BY m.timeStamp ASC")
    List<Message> findChatHistory(@Param("user1") String user1, @Param("user2") String user2);

    @Query("SELECT DISTINCT m.receiver.username FROM Message m WHERE m.sender.username = :username " +
            "UNION " +
            "SELECT DISTINCT m.sender.username FROM Message m WHERE m.receiver.username = :username")
    List<String> findDistinctChatPartners(@Param("username") String username);

}
