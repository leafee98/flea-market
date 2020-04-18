package model.dao.repo;

import model.dao.entity.ChatEntity;
import model.dao.entity.ChatMsgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMsgRepo extends JpaRepository<ChatMsgEntity, Long> {
    List<ChatMsgEntity> findByChatEquals(ChatEntity chat);
}
