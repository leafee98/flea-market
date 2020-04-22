package model.dao.repo;

import model.dao.entity.ProductCommentEntity;
import model.dao.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCommentRepo extends JpaRepository<ProductCommentEntity, Long> {
    List<ProductCommentEntity> findByProductEquals(ProductEntity product);

}
