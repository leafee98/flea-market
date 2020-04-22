package model.dao.repo;

import model.dao.entity.SocialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepo extends JpaRepository<SocialEntity, Long> {
}
