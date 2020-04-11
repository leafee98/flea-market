package model.dao.repo;

import model.dao.entity.ProductPicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPicRepo extends JpaRepository<ProductPicEntity, Long> {
}
