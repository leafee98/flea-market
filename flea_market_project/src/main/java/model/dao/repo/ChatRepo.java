package model.dao.repo;

import model.dao.entity.ChatSessionEntity;
import model.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<ChatSessionEntity, Long> {
    @Query("select c from ChatSessionEntity c where :user1 in (select user from c.userList user) and :user2 in (select user from c.userList user) order by size(c.userList)")
    List<ChatSessionEntity> findByChatMember(@Param("user1") UserEntity user1, @Param("user2") UserEntity user2);

    @Query("select c from ChatSessionEntity c where :user in (select user from c.userList user) order by size(c.userList)")
    List<ChatSessionEntity> findByChatMember(@Param("user") UserEntity user);
}
