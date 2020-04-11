package model.dao.repo;

import model.dao.entity.BulletinMsgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BulletinMsgRepo extends JpaRepository<BulletinMsgEntity, Long> {
}
