package model.dao.repo;

import model.dao.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepo extends JpaRepository<ChatEntity, Long> {
}
