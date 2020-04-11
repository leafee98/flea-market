package model.dao.repo;

import model.dao.entity.ChatMsgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMsgRepo extends JpaRepository<ChatMsgEntity, Long> {
}
