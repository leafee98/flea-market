package model.dao.repo;

import model.dao.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepo extends JpaRepository<TokenEntity, Long> {
    // @Query("SELECT t FROM TokenEntity t WHERE t.token = :token")
    // List<TokenEntity> findByToken(String token);

    List<TokenEntity> findByTokenEquals(String token);
}
