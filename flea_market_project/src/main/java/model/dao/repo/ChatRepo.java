package model.dao.repo;

import model.dao.entity.ChatEntity;
import model.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<ChatEntity, Long> {
    @Query("select c from ChatEntity c where :user1 in (c.userList) and :user2 in (c.userList) order by c.userList.size")
    List<ChatEntity> findByChatMember(@Param("user1") UserEntity user1, @Param("user2") UserEntity user2);

    @Query("select c from ChatEntity c where :user in (c.userList) order by c.userList.size")
    List<ChatEntity> findByChatMember(@Param("user") UserEntity user);
}
