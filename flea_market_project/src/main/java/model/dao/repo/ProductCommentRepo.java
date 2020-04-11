package model.dao.repo;

import model.dao.entity.ProductCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCommentRepo extends JpaRepository<ProductCommentEntity, Long> {
}
