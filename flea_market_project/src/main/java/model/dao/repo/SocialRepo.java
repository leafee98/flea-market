package model.dao.repo;

import model.dao.entity.SocialEntity;
import model.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialRepo extends JpaRepository<SocialEntity, Long> {
    List<SocialEntity> findByUserIsAndSocialTypeEqualsAndSocialUrlEquals(
            UserEntity user, String socialType, String socialUrl);
}
